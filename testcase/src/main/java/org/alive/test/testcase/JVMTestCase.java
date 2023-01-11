package org.alive.test.testcase;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.alive.test.core.TestCase;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

/**
 * 测试JVM内存OOM
 * @author hailin84
 */
public class JVMTestCase extends TestCase {

	public JVMTestCase(String name) {
		super(name);
	}
	
	/**
	 * 测试Java方法区OOM异常
	 * <p>JDK1.7: VM args: -XX:PermSize=10M -XX:MaxPermSize=10M
	 * <p>如果是JDK1.8及以上，因为已经移出了PermGen，用Metaspace代替，所以参数是：-XX:MetaspaceSize=10M -XX:MaxMetaspaceSize=10M
	 */
	public void mtestJavaMethodAreaOOM() {
		while (true) {
			Enhancer e = new Enhancer();
			e.setSuperclass(OOMObject.class);
			e.setUseCache(false);
			e.setCallback(new MethodInterceptor() {

				@Override
				public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
					// TODO Auto-generated method stub
					return proxy.invokeSuper(obj, args);
				}
			});
			e.create();
			sleep();
		}
	}
	
	static class OOMObject {
	}
	
	/**
	 * -XX:MetaspaceSize=5M -XX:MaxMetaspaceSize=5M
	 * JDK1.8未能抛出异常
	 */
	public void mtestRuntimeConstantPoolOOM() {
		List<String> l = new ArrayList<>();
		int i = 0;
		while (true) {
			l.add(String.valueOf(i++).intern());
			// sleep();
		}
	}
	
	/**
	 * -Xss2M
	 */
	public void testJavaVMStackOOM() {
		while (true) {
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					while (true) {
						sleep();
					}
					
				}
			}).start();
		}
	}
	
	private static void sleep() {
		try {
			TimeUnit.NANOSECONDS.sleep(1);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
