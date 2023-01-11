package org.alive.learn.heartbeat;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import org.alive.learn.heartbeat.biz.BizHandler;
import org.alive.learn.heartbeat.biz.ClientBizHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * SubReactor线程，此类的功能类似于Netty的NioEventLoop。具体如下：
 * <ul>
 * <li>接收SocketChannel注册；
 * <li>循环Select，处理就绪的通道的读写事件，负责读写数据；SelectionKey的attachment为ByteBuffer，则表示有数据需要写回到channel；写回的数据包括心跳；
 * <li>将业务报文转发到业务线程池处理；
 * </ul>
 * <p>
 * 注册、写数据，都是通过添加Task处理，如果有数据需要写回，则将数据对应的ByteBuffer，通过attachment形式注册给selector，下一次select时写回到channel；
 * 
 * @author hailin84
 *
 */
public class SubReactorThread extends Thread {
	
	public static final Logger logger = LoggerFactory.getLogger(SubReactorThread.class);

	protected Selector selector;

	protected ExecutorService bizExecutorService;

	protected Queue<MyNioTask> taskQueue = new ConcurrentLinkedQueue<>();

	protected volatile boolean running = true;

	/** SubReactor所属的ChannelManager */
	protected ChannelManager parent;

	/**
	 * 业务线程池
	 * 
	 * @param businessExecutorPool
	 */
	public SubReactorThread(ExecutorService bizExecutorService, ChannelManager parent) {
		try {
			this.bizExecutorService = bizExecutorService;
			this.parent = parent;
			this.selector = Selector.open();
		} catch (IOException e) {
			logger.error("SubReactor初始化失败", e);
		}
	}

	public void register(MyNioTask task) {
		if (task != null) {
			taskQueue.add(task);
		}
	}

	@Override
	public void run() {
		while (running) {
			// (I): 处理selector.select
			int num = 0;
			try {
				// 用selectNow不阻塞,立即返回，因为即使没有通道就绪，taskQueue里也可能有任务需要处理
				num = selector.selectNow();
			} catch (IOException e) {
				logger.error("select异常", e);
			}
			if (num > 0) {
				Set<SelectionKey> st = selector.selectedKeys();
				Iterator<SelectionKey> itor = st.iterator();

				while (itor.hasNext()) {
					process(itor.next());
				}
				st.clear();
			}

			// (II): 处理MyNioTask队列
			if (!taskQueue.isEmpty()) {
				MyNioTask task = null;
				// 计数器，每次只处理最多5个Task，就再次回到select，不然可能一直在处理task，channel状态来不及处理；
				// Netty是通过时间花销比例来进行控制的；
				int counter = 0;
				while ((task = taskQueue.poll()) != null) {
					try {
						SocketChannel sc = task.getChannel();
						SelectionKey key = null;
						// task.getData有可能为null，表示首次注册；有可能不为null，表示有数据需要写回
						key = sc.register(selector, task.getOp(), task.getData());
						
						// 将SocketChannel注册到selector的时候，需要将对应的SelectionKey设置到ExtendSocketChannel中，发送心跳的时候需要用到；
						// 实际上只需要第一次注册的时候设置就行，后续每次注册，返回的都是同一个SelectionKey对象；
						parent.setKeyToExtendChannel(sc, key);
					} catch (Throwable e) {
						logger.error("处理异常", e);
					}
					if (counter++ > 5) {
						break;
					}
				}
			}

			// (III): 适当休眠以防止空循环导致CPU占用100%，用wait/notify实现可能更好一些；根据实际性能调整sleep时间；
			try {
				TimeUnit.MILLISECONDS.sleep(1);
			} catch (InterruptedException e) {
				// 收到中断的话，马上退出while循环
				logger.info("{} 线程收到中断退出循环", getName());
				break;
			}

		}
		logger.info("结束线程 {}", getName());
	}

	/**
	 * 处理主要的Nio读写事件
	 * 
	 * @param key
	 */
	protected void process(SelectionKey key) {
		if (!key.isValid()) {
			return;
		}

		SocketChannel sc = (SocketChannel) key.channel();
		ExtendSocketChannel es = parent.getChannelMap().get(sc);
		// 当心跳连接关闭了channel后，这时可能还会进入一次，这时es为null，需要进行判断，能进入isWriteabel和isReadable则必然不会为null
		MyNode node = (es != null ? es.getNode() : null);

		// 双工模式连接，是有可能同时具备可读和可写的，故不能用if else if这样的语句
		if (key.isWritable()) {
			ByteBuffer buf = (ByteBuffer) key.attachment();
			if (buf != null) {
				try {
					sc.write(buf);
					es.setLastSendDataTime(System.currentTimeMillis());
					key.attach(null);
				} catch (IOException e) {
					logger.info("{} IO写数据异常 {}", node, e);
				}
			}
		}

		if (key.isReadable()) {
			ByteBuffer buf = ByteBuffer.allocate(512);
			int headLen = BaseMessage.HEAD_LEN;
			int ret = 0;
			int readBytes = 0;
			try {
				// 判断条件为大于0，等于0表示读取完毕，退出循环
				while ((ret = sc.read(buf)) > 0) {
					readBytes += ret;
					if (!buf.hasRemaining()) { // 读取操作会直到buffer没有remaning,所以这里安全，下次会再读取
						break;
					}
				}
			} catch (IOException e) {
				// 远程关闭后会抛出IO异常，关闭SocketChannel
				logger.info("{} IO读取异常 {}", node, e);
				parent.closeChannel(sc);
			}

			if (readBytes > 0) {
				// 有读取到数据，重置心跳和最后接收时间
				es.resetHeartBeatCount();
				es.setLastRecvDataTime(System.currentTimeMillis());

				// 数据先存入到缓存中, ByteBuffer.array返回的是整个数组，包括无效部分，所以加上limit限制
				buf.flip();
				CircleByteBuffer readBuffer = es.getReadBuffer();
				readBuffer.storeData(buf.array(), buf.limit());

				// 读取到的数据超过12字节，能组成一个数据包
				int readableBytes = readBuffer.getReadableBytes();
				if (readableBytes >= headLen) {
					// 预读取12字节消息头部，从中获取业务报文长度，因为不能确定缓冲区中数据是否够，所以先预读取12字节，
					// 后面确认长度够一个完整报文之后，再从缓存中读取数据
					BaseMessage msg = new BaseMessage(readBuffer.fetchData(headLen, true));
					msg.setChannel(sc);
					msg.setExtendChannel(es);

					if (msg.getMessageType() == BaseMessage.MESSAGE_TYPE_HB) {
						logger.debug("{} 收到：心跳 ", node);
						readBuffer.fetchData(headLen);
					} else if (msg.getMessageType() == BaseMessage.MESSAGE_TYPE_BIZ) {
						int length = msg.getLength();
						if (readableBytes >= headLen + length) {
							readBuffer.fetchData(headLen);
							msg.setMessageBody(readBuffer.fetchData(length));
							logger.info("{} 收到: {}", node, msg.toString());
							// 这里就需要根据客户端和服务端做不同的处理
							if (parent.isClientSide()) {
								this.bizExecutorService.submit(new ClientBizHandler(msg, this));
							} else {
								// 服务端，调用业务处理逻辑进行处理
								this.bizExecutorService.submit(new BizHandler(msg, this));
							}
						}
					} else {
						logger.info("{} 非法输入，丢弃 ", node);
						readBuffer.fetchData(12, false);
						// readBuffer.clear();
					}
				}

			}

			// read返回-1说明客户端的数据发送完毕，并且主动的close
			// socket。所以在这种场景下，（服务器程序）你需要关闭socketChannel并且取消key，最好是退出当前函数。
			// 注意，这个时候服务端要是继续使用该socketChannel进行读操作的话，就会抛出"远程主机强迫关闭一个现有的连接"的IO异常。
			//
			if (ret < 0) {
				logger.info("{} 远程主机断开连接，关闭SocketChannel", node);
				parent.closeChannel(sc);
			}
		}
	}

	public void close() {
		logger.info("close - SubReactor线程");
		this.running = false;
		this.interrupt(); // 发送中断，如果当前线程在sleep中，也可以立即返回
		try {
			this.selector.close();
		} catch (IOException e) {
			logger.info("{} 线程close异常", getName());
		}
	}
}
