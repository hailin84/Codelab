package org.alive.test.testcase;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.alive.dao.BaseDAO;
import org.alive.test.core.TestCase;

public class JdbcTestCase extends TestCase {
	public JdbcTestCase(String name) {
		super(name);

	}
	
	private BaseDAO baseDao = new BaseDAO();
	
	public void testInsertData() {
		String[] custCodes = new String[] {
				"0000000094",
				"0900000094", 
				"0000000108",
				"0000000112",
				"0000000126", 
				"0000000156",
				"0000000161",
				"0900000161", 
				"0000000164",
				"0000000185",
				"0000000190",
				"0900000190",  
				"0900000211",  
				"0000000216",  
				"0900000216", 
				"0000000222",
				"0000000243",
				"0000000246",
				"0900000257",
				"0000000263",
				"0000002065",
				// "0100140422", // 平安信托黄金宝
				"0000000263",
				"0000002012"
		};
//		for (String code : custCodes) {
//			try {
//				dealOne(code);
//			} catch (SQLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
		
		try {
			dealOne("0900000257");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	private void dealOne(String custCode) throws SQLException {
		char mark = custCode.charAt(1);
		String unitCode = custCode.substring(6);
		String unitSeatCode = null;
		if ('9' == mark) {
			unitSeatCode = unitCode + "21";
		} else {
			unitSeatCode = unitCode + "11";
		}
		
		String sql = "SELECT A.UNITCODE, A.ACCOUNT, A.ACCTNAME, A.BRANCHNO, " +
				"B.SEATTYPE FROM GEB_CODE A, GEB_SEATINFO B WHERE A.UNITSEATCODE = B.UNITSEATCODE AND A.UNITSEATCODE = ?";
		Map<String, String> data = baseDao.queryForMap(sql, unitSeatCode);
		
		String insert = "insert into geb_customer_info (CUSTCODE, CUSTNAME, CUSTACCT, " +
				"CUSTNODE, UNITCODE, UNITNAME, UNITACCT, FEESIGN, OPENNODE, STATUS, UNITSEATCODE, CUSTTYPE, CUSTSHORTNAME, OPENDATE, UPDATEDATE)" +
		"values (?, ?, ?, ?, ?, ?, ?, '0', ?, '0', ?, '', '佣金划转', '', '')";
		
		List<String> params = new ArrayList<String>();
		params.add(custCode);
		
		params.add("佣金划转@" + data.get("ACCTNAME"));
		params.add(data.get("ACCOUNT"));
		params.add(data.get("BRANCHNO"));
		
		params.add(unitCode);
		params.add(data.get("ACCTNAME"));
		params.add(data.get("ACCOUNT"));
		params.add(data.get("BRANCHNO"));
		
		params.add(unitSeatCode);
		
		baseDao.executeUpdate(insert, params);
		
	}
	
}
