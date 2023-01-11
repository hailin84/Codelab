package org.alive.learn.heartbeat;

import java.util.Arrays;

/**
 * <p>
 * Cicle Buffer类，底层采用byte[]实现的循环Buffer，支持存取数据，空间不够时长度为自动变为原来的两倍；
 * 
 * <ul>
 * <li>用途：可做为Nio读取数据的缓存，实现分包处理；
 * <li>本类不是线程安全的；
 * <li>为简单起见，未采用DirectMemory，使用的JVM Heap内存；
 * <li>因为底层采用的数组存储数据，所以数据有可能不连续；如果按照首尾相联的环形来理解，则是连续的；
 * </ul>
 * 
 * @author hailin84
 * @since 2017.06.28
 */
public class CircleByteBuffer {
	/** 默认大小为16字节 */
	private final static int DEFAULT_CAPACITY = 16;

	/** 存放数据的字节数组 */
	private byte[] buf = null;

	/** 容量/大小，最多存放字节数 */
	private int capacity = 0;

	/** 写数据下标，下一个可写的字节对应的数组索引 */
	private int writeIndex = 0;

	/** 读数据下标，下一个可读的字节对应的数组索引 */
	private int readIndex = 0;

	/** 是否为空，为空时，writeIndex和readIndex相等，反过来则不一定是空的，也有可能是满的 */
	private boolean empty = true;

	public CircleByteBuffer() {
		this.capacity = DEFAULT_CAPACITY;
		this.buf = new byte[capacity];
	}

	/**
	 * 
	 * @param cap
	 *            正整数
	 */
	public CircleByteBuffer(int cap) {
		this.capacity = cap;
		this.buf = new byte[cap];
	}

	public void storeData(byte[] data) {
		if (data != null) {
			storeData(data, data.length);
		}
	}

	/**
	 * <p>
	 * 存放byte[]数据，只存入limit指定长度
	 * 
	 * <p>
	 * 如果数据超过可用空间，则进行空间扩展，每次扩展为原来的2倍，直到空间够用；
	 * <p>
	 * 存入数据的时候，分为两种情况：
	 * <p>
	 * 1. 如果writeIndex >= readIndex，此时可用的存储空间可能有两块：
	 * <ul>
	 * <li>(I) 数组尾部，即[writeIndex,
	 * capacity-1]闭区间，长度为capacity-writeIndex，如果够存放，则直接存入数据；不够，则需要将剩余部分存到数组头部；
	 * <li>(II) 数组头部，即[0, readIndex-1]，长度为readIndex，一定是够存入剩余数据的；因为不够的话前面已经扩容了；
	 * </ul>
	 * <p>
	 * 2. 如果writeIndex &lt; readIndex，则表示可用空间在数组中间，是连续区域；直接存储就可以了；
	 * <p>
	 * 只要有数据存入，那么就一定不为空，empty置为false；
	 * 
	 * @param data
	 *            待存入数据
	 * @param limit
	 *            data中有效数据长度
	 */
	public void storeData(byte[] data, int limit) {
		if (data == null || data.length == 0) {
			return;
		}
		if (limit <= 0 || limit > data.length) {
			throw new IllegalArgumentException("参数错误：参数limit值为" + limit + "，超出数组长度范围" + data.length);
		}

		int writeableBytes = getWriteableBytes();
		while (limit > writeableBytes) {
			// 数据超过可用空间，空间扩展后，直到够用
			this.extendCapaticy();
			writeableBytes = getWriteableBytes();
		}

		// 存入数据
		// 可用空间为数组尾部和头部，不连续区域
		if (writeIndex >= readIndex) { // 注意条件是大于等于
			// 数组尾部可用空间
			int arrow = capacity - writeIndex;
			if (limit <= arrow) {
				// 如果尾部空间够用，直接存到尾部
				System.arraycopy(data, 0, buf, writeIndex, limit);
				writeIndex += limit;
				writeIndex = writeIndex % capacity;
			} else {
				// 尾部空间不够有，先存入数组尾部，剩余的数据存到数组头部
				System.arraycopy(data, 0, buf, writeIndex, arrow);
				writeIndex += arrow;
				writeIndex = writeIndex % capacity;

				// 需要存入数组头部的数据长度
				int circle = limit - arrow;
				System.arraycopy(data, arrow, buf, writeIndex, circle);
				writeIndex += circle;
				writeIndex = writeIndex % capacity;
			}
		} else {
			// 可用空间为数组中部连续区域
			System.arraycopy(data, 0, buf, writeIndex, limit);
			writeIndex += limit;
			writeIndex = writeIndex % capacity;
		}

		empty = false;
	}

	/**
	 * <p>
	 * 提取数据，可根据参数preFetch做预提取。如果无数据，则返回null；如果提取数据长度超过存储的长度，则返回剩下的全部数据。
	 * <p>
	 * 数据有可能是连续存放，也有可能不是连续存放，读取也分两种情况：
	 * <p>
	 * 1. 如果writeIndex &gt; readIndex，直接读取能够读取到的全部数据；
	 * <p>
	 * 2. 如果writeIndex &lt;= readIndex，则说明可读取的数据不连续（如果按照环形来理解，则是连续的），有两块区域：
	 * <ul>
	 * <li>(I) 数组尾部，即[readIndex,
	 * capacity-1]闭区间，长度为capacity-readIndex，如果读取的size少于这部分，则直接读取即可；不够，则需要再读取数组头部；
	 * <li>(II) 数组头部，即[0, writeIndex-1]，长度为writeIndex，最多也只有这么多数据可以读取；
	 * </ul>
	 * <p>
	 * 如果提取数据长度超过存储的有效数据长度readableBytes，则返回剩下的全部数据；
	 * <p>
	 * 如果参数preFetch为true，则表示只读取数据，不移到readIndex，即数据不从缓冲区移出，下次仍然可以读取到。
	 * 
	 * @param size
	 *            正整数
	 * @param preFetch
	 *            为true表示预读取，读取后数据仍然能再次读取，一般不需要预读取
	 * @return
	 */
	public byte[] fetchData(int size, boolean preFetch) {
		// 看看还有多少数据可供读取
		int readableBytes = getReadableBytes();

		// 没有数据，返回null
		if (readableBytes == 0) {
			return null;
		}

		// 最终读取的长度
		int len = (size >= readableBytes ? readableBytes : size);
		byte[] target = new byte[len];
		int oldReadIndex = readIndex;

		if (writeIndex > readIndex) {
			// 数据连续
			System.arraycopy(buf, readIndex, target, 0, len);
			readIndex += len;
			readIndex = readIndex % capacity;
		} else {
			// 数据不连续
			int arrow = capacity - readIndex;
			if (len <= arrow) {
				System.arraycopy(buf, readIndex, target, 0, len);
				readIndex += len;
				readIndex = readIndex % capacity;
			} else {
				int circle = len - arrow;
				System.arraycopy(buf, readIndex, target, 0, arrow);
				readIndex += arrow;
				readIndex = readIndex % capacity;
				System.arraycopy(buf, readIndex, target, arrow, circle);
				readIndex += circle;
				readIndex = readIndex % capacity;
			}
		}

		// 如果是预读取，则需要将readIndex复位，以便下次能继续读取同样的数据
		if (preFetch) {
			readIndex = oldReadIndex;
		}

		// 读完数据，如果读写指针相同，则表示读空了
		if (readIndex == writeIndex) {
			empty = true;
		}

		return target;
	}
	
	/**
	 * 直接取数据
	 * 
	 * @param size
	 * @return
	 */
	public byte[] fetchData(int size) {
		return fetchData(size, false);
	}

	/**
	 * 扩容，每次capacity变为原来的2倍
	 */
	private void extendCapaticy() {
		byte[] storedData = fetchData(getReadableBytes(), false);
		this.capacity *= 2;
		this.clear();
		buf = new byte[capacity];
		storeData(storedData);
	}

	/**
	 * 得到可用存储空间大小
	 * 
	 * @return 可用存储字节数
	 */
	public int getWriteableBytes() {
		int writeableBytes = 0;
		if (writeIndex == readIndex) {
			writeableBytes = empty ? capacity : 0;
		} else {
			writeableBytes = writeIndex > readIndex ? (capacity - writeIndex + readIndex) : (readIndex - writeIndex);
		}
		return writeableBytes;
	}

	/**
	 * 得到可读取的数据长度
	 * 
	 * @return 可读取字节数
	 */
	public int getReadableBytes() {
		int readableBytes = 0;
		if (writeIndex == readIndex) {
			readableBytes = empty ? 0 : capacity;
		} else {
			readableBytes = writeIndex > readIndex ? (writeIndex - readIndex) : (capacity - readIndex + writeIndex);
		}
		return readableBytes;
	}

	/**
	 * 清空Buffer，只将属性重置，具体的buf[]不需要重置
	 */
	public void clear() {
		readIndex = writeIndex = 0;
		empty = true;
	}

	public String toString() {
		if (this.buf == null) {
			return null;
		}
		StringBuilder sb = new StringBuilder(capacity + 100);
		sb.append("CircleByteBuffer [\n\tbuf=");
		sb.append(Arrays.toString(buf));
		sb.append("\n\twritePointer:\t" + writeIndex);
		sb.append("\n\treadPointer:\t" + readIndex);
		sb.append("\n\twriteableBytes:\t" + getWriteableBytes());
		sb.append("\n\treadableBytes:\t" + getReadableBytes());
		sb.append("\n\tcapacity:\t" + capacity);
		sb.append("\n\tempty:\t" + empty);
		sb.append("\n]");
		return sb.toString();
	}

	public int getCapacity() {
		return capacity;
	}

	public boolean isEmpty() {
		return empty;
	}
}
