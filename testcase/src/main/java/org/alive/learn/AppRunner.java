package org.alive.learn;

import java.lang.reflect.Method;

public class AppRunner {

	public static void main(String[] args) throws Exception {
		String classToRun = "org.alive.learn.thread.TestMain";
		//classToRun = "org.alive.learn.cache.MemcachedOperation";
		//classToRun = "org.alive.learn.cache.RedisOperation";
		classToRun = "org.alive.learn.thread.SimpleHttpServer";
		classToRun = "org.alive.learn.netty.NettyServer";
		// classToRun = "org.alive.learn.thread.InterruptTest";
		classToRun = "org.alive.learn.thread.FalseSharing";
		classToRun = "org.alive.learn.guava.GuavaTester";
		Class<?> c = Class.forName(classToRun);
		Method m = c.getMethod("main", String[].class);
		m.invoke(null, new Object[] { args });
	}
}
