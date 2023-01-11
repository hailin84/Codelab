package org.alive.test.testcase;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.alive.test.core.TestCase;

/**
 * Test java.util.Timer
 * 
 * @author hailin84
 * 
 */
public class TimerTestCase extends TestCase {

	public TimerTestCase(String name) {
		super(name);
	}

	/**
	 * Timer executes all Task sequencely in a single Timer Thread.
	 */
	public void testTimer() {
		Timer timer = new Timer("java.util.Timer -- xuhailin", false);
		System.out.println(Thread.currentThread().getName());
		timer.schedule(new WorkTask("T1"), 0, 2000);

		timer.schedule(new WorkTask("T2"), 0, 3000);
	}

	/**
	 * Execute every task in on alone thread.
	 */
	public void testScheduledExecutor() {
		ScheduledExecutorService service = Executors.newScheduledThreadPool(10);

		long initialDelay1 = 5;
		long period1 = 1;
		// 从现在开始1秒钟之后，每隔1秒钟执行一次job1
		service.scheduleAtFixedRate(new WorkTask("job1"),
				initialDelay1, period1, TimeUnit.SECONDS);

		long initialDelay2 = 1;
		long delay2 = 1;
		// 从现在开始2秒钟之后，每隔2秒钟执行一次job2
		service.scheduleWithFixedDelay(new WorkTask("job2"),
				initialDelay2, delay2, TimeUnit.SECONDS);
	}
}

class WorkTask extends TimerTask {
	String name;

	public WorkTask(String name) {
		super();
		this.name = name;
	}

	@Override
	public void run() {
		String tName = Thread.currentThread().getName();
		System.out.println("Taks:" + name + " running at " + new Date()
				+ " Thread: " + tName);
//		try {
//			Thread.sleep(5000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
	}

}