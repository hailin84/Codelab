package org.alive.test.testcase;

import java.lang.reflect.Proxy;

import org.alive.learn.dynamicproxy.BookProxyHandler;
import org.alive.learn.dynamicproxy.IBook;
import org.alive.learn.dynamicproxy.ProgrammingBook;
import org.alive.test.core.TestCase;

public class ProxyTestCase extends TestCase {

	public ProxyTestCase(String name) {
		super(name);
	}

	public void testDynamicProxy() {
		BookProxyHandler handler = new BookProxyHandler(new ProgrammingBook());
		
		// get proxy instance
		IBook book = (IBook) Proxy.newProxyInstance(
				IBook.class.getClassLoader(), new Class[] { IBook.class },
				handler);
		// every method call is forwarded to InvocationHandler.invoke
		System.out.println(book.showCatalog());
		System.out.println(book.showLanguage());
	}
}
