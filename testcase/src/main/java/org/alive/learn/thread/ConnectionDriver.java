package org.alive.learn.thread;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.util.concurrent.TimeUnit;

public class ConnectionDriver {
	static class ConnectionHandler implements InvocationHandler {

		@Override
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			if ("commit".equals(method.getName())) {
				TimeUnit.MILLISECONDS.sleep(100);
			}
			return null;
		}
	}

	// 构造Connection动态代理实现，仅仅在commit方法的时候，休眠100ms
	public static final Connection createConnection() {
		return (Connection) Proxy.newProxyInstance(ConnectionDriver.class.getClassLoader(), new Class[] { Connection.class },
				new ConnectionHandler());
	}
}
