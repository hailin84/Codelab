package org.alive.test.testcase;

import java.util.Arrays;

import org.alive.learn.heartbeat.CircleByteBuffer;
import org.alive.test.core.TestCase;

public class CircleByteBufferTestCase extends TestCase {

	public CircleByteBufferTestCase(String name) {
		super(name);
	}

	public void mtestCircleBuffer() {
		CircleByteBuffer cb = new CircleByteBuffer(8);

		System.out.println(cb);

		byte[] store = new byte[] { 'A', 'B', 'C' };
		byte[] fetch = null;
		cb.storeData(store);
		System.out.println(cb);
		fetch = cb.fetchData(4, false);
		System.out.println(Arrays.toString(fetch)); // 取4字节，但只有3字节，返回3字节['A', 'B', 'C']
		System.out.println(cb);
		
		store = new byte[] {'D', 'E', 'F', 'G', 'H', 'I', 'J'};
		cb.storeData(store);
		System.out.println(cb);
		
		System.out.println(Arrays.toString(cb.fetchData(3)));
		System.out.println(cb);
	}
	
	public void mtestExtend() {
		CircleByteBuffer cb = new CircleByteBuffer(4);
		byte[] store = new byte[] { 'A', 'B', 'C' };
		// 自动扩充为8
		cb.storeData(store);
		cb.storeData(store);
		System.out.println(cb);
		
		System.out.println(Arrays.toString(cb.fetchData(5)));
		System.out.println(cb);
	}
	
	public void mtestFull() {
		CircleByteBuffer cb = new CircleByteBuffer(10);
		String req = "你好，收到请求消息一";
		cb.storeData(req.getBytes());
		
		String rsp = new String(cb.fetchData(req.getBytes().length));
		System.out.println(rsp);
		req = "hello, this is message2";
		cb.storeData(req.getBytes());
		
		rsp = new String(cb.fetchData(req.getBytes().length));
		System.out.println(rsp);
		
		System.out.println(cb.toString());
	}
	
	public void testPreFetch() {
		CircleByteBuffer cb = new CircleByteBuffer(10);
		String req = "ABCDE";
		cb.storeData(req.getBytes());
		System.out.println(cb.toString());
		String rsp = new String(cb.fetchData(req.getBytes().length, true));
		System.out.println(rsp);
		System.out.println(cb.toString());
		
		cb.fetchData(req.getBytes().length);
		System.out.println(cb.toString());
	}
}
