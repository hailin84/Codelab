package org.alive.test.testcase;

import java.nio.ByteBuffer;
import java.util.Arrays;

import org.alive.test.core.TestCase;

public class ByteBufferTestCase extends TestCase {

	public ByteBufferTestCase(String name) {
		super(name);
	}

	public void testBuffer() {
		ByteBuffer buf = ByteBuffer.allocate(6);
		buf.put((byte) 3);
		
		buf.position(0);  //设置position到0位置，这样读数据时就从这个位置开始读
		buf.limit(1);     //设置limit为1，表示当前bytebuffer的有效数据长度是1
		
		buf.get();
		System.out.println(buf);
		
		buf = ByteBuffer.allocate(6);
		buf.put((byte) 'A');
		// buf.put((byte) 'B');
		buf.flip();
		
		System.out.println(Arrays.toString(buf.array()));
		System.out.println(buf.limit());
	}
}
