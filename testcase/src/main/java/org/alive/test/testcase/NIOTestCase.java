package org.alive.test.testcase;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.Selector;
import java.nio.charset.Charset;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.alive.test.core.TestCase;

public class NIOTestCase extends TestCase {

	public NIOTestCase() {
		super();
	}

	public NIOTestCase(String name) {
		super(name);
	}

	public void mtestSelector() throws Exception {
		Selector s1 = Selector.open();

		Selector s2 = Selector.open();

		System.out.println(s1 == s2); // false
		System.out.println(s1.equals(s2)); // false

		s1.close();
		s2.close();
	}

	public void mtestBuffer() throws Exception {
		RandomAccessFile aFile = new RandomAccessFile(new File("nio-data.txt"), "rw");

		FileChannel inChannel = aFile.getChannel();

		ByteBuffer buf = ByteBuffer.allocate(48);

		List<ByteBuffer> buffers = new ArrayList<ByteBuffer>();
		int bytesRead = 0;
		int totalBytesRead = 0;
		// StringBuffer content = new StringBuffer();
		while ((bytesRead = inChannel.read(buf)) != -1) {
			buffers.add(buf);
			buf = ByteBuffer.allocate(48);
			totalBytesRead += bytesRead;
		}

		byte[] data = new byte[totalBytesRead];
		int startIndex = 0;
		for (ByteBuffer bf : buffers) {
			bf.flip();
			System.arraycopy(bf.array(), 0, data, startIndex, bf.remaining());
			startIndex += bf.remaining();
		}

		System.out.println(new String(data, "UTF-8"));

		inChannel.close();
		aFile.close();
	}

	public void mtestFileChannel() throws Exception {
		RandomAccessFile aFile = new RandomAccessFile(new File("nio-data.txt"), "rw");

		FileChannel inChannel = aFile.getChannel();

		ByteBuffer buf = ByteBuffer.allocate(48);

		int bytesRead = -1;
		// StringBuffer content = new StringBuffer();
		Charset cset = Charset.forName("UTF-8");
		while ((bytesRead = inChannel.read(buf)) != -1) {

			// System.out.println("Read " + bytesRead);
			buf.flip();

			// while (buf.hasRemaining()) {
			// System.out.print((char) buf.get());
			// }
			System.out.print(cset.decode(buf).toString());

			buf.clear();
			// bytesRead = inChannel.read(buf);
		}
		inChannel.close();
		aFile.close();
	}

	/**
	 * 在Windows下，Sun的Java虚拟机在Selector.open()时会自己和自己建立loopback的TCP链接；在Linux下，
	 * Selector会创建pipe。这主要是为了Selector.wakeup()可以方便唤醒阻塞在select()系统调用上的线程（
	 * 通过向自己所建立的TCP链接和管道上随便写点什么就可以唤醒阻塞线程）
	 * 
	 * 测试方法：执行完测试方法，线程休眠的时候，使用netstat -an查看系统里的Socket连接；看到两个相邻端口互相连接的就是
	 * loopback的TCP链接，如下所示
	 * 
	 * <pre>
	 * <code>
	 *  TCP    127.0.0.1:1360         127.0.0.1:1361         ESTABLISHED
	 *  TCP    127.0.0.1:1361         127.0.0.1:1360         ESTABLISHED
	 *  TCP    127.0.0.1:1362         127.0.0.1:1363         ESTABLISHED
	 *  TCP    127.0.0.1:1363         127.0.0.1:1362         ESTABLISHED
	 *  TCP    127.0.0.1:1364         127.0.0.1:1365         ESTABLISHED
	 *  TCP    127.0.0.1:1365         127.0.0.1:1364         ESTABLISHED
	 *  TCP    127.0.0.1:1366         127.0.0.1:1367         ESTABLISHED
	 *  TCP    127.0.0.1:1367         127.0.0.1:1366         ESTABLISHED
	 *  TCP    127.0.0.1:1368         127.0.0.1:1369         ESTABLISHED
	 *  TCP    127.0.0.1:1369         127.0.0.1:1368         ESTABLISHED
	 *  </code>
	 * </pre>
	 * 
	 * @throws Exception
	 */
	public void mtestLoopbackConnection() throws Exception {
		int size = 5;
		Selector[] sels = new Selector[size];
		for (int i = 0; i < size; i++) {
			sels[i] = Selector.open();
		}

		Thread.sleep(30 * 1000);
	}

	/**
	 * 按GBK读取文件内容，转换为字符串，注意，因为文件一般不会太大，故全部读取进来也无妨，文件如果超过10M，需要打印WARNING日志
	 * 
	 * @param file
	 * @return
	 */
	public static String getFileContent(String file) {
		FileChannel channel = null;
		FileInputStream fs = null;
		try {
			File f = new File(file);
			long size = f.length();
			if (size > 1024 * 1024 * 5) {
				System.out.println("文件超过5M，大小为" + size);
			}

			Thread.sleep(5000);
			fs = new FileInputStream(f);
			channel = fs.getChannel();
			ByteBuffer byteBuffer = ByteBuffer.allocate((int) channel.size());
			while ((channel.read(byteBuffer)) > 0) {
			}
			return new String(byteBuffer.array());

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (channel != null) {
					channel.close();
				}
			} catch (IOException e) {
				// e.printStackTrace();
			}
			try {
				if (fs != null) {
					fs.close();
				}
			} catch (IOException e) {
				// e.printStackTrace();
			}
		}

		return null;
	}

	public void mtestFileRead() {
		String file = "d:/myumen/Temp/shgeb.log";
		System.out.println(getFileContent(file));
	}

	/**
	 * 测试使用DirectBuffer，任务管理器可以明显看到内在占用变化
	 */
	public void testDirectByteBuffer() {
		// 512M
		
		System.out.println("申请DirectByteBuffer...");
		ByteBuffer bb = ByteBuffer.allocateDirect(1024 * 1024 * 512);

		try {
			TimeUnit.SECONDS.sleep(10);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// File
		// ((DirectBuffer) bb).cleaner().clean();

		System.out.println("释放DirectByteBuffer...");
		try {
			Object cleaner = invoke(bb, "cleaner");
			invoke(cleaner, "clean");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			TimeUnit.SECONDS.sleep(10);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static Object invoke(final Object target, String methodName) throws Exception {
		final Method method = target.getClass().getMethod(methodName);
		method.setAccessible(true);
		return method.invoke(target);
		/**
		return AccessController.doPrivileged(new PrivilegedAction<Object>() {
			@Override
			public Object run() {
				try {
					return method.invoke(target);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		});
		
		*/
	}
}
