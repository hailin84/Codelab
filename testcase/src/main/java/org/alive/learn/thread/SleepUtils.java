package org.alive.learn.thread;

import java.util.concurrent.TimeUnit;

public class SleepUtils {
	public static void second(int timeOut) {
		try {
			TimeUnit.SECONDS.sleep(timeOut);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
