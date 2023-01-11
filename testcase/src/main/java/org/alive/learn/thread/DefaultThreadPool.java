package org.alive.learn.thread;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p>
 * 线程池简单实现
 * 
 * @author hailin84
 *
 * @param <Job>
 */
public class DefaultThreadPool<Job extends Runnable> implements ThreadPool<Job> {

	/** 最大工作线程数量 */
	private static final int MAX_WORDER_NUM = 20;

	/** 默认工作线程数量 */
	private static final int DEFAULT_WORKER_NUM = 5;

	/** 最小工作线程数量 */
	private static final int MIN_WORKER_NUM = 1;

	/** 待执行任务列表 */
	private LinkedList<Job> jobs = new LinkedList<Job>();

	/** 工作者列表 */
	private List<Worker<Job>> workers = Collections.synchronizedList(new ArrayList<Worker<Job>>());

	/** 当前工作者线程数 */
	private int workerNum = DEFAULT_WORKER_NUM;

	/** 线程编号计数 */
	private AtomicInteger threadNum = new AtomicInteger();

	public DefaultThreadPool() {
		this.initWorkers(workerNum);
	}

	public DefaultThreadPool(int num) {
		this.workerNum = num > MAX_WORDER_NUM ? MAX_WORDER_NUM : (num < MIN_WORKER_NUM ? MIN_WORKER_NUM : num);
		this.initWorkers(workerNum);
	}

	@Override
	public void execute(Job job) {
		if (job != null) {
			// 添加一个Job，并通过线程池
			synchronized (jobs) {
				jobs.addLast(job);
				jobs.notify();
			}
		}
	}

	@Override
	public void shutdown() {
		synchronized (workers) {
			for (Worker<Job> w : workers) {
				w.shutdown();
			}
			// workers.clear();
		}
	}

	@Override
	public void addWorkers(int num) {
		synchronized (jobs) {
			if (num + workerNum > MAX_WORDER_NUM) {
				num = MAX_WORDER_NUM - workerNum;
			}
			this.initWorkers(num);
			this.workerNum += num;
		}

	}

	@Override
	public void removeWorkers(int num) {
		synchronized (jobs) {
			if (num > workerNum) {
				throw new IllegalArgumentException("beyond workNum");
			}

			for (int i = 0; i < num; i++) {
				Worker<Job> w = workers.remove(i);
				w.shutdown();
			}
			this.workerNum -= num;
		}
	}

	@Override
	public int getJobSize() {
		return jobs.size();
	}

	private void initWorkers(int num) {
		for (int i = 0; i < num; i++) {
			Worker<Job> w = new Worker<>(jobs);
			workers.add(w);
			Thread t = new Thread(w, "Threadpool-worker-" + threadNum.getAndIncrement());
			w.setRunnerThread(t);
			t.start();
			// 线程编号0到100000循环使用，一般不会用到
			if (threadNum.get() >= 100000) {
				threadNum.set(0);
			}
		}
	}

	static class Worker<Job extends Runnable> implements Runnable {
		private volatile boolean running = true;

		private LinkedList<Job> jobs;

		/** Worker运行的线程 */
		private Thread runnerThread;

		public Worker(LinkedList<Job> jobs) {
			super();
			this.jobs = jobs;
		}

		public void setRunnerThread(Thread runnerThread) {
			this.runnerThread = runnerThread;
		}

		@Override
		public void run() {
			while (running) {
				Job job = null;
				synchronized (jobs) {
					// 如果没有待处理的job，则wait
					while (jobs.isEmpty()) {
						try {
							jobs.wait();
						} catch (InterruptedException e) {
							// 外部中断WorkerThread，Worker线程结束，外部线程通过调用本线程的interrupt方法，导致wait,join,sleep等方法抛出InterruptedException异常
							Thread.currentThread().interrupt(); // 重新设置中断，这里并没有什么用，但是是编程的常规思路之一，以免依赖于本线程中断状态的上层代码无法得到中断状态
							System.out.printf("%s 被终断退出", Thread.currentThread().getName()).println();
							return;
						}
					}
					job = jobs.removeFirst();
					if (job != null) {
						try {
							job.run();
						} catch (Exception e) {
							// 忽略job执行过程中的异常
							// e.printStackTrace();
						}
					}
				}
			}
		}

		/**
		 * 关闭。因为大多数时候，线程是处于wait(阻塞)状态，故修改了running之后，不会马上退出，需要外部调用本线程的interrupt方法
		 */
		public void shutdown() {
			running = false;
			if (runnerThread != null && runnerThread.isAlive()) {
				runnerThread.interrupt();
			}
		}
	}
}
