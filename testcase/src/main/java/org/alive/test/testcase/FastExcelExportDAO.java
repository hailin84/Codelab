package org.alive.test.testcase;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

import org.alive.dao.BaseDAO;
import org.alive.dao.DsUtil;

import edu.npu.fastexcel.ExcelException;
import edu.npu.fastexcel.FastExcel;
import edu.npu.fastexcel.Sheet;
import edu.npu.fastexcel.Workbook;

public class FastExcelExportDAO extends BaseDAO {
	public void exportExcel() {
		Connection conn = DsUtil.getInstance().getConnection();

		String sql = "SELECT *\n"
				+ "  FROM (SELECT TT.*, ROWNUM AS ROWNO\n"
				+ "          FROM (SELECT A.ACCOUNTID,\n"
				+ "                       A.ORDERID,\n"
				+ "                       A.BUYID,\n"
				+ "                       A.ATTACHCARDID,\n"
				+ "                       A.PRODUCTID,\n"
				+ "                       A.PRODUCTNAME,\n"
				+ "                       A.SOURCEAMOUNT,\n"
				+ "                       A.CURRENCYID,\n"
				+ "                       A.CHFLAG,\n"
				+ "                       A.POUNDAGE,\n"
				+ "                       A.BUILDDATE,\n"
				+ "                       A.BUILDTIME,\n"
				+ "                       A.BUILDBANKID,\n"
				+ "                       A.BUILDCHANNELID,\n"
				+ "                       A.OPERSTATE,\n"
				+ "                       A.OPERRESULT,\n"
				+ "                       A.BANKNAME,\n"
				+ "                       A.CUSTOMERNAME,\n"
				+ "                       A.CUSTOMERMANAGERID,\n"
				+ "                       A.BUILDUSERID,\n"
				+ "                       A.CUSTOMERID,\n"
				+ "                       A.ACCBANKID,\n"
				+ "                       A.HOLDID,\n"
				+ "                       A.SEQNO,\n"
				+ "                       B.DIVISIONNAME\n"
				+ "                  FROM LC08_BUY_V A, V_CPXS_DIVISIONOFACC B\n"
				+ "                 WHERE (A.PRODUCTID = ?)\n"
				+ "                   AND A.BUILDDATE >= ?\n"
				+ "                   AND A.BUILDDATE <= ?\n"
				+ "                   AND A.ATTACHCARDID = B.ATTACHCARDID(+)\n"
				+ "                 ORDER BY PRODUCTID,\n"
				+ "                          BUILDDATE,\n"
				+ "                          BUILDTIME,\n"
				+ "                          ACCOUNTID,\n"
				+ "                          ORDERID,\n"
				+ "                          BUYID DESC) TT\n"
				+ "         WHERE ROWNUM <= ?) T\n" + " WHERE T.ROWNO >= ?";

		PreparedStatement ps = null;
		ResultSet rs = null;
		Workbook workBook = null;

		try {
			workBook = FastExcel.createWriteableWorkbook(new File("report.xls"));
			// workBook.setSS
			workBook.open();
			Sheet sheet = workBook.addSheet("产品认购明细报表");
			int row = 0;
			String[] title = new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9",
					"10", "11", "12", "13", "14", "15", "16", "17", "18", "19"};
			sheet.setRow(row++, title);
			System.out.println(Arrays.toString(title));
			ps = conn.prepareStatement(sql);
			int index = 0;
			ps.setString(++index, "3100011003");
			ps.setString(++index, "20040110");
			ps.setString(++index, "20180206");
			ps.setInt(++index, 10000);
			ps.setInt(++index, 1);
			rs = ps.executeQuery();
			while (rs.next()) {
				String channelType = rs.getString("BUILDCHANNELID");
				String stateType = rs.getString("OPERSTATE");

				if (channelType.equals("01")) {
					channelType = "柜台";
				} else {
					channelType = "网银";
				}

				if (stateType.equals("0")) {
					stateType = "待处理";
				}

				else if (stateType.equals("1")) {
					stateType = "已处理";
				}

				else if (stateType.equals("2")) {
					stateType = "撤单";
				}

				else if (stateType.equals("9")) {
					stateType = "处理失败";
				} else {
					stateType = "未知";
				}

				String[] rowContent = new String[18];

				rowContent[0] = Integer.toString(row);
				rowContent[1] = rs.getString("ACCOUNTID") + '+'
						+ rs.getString("ORDERID") + '+' + rs.getString("BUYID");
				rowContent[2] = rs.getString("CUSTOMERNAME");
				rowContent[3] = rs.getString("ATTACHCARDID");
				rowContent[4] = stateType; // rs.getString("OPERSTATE");
				rowContent[5] = rs.getString("PRODUCTID");
				rowContent[6] = rs.getString("PRODUCTNAME");
				rowContent[7] = rs.getBigDecimal("SOURCEAMOUNT").toString();
				rowContent[8] = rs.getString("CURRENCYID");
				rowContent[9] = rs.getString("CHFLAG");
				rowContent[10] = rs.getString("BUILDDATE");
				rowContent[11] = rs.getString("BUILDTIME");
				rowContent[12] = rs.getString("BUILDBANKID");
				rowContent[13] = rs.getString("BANKNAME"); // 乱码
				rowContent[14] = rs.getString("BUILDUSERID");
				rowContent[15] = rs.getString("CUSTOMERMANAGERID");
				rowContent[16] = channelType; // rs.getString("BUILDCHANNELID");
				rowContent[17] = rs.getString("DIVISIONNAME");

//				for (int i = 0; i < rowContent.length; i++) {
//					rowContent[i] = rowContent[i] != null ? rowContent[i] : "";
//				}
				System.out.println(Arrays.toString(rowContent));
				// rowContent[6] = rs.getString("BANKNAME");
				// rowContent[13] = rs.getString("PRODUCTNAME"); // OK
				// rowContent[13] = "赢合力-天天利"; // OK
				// rowContent[13] = "赢合力-天天"; // 乱码
				// rowContent[13] = "ABCDEFG"; // 乱码
				// rowContent[13] = "平安金-定存通"; // 乱码
				// rowContent[13] = "赢合力-天天合"; // 乱码
				rowContent[13] = "";
				sheet.setRow(row++, rowContent);
				// sheet.setRow(row++, rowContent);
			}
			
			// 修复fastExcel导出时少一行的bug，插入一个非空行
			String[] fixRow = new String[18];
			fixRow[0] = "fixRow";
			sheet.setRow(row, fixRow);
			// conn.rollback();
		} catch (Exception e) {
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} finally {
			this.close(new Statement[] { ps }, conn);
			if (workBook != null) {
				try {
					workBook.close();
				} catch (ExcelException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
