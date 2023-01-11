package org.alive.test.testcase;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.alive.test.core.TestCase;

public class DateFormatTestCase extends TestCase {

	public DateFormatTestCase() {
		super();
		// TODO Auto-generated constructor stub
	}

	public DateFormatTestCase(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	public void nottestThreadSafe() throws Exception {
		for(int i = 0; i < 5; i++){
            new DateFormatThread().start();
        }
	}
	
	public void testFormat() throws Exception {
		String transForbiddenTimeArea = "01:30-09:00";
		System.out.println(parse(transForbiddenTimeArea));
	}
	
	private String parse(String transForbiddenTimeArea) {
		if (transForbiddenTimeArea == null
				|| transForbiddenTimeArea.length() == 0) {
			return "1";
		}

		String[] parts = transForbiddenTimeArea.split("-");
		if (parts.length != 2) {
			// 时间格式不正确
			return "1";
		}

		DateFormat sdf = new SimpleDateFormat("HH:mm");
		try {
			sdf.parse(parts[0]);
			sdf.parse(parts[1]);
		} catch (Exception e1) {
			// 时间格式不正确
			return "1";
		}
		 
		
		sdf = new SimpleDateFormat("yyyy-MM-dd");
		// sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Timestamp now = new Timestamp(System.currentTimeMillis());
		String today = sdf.format(now);
		Timestamp begin = Timestamp.valueOf(today + " " + parts[0] + ":00");
		Timestamp end = Timestamp.valueOf(today + " " + parts[1] + ":59");

		sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		System.out.println(sdf.format(now));
		System.out.println(sdf.format(new Date()));
		if (now.after(begin) && now.before(end)) {
			// 系统时间在禁止交易时间段内，返回1
			return "1";
		}

		return "0";
	}
}

class DateFormatThread extends Thread {
	/** 多线程同时访问会有问题，因为不是线程安全的 */
	private static final SimpleDateFormat sdf = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	private static boolean runing = true;
	@Override
	public void run() {
		while (runing) {
			try {
				this.join(2000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			try {
				System.out.println(this.getName() + ":"
						+ sdf.parse("2013-05-24 06:02:20"));
			} catch (Exception e) {
				e.printStackTrace();
				runing = false;
			}
		}
	}
}