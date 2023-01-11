package org.alive.learn.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Priority {
	private static volatile boolean notStart = true;

	private static volatile boolean notEnd = true;

	public static void main(String[] args) {
		List<Job> jobs = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			int p = i < 5 ? Thread.MIN_PRIORITY : Thread.MAX_PRIORITY;
			Job j = new Job(p);
			jobs.add(j);
			Thread t = new Thread(j, "Thread-" + i);
			t.setPriority(p);
			t.start();
		}
		
		notStart = false;
		try {
			TimeUnit.SECONDS.sleep(10);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		notEnd = false;
		for (Job j : jobs) {
			System.out.println("priority: " + j.priority + ", count: " + j.jobCount);
		}
	}

	static class Job implements Runnable {
		private int priority;

		private long jobCount;

		public Job(int priority) {
			this.priority = priority;
			this.jobCount = 0L;
		}

		@Override
		public void run() {
			while (notStart) {
				Thread.yield();
			}
			while (notEnd) {
				Thread.yield();
				jobCount++;
			}
		}

	}
}
