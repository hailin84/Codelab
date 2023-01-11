package org.alive.test.testcase;

import java.math.BigDecimal;

import org.alive.test.core.TestSuite;

public class RunTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		TestSuite ts = new TestSuite();
		// ts.addTestCase(new ProxyTestCase("ProxyTestCase"));
		// ts.addTestCase(new DoubleTestCase("DoubleTestCase"));
		// ts.addTestCase(new CollectionTestCase("CollectionTestCase"));
		// ts.addTestCase(new TimerTestCase("TimerTestCase"));
		// ts.addTestCase(new CallBackTestCase("CallBackTestCase"));
		// ts.addTestCase(new EventTestCase("EventTestCase"));
		// ts.addTestCase(new WaitNotifyTestCase("WaitNotifyTestCase"));
		// ts.addTestCase(new NormalTestCase("NormalTestCase"));
		// ts.addTestCase(new NIOTestCase("NIOTestCase"));
		// ts.addTestCase(new SocketServerTestCase("SocketServerTestCase"));
		// System.out.println(0x46);
		// ts.addTestCase(new MultiThreadTestCase("MultiThreadTestCase"));
		// ts.addTestCase(new ThreadLocalTestCase("ThreadLocalTestCase"));
		// ts.addTestCase(new DateFormatTestCase("DateFormatTestCase"));

		// ts.addTestCase(new ShutDownTestCase("ShutDownTestCase"));
		// ts.addTestCase(new RandomTestCase("RandomTestCase"));
		// ts.addTestCase(new FastExcelTestCase("FastExcelTestCase"));
		// ts.addTestCase(new UITestCase("UITestCase"));
		ts.addTestCase(new ByteTransTestCase("ByteTransTestCase"));
		// ts.addTestCase(new BitOperationTestCase("BitOperationTestCase"));
		// ts.addTestCase(new
		// EncryptPlatformTestCase("EncryptPlatformTestCase"));
		// ts.addTestCase(new BCDTestCase("BCDTestCase"));
		// ts.addTestCase(new JdbcTestCase("JdbcTestCase"));
		// ts.addTestCase(new MD5TestCase("MD5TestCase"));
		// ts.addTestCase(new NotifyTestCase(NotifyTestCase.class.getName()));

		// ts.addTestCase(new FileEncodingTestCase(FileEncodingTestCase.class.getName()));
		// ts.addTestCase(new AlgorithmTestCase(AlgorithmTestCase.class.getName()));
		// ts.addTestCase(new ByteBufferTestCase(ByteBufferTestCase.class.getName()));
		// ts.addTestCase(new GenericsTestCase(GenericsTestCase.class.getName()));
		// ts.addTestCase(new JVMTestCase(JVMTestCase.class.getName()));
		// ts.addTestCase(new CircleByteBufferTestCase(CircleByteBufferTestCase.class.getName()));
		// ts.addTestCase(new CharsetDetectTestCase(CharsetDetectTestCase.class.getName()));
		// ts.addTestCase(new DateTimeTestCase(DateTimeTestCase.class.getName()));
		ts.run();

		// System.out.println(s);
		// new SocketMessageUI();
		// printSql();

	}

	public static void printSql() {
		BigDecimal d = new BigDecimal("17040715284410600003492");
		// long seq = Long.parseLong("17040715284410600003492");
		System.out.println(d);
		int magicNumber = 0xcafebabe;
		System.out.println(magicNumber); // -889275714
		System.out.println(Integer.toHexString(magicNumber));
	}
}
