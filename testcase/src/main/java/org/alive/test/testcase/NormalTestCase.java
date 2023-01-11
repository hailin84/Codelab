package org.alive.test.testcase;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.alive.test.core.TestCase;

public class NormalTestCase extends TestCase {

	public NormalTestCase(String name) {
		super(name);
	}
	
	public void testChar() {
		char b = (char) 0x80;
		System.out.println(b);
		System.out.println(0x80);
		int c = 0144; // 8进制
		System.out.println(c);
		
		String result = "SHAU|QD01|16051809002310600000000|106|20160518|" + b;
		System.out.println(result);
		
		
		int index = result.lastIndexOf('|');
		System.out.println(result.substring(0, index + 1));
		
		System.out.println(Double.parseDouble("9999999999900"));
	}

	public void testDateFormat() {
		SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
		System.out.println(fmt.format(new Date()));
	}

	public void testStringBytes() {
		byte[] b = new byte[200];
		b[0] = '0';
		b[1] = '1';
		System.out.println(new String(b, 0, 2));
		// 不加长度限制，后面就是方块一样的乱码
		System.out.println(new String(b));
	}

	public void testLoop() {
		for (int unit = 3; unit <= 3; unit++) {
			for (int level = 1; level <= 2; level++) {
				System.out.println("7-" + unit + "-" + level + "A");
				System.out.println("7-" + unit + "-" + level + "B");
				System.out.println("7-" + unit + "-" + level + "C");
				System.out.println("7-" + unit + "-" + level + "D");
			}
		}
	}

	public void testSpecialChar() {
		StringBuffer buf = new StringBuffer();
		buf.append("┏━━━━━━━━━━━━━━━━━━━━┳━━━━━━━━━┳━━━━━━━┳━━━━━━━━━━━━┳━━━━━━━━━━━━┓");
		buf.append("┣━━━━━━━━━━━━━━━━━━━━╋━━━━━━━━━╋━━━━━━━╋━━╋━━━━━━━━━╋━━╋━━━━━━━━━┫");
		buf.append("┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┻━━━━━━━┻━━┻━━━━━━━━━┻━━┻━━━━━━━━━┛");

		Set<Character> data = new HashSet<Character>();
		for (int i = 0, size = buf.length(); i < size; i++) {
			char c = buf.charAt(i);
			data.add(c);
		}

		System.out.println(data);
	}

	public void testMessageFormat() {
		String logMsg = MessageFormat
				.format("Cann''t stop Message Dispatch Thread now, readQueue={0}, decryptedQueue={1}, toEncryptQueue={2}, runnintTaskCount={3} --- wait for {4} seconds.",
						new Object[] { 0, 1, 3, 4, 20 });
		System.out.println(logMsg);
		System.out.println(MessageFormat.format("交易[{0}, {1}]对账成功，但对账结果数组为空", new Object[] { "aaaa", "bbbb" }));
	}
	
	public void testStringSplit() {
		String s = "SHAU|CX01|16042115244910600000427|106|20160420|";
		System.out.println(s + " split --> " + s.split("\\|").length);
		s = "SHAU|CX01|16042115244910600000427|106|20160420|XXX";
		System.out.println(s + " split --> " + s.split("\\|").length);
	}
}
