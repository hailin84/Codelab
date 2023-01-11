package org.alive.test.testcase;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.alive.test.core.TestCase;

/**
 * <p>
 * 测试DataTime相关，使用java.time包
 * 
 * @author hailin84
 * @date 2017.09.12
 *
 */
public class DateTimeTestCase extends TestCase {

	/** 日期格式 */
	private DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyyMMdd");
	
	public DateTimeTestCase() {
		super();
	}

	public DateTimeTestCase(String name) {
		super(name);
	}

	public void testWorkDate() {
		// LocalDate date = LocalDate.now();
		// System.out.println(date);
		// 得到某个日间段内的所有工作日，法定节假日的情况不考虑
		LocalDate start = LocalDate.of(2017, 5, 1);
		LocalDate end = LocalDate.of(2017, 9, 15);
		while (start.isBefore(end)) {
			DayOfWeek dwe = start.getDayOfWeek();
			if (DayOfWeek.SATURDAY != dwe && DayOfWeek.SUNDAY != dwe) {
				System.out.println(start.format(fmt));
			} else {
				// System.out.println(start + "周末");
			}
			start = start.plusDays(1);
		}
	}
}
