package org.alive.learn.thread;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;


public class MultiThread {

	public static void main(String[] args) {
		ThreadMXBean mxBean = ManagementFactory.getThreadMXBean();
		ThreadInfo[] info = mxBean.dumpAllThreads(false, false);
		for (ThreadInfo threadInfo : info) {
			System.out.println(threadInfo.getThreadId() + " - " + threadInfo.getThreadName());
		}
	}

}
