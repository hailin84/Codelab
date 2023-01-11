package org.alive.test.testcase;

import java.io.File;

import org.alive.test.core.TestCase;

import edu.npu.fastexcel.FastExcel;
import edu.npu.fastexcel.Sheet;
import edu.npu.fastexcel.Workbook;

public class FastExcelTestCase extends TestCase {

	private FastExcelExportDAO dao = new FastExcelExportDAO();

	public FastExcelTestCase() {
		super();
	}

	public FastExcelTestCase(String name) {
		super(name);
	}

	public void test1() throws Exception {
		dao.exportExcel();
	}

	public void test2() throws Exception {
		Workbook workBook = FastExcel.createWriteableWorkbook(new File(
				"data.xls"));

		workBook.open();

		Sheet sheet = workBook.addSheet("职员明细报表");
		int row = 0;
		sheet.setRow(row++, new String[] { "姓名", "身高", "职业", "手机号", "E", "F", "G", "H" });

//		sheet.setRow(row++, new String[] { "徐一", "183", "攻城狮", "13590343342", "汉字E", "汉字F", "汉字G" });
//		sheet.setRow(row++, new String[] { "徐二", "183", "攻城狮", "13590343342" });
//		sheet.setRow(row++, new String[] { "徐三", "183", "攻城狮", "13590343342" });
		do {
			sheet.setRow(row++, new String[] { "徐四", "183", "攻城狮",
					"13590343342", "汉字E", "汉字F", "汉字G"  });
		} while (row < 350);
		// 修复fastExcel导出时少一行的bug，插入一个非空行
		String[] fixRow = new String[7];
		fixRow[0] = "fixRow";
		sheet.setRow(row++, fixRow);

		workBook.close();
		System.out.println("Done..");
	}
}
