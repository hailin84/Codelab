package org.alive.test.testcase;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import org.alive.test.core.TestCase;

/**
 * 测试Java wait notify notifyAll方法
 * 
 * @author hailin84
 *
 */
public class NotifyTestCase extends TestCase {

	public NotifyTestCase(String name) {
		super(name);
	}

	public void testNofity() {
		Queue<Integer> queue = new LinkedList<Integer>();
		int maxSize = 10;
		
		new Producer(queue, maxSize, "P-1").start();
		new Producer(queue, maxSize, "P-2").start();
		// new Producer(queue, maxSize, "P-3").start();
		new Consumer(queue, maxSize, "C-1").start();
		new Consumer(queue, maxSize, "C-2").start();
		
		// 输入直到回车后退出循环
		try {
			System.in.read();
			DataHolder.running = false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

class DataHolder {
	public static volatile boolean running = true;
}
/**
 * 消费者线程
 * 
 * @author hailin84
 *
 */
class Consumer extends Thread {
	private Queue<Integer> queue;
	private int maxSize;

	public Consumer(Queue<Integer> queue, int maxSize, String name) {
		super(name);
		this.queue = queue;
		this.maxSize = maxSize;
	}
	
	public void run() {
		while (DataHolder.running) {
			synchronized (queue) {
				while (queue.isEmpty()) {
					System.out.printf("Queue is empty, Consumer thread %s is waiting...[Empty]", this.getName()).println();
					try {
						queue.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				
				System.out.printf("%s is consuming value %s ", this.getName(), queue.remove()).println();
				queue.notifyAll();
			}
//			try {
//				sleep(1000);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		}
	}
}

class Producer extends Thread {
	private Queue<Integer> queue;
	private int maxSize;

	public Producer(Queue<Integer> queue, int maxSize, String name) {
		super(name);
		this.queue = queue;
		this.maxSize = maxSize;
	}

	public void run() {
		while (DataHolder.running) {
			synchronized (queue) {
				while (queue.size() == maxSize) {
					try {
						System.out.printf("Queue is full, Producer thread %s is waiting...[Full]", this.getName()).println();
						queue.wait();
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
				Random random = new Random();
				int i = random.nextInt();
				System.out.printf("%s is producing value %s ", this.getName(), i).println();
				// System.out.println("Producing value : " + i);
				queue.add(i);
				queue.notifyAll();
			}
//			
//			try {
//				sleep(1000);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		}
	}
}