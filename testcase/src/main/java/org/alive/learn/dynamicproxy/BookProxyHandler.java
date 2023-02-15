package org.alive.learn.dynamicproxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Invocation Handler for book proxy.
 * 
 * @author hailin84
 *
 */
public class BookProxyHandler implements InvocationHandler {

	/** the real object to be proxied */
	private Object target;

	
	public BookProxyHandler(Object target) {
		super();
		this.target = target;
	}

	/**
	 * 绑定委托对象并返回一个代理类
	 * 
	 * @param target
	 * @return
	 */
//	public Object bind(Object target) {
//		this.target = target;
//		// 取得代理对象
//		return Proxy.newProxyInstance(target.getClass().getClassLoader(),
//				target.getClass().getInterfaces(), this); // 要绑定接口(这是一个缺陷，cglib弥补了这一缺陷)
//	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		Object result = null;
		System.out.println("start...");
		System.out.println(proxy.getClass().getName());
		// 执行方法
		result = method.invoke(target, args);
		System.out.println("end.");
		return result;
	}
}
