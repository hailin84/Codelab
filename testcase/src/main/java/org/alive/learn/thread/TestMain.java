package org.alive.learn.thread;

import java.sql.Connection;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class TestMain {

	static ConnectionPool pool;
	static CountDownLatch start;
	static CountDownLatch end;

	public static void main(String[] args) throws Exception {
		// testConnectionPool();
		testTwinLock();
		// testForkJoin();
		// testSemaphore();
		// testBoundedQueue();
	}

	public static void testTwinLock() {
		final TwinsLock lock = new TwinsLock();

		class Worker extends Thread {
			public Worker(String name) {
				super(name);
			}

			@Override
			public void run() {
				while (true) {
					lock.lock();

					try {
						SleepUtils.second(1);
						System.out.println(getName());
						SleepUtils.second(1);
					} finally {
						lock.unlock();
					}
				}
			}
		}

		// 启动10个线程
		for (int i = 0; i < 10; i++) {
			Worker w = new Worker("Worker-" + i);
			w.setDaemon(true);
			w.start();
		}
		// 每隔1秒换行
		for (int i = 0; i < 10; i++) {
			SleepUtils.second(1);
			System.out.println();
		}
	}

	public static void testForkJoin() {
		ForkJoinPool pool = new ForkJoinPool(2);
		CountTask task = new CountTask(1, 10);

		Future<Long> result = pool.submit(task);
		try {
			System.out.printf("%s: %d", "结果", result.get()).println();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 即使线程池大小、任务数量都为30，但信号量为10，故最多只能有10个任务并发执行，
	 */
	public static void testSemaphore() {
		int size = 30;
		ExecutorService exe = Executors.newFixedThreadPool(size);
		final Semaphore s = new Semaphore(10);

		for (int i = 0; i < size; i++) {
			exe.execute(new Runnable() {

				@Override
				public void run() {
					try {
						s.acquire();
						System.out.printf("%s - 执行任务", Thread.currentThread().getName()).println();
						s.release();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			});
		}

		exe.shutdown();
	}

	public static void testBoundedQueue() {
		final BoundedQueue<Integer> queue = new BoundedQueue<>(4);
		final Random r = new Random();
		new Thread(new Putter(queue, r), "put-thread1").start();
		new Thread(new Remover(queue, r), "remove-thread1").start();
	}

	static class Putter implements Runnable {
		BoundedQueue<Integer> queue;
		Random r;

		public Putter(BoundedQueue<Integer> queue, Random r) {
			this.queue = queue;
			this.r = r;
		}

		@Override
		public void run() {
			for (int i = 0; i < 10; i++) {
				try {
					int value = r.nextInt();
					queue.add(value);
					System.out.printf("put - %d\tsize=%d", value, queue.getSize()).println();
					SleepUtils.second(1);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	static class Remover implements Runnable {
		BoundedQueue<Integer> queue;
		Random r;

		public Remover(BoundedQueue<Integer> queue, Random r) {
			this.queue = queue;
			this.r = r;
		}

		@Override
		public void run() {
			SleepUtils.second(6);
			for (int i = 0; i < 10; i++) {
				try {
					int value = queue.remove();
					System.out.printf("remove - %d\tsize=%d", value, queue.getSize()).println();
					SleepUtils.second(1);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public static void testConnectionPool() throws Exception {
		pool = new ConnectionPool(10);
		start = new CountDownLatch(1);

		int count = 20;
		int threadCount = 30;
		AtomicInteger got = new AtomicInteger();
		AtomicInteger notGot = new AtomicInteger();
		end = new CountDownLatch(threadCount);

		for (int i = 0; i < threadCount; i++) {
			new Thread(new ConnectionRunner(count, got, notGot), "ConnectionRunner-" + i).start();
		}
		start.countDown();
		end.await();
		System.out.printf("total invoke %d \n", (count * threadCount));
		System.out.printf("got %d \n", got.get());
		System.out.printf("not got %d \n", notGot.get());
	}

	static class ConnectionRunner implements Runnable {
		int count;
		AtomicInteger got;
		AtomicInteger notGot;

		public ConnectionRunner(int count, AtomicInteger got, AtomicInteger notGot) {
			super();
			this.count = count;
			this.got = got;
			this.notGot = notGot;
		}

		@Override
		public void run() {
			try {
				start.await();
			} catch (Exception e) {
				e.printStackTrace();
			}

			while (count > 0) {
				try {
					Connection conn = pool.fetchConnection(1000);
					if (conn != null) {
						try {
							conn.createStatement();
							conn.commit();
						} finally {
							pool.releaseConnection(conn);
							got.incrementAndGet();
						}
					} else {
						notGot.incrementAndGet();
					}
				} catch (Exception ex) {
				} finally {
					count--;
				}
			}
			end.countDown();
		}
	}
}
