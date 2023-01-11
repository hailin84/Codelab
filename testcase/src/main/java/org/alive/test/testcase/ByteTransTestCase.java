package org.alive.test.testcase;

import java.nio.ByteBuffer;
import java.util.Arrays;

import org.alive.test.core.TestCase;
import org.alive.util.ByteTransUtil;

/**
 * 测试各类型和byte[]之类的相互转换变化
 * 
 * @author hailin84
 * 
 */
public class ByteTransTestCase extends TestCase {

	public ByteTransTestCase() {
	}

	public ByteTransTestCase(String name) {
		super(name);
	}
	
	public void testByteInt() {
		int a = 808464432;
		byte[] ab = ByteTransUtil.intToBytes(a);
		byte[] ac = ByteTransUtil.intToByteArray(a, false);
		
		// 0000 -- 808464432
		// 0123 -- 808530483
		// 4567 -- 875902519
		// 89ab -- 943284578
		// cdef -- 1667523942
		byte[] data = new byte[] {'0', '0', '0', '0'};
		int di = ByteTransUtil.byteArrayToInt(data, false);
		System.out.println(di);
		output(ab);
		output(ac);
	}

	public void testByteString() throws Exception {
		String value1 = "ABCDEFG";
		// String.getBytes会进行转码，并且前面可能会放几个字节表示编码格式
		byte[] b1 = value1.getBytes("Unicode");
		System.out.println(value1.length() + " | " + b1.length);
		System.out.println(Arrays.toString(b1));
		System.out.println(b1[0] == 0x41); // false，转码为GBK、UTF-8时为true

		String value2 = "你";
		byte[] b2 = value2.getBytes("Unicode");// 4F,60
		System.out.println(value2.length() + " | " + b2.length);
		System.out.println(Arrays.toString(b2));
		System.out.println(b2[0] == 0xC9);
		System.out.println(0xC9);
		
		int i = 20320;
		byte[] ib = ByteTransUtil.intToByteArray(i);
		System.out.println("ib = " + Arrays.toString(ib));
		String s = new String(ib);
		System.out.println("s = " + s);
		byte[] sb = s.getBytes();
		System.out.println("sb = " + Arrays.toString(sb));
		int si = ByteTransUtil.byteArrayToInt(sb);
		System.out.println("si = " + si);
	}

	public void testByteChar() throws Exception {
		// Java内部字符和字符串都用的Unicode编码表示，汉字'你'对应的Unicode编码为4f60
		// 下面几种方式都可以定义字符'你'
		char c1 = '你';
		char c2 = '\u4f60';
		char c3 = 0x4f60; // 十进制值为20320
		System.out.println(Integer.toHexString(c3)); // 4f60
		System.out.println((int) c1); // 20320
		System.out.println((char) 20320); // 你
		System.out.println(c1 == c2); // true
		System.out.println(c1 == c3); // true
	}

	public void testByteBuffer() {
		ByteBuffer buffer = ByteBuffer.allocate(1);
		buffer.put((byte) 'A');
		byte[] array = buffer.array();
		System.out.println("byte 'A' :" + Arrays.toString(array));

		buffer = ByteBuffer.allocate(2);
		buffer.putChar('A');
		array = buffer.array();
		System.out.println("char 'A' :" + Arrays.toString(array));

		buffer = ByteBuffer.allocate(2);
		buffer.putChar('你');
		array = buffer.array();
		System.out.println("char '你' :" + Arrays.toString(array));
		output(array); // 4f60

		buffer = ByteBuffer.allocate(4);
		buffer.putInt(1000);
		array = buffer.array();
		System.out.println("int 1000 :" + Arrays.toString(array));
		output(array); // 4f60
		System.out.println(Integer.toHexString(1000));
		
		byte[] a2 = ByteTransUtil.intToByteArray1(1000);
		System.out.println("int 10000 :" + Arrays.toString(a2));
		
		System.out.println(ByteTransUtil.byteArrayToInt(array));
		System.out.println(ByteTransUtil.byteArrayToInt2(array));
	}

	private void output(byte[] array) {
		for (byte b : array) {
			// System.out.print(Integer.toHexString(b));
			System.out.print(b);
			System.out.print(",");
			System.out.print((char)b);
			System.out.print(",");
		}
		System.out.println();
	}

}
