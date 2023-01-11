package org.alive.test.testcase;

import org.alive.test.core.TestCase;

public class BCDTestCase extends TestCase {

	public BCDTestCase(String name) {
		super(name);

	}

	public void testBasic() {
		String s = "0"; // 字符串0
		byte b = (byte) '0'; // 字符'0'对应的ASCII码为48，转换为BYTE即为其ASCII码
		System.out.println(b);  // b = 48
		
		byte[] bcd = new byte[2];
		bcd[0] = (byte) ((b & 0xf0) >>> 4);  // 数字3，需要转换成字符3，数值即为51 
		bcd[1] = (byte) (b & 0x0f); // 数字0
		
		
		
		System.out.println(bcd[0] + " | " + Byte.toString(bcd[0]).getBytes()[0]);
		System.out.println(bcd[1]);
		System.out.println(new String(bcd));
		
		byte[] b2 = "30".getBytes();
		System.out.println(b2[0] + "=" + (char) b2[0]);
		System.out.println(b2[1]);
	}
	
	public void testBcd() {
		byte[] b = str2Bcd("30303030");
		System.out.println(b.length);
		for (byte c : b) {
			System.out.println("byte = " +  c);
		}
		System.out.println(bcd2Str(b));
		System.out.println(new String(b));
		
		byte[] b2 = "0106".getBytes();
		System.out.println("b2.length=" + b2.length);
		System.out.println(new String(bcd2Str(b2)));
		
		byte[] b3 = str2Bcd("3030303030300000000000000000000000000000323031363037303630303031D4ED6C84662756937F31FF5C8F7175944F4EE5A3C54757AD2AB9C7352E065439D30973852EF33178FB79DE2A4EA81FD1D988B0EEB652292C822385B80B721622");
		System.out.println(new String(b3));
		
				
	}
	
	public void testSgeMessage() {
		String msg = "3030303030300000000000000000000000000000323031363039303830303031E206F98ECDA319A2EC48589C4DB9AD47BBD7F85891A0EA9FA264879B192F3D05F1FDA3F87695E63D4F7410443AD02CDCC4B09B15C754617C2ADC206D6CA5326E";
		
		System.out.println(msg.length());
		int start = 0;
		int partLen = 8;
		String tmp = msg.substring(start, start + partLen);
		System.out.println(tmp + "=" + new String(str2Bcd(tmp)));
		
		start += partLen;
		partLen = 32;
		tmp = msg.substring(start, start + partLen);
		System.out.println(tmp + "=" + new String(str2Bcd(tmp)));
		
		start += partLen;
		partLen = 24;
		tmp = msg.substring(start, start + partLen);
		start += partLen;
		System.out.println(tmp + "=" + new String(str2Bcd(tmp)));
		
		partLen = 32;
		tmp = msg.substring(start, start + partLen);
		start += partLen;
		System.out.println(tmp + "=" + new String(str2Bcd(tmp)));
		
		partLen = 32;
		tmp = msg.substring(start, start + partLen);
		start += partLen;
		System.out.println(tmp + "=" + new String(str2Bcd(tmp)));
		
		partLen = 32;
		tmp = msg.substring(start, start + partLen);
		start += partLen;
		System.out.println(tmp + "=" + new String(str2Bcd(tmp)));
		
		partLen = 32;
		tmp = msg.substring(start, start + partLen);
		start += partLen;
		System.out.println(tmp + "=" + new String(str2Bcd(tmp)));
		
		System.out.println("0".getBytes()[0] == (byte) '0');
		System.out.println((byte)'0');
		System.out.println("0xf0=" + 0xf0 + Integer.toBinaryString(0xf0));
		System.out.println("0x0f=" + 0x0f + Integer.toBinaryString(0x0f));
	}
	
	public void testNewBCD() {
		String data = "0106ABC";
		String bcdStr = orignalToBCD(data);
		
		System.out.println(bcdStr);
		
		System.out.println(new String(str2Bcd(bcdStr)));
		System.out.println(bcdToOriginal(bcdStr));
		
		String backup = "00" + "\0\0\0\0\0\0\0\0" + "\0\0\0\0\0\0";
		System.out.println(orignalToBCD(backup));
	}

	/**
	 * @功能: BCD码转为10进制串(阿拉伯数据)
	 * @参数: BCD码
	 * @结果: 10进制串
	 */
	public String bcd2Str(byte[] bytes) {
		StringBuffer temp = new StringBuffer(bytes.length * 2);
		for (int i = 0; i < bytes.length; i++) {
			temp.append((byte) ((bytes[i] & 0xf0) >>> 4));
			temp.append((byte) (bytes[i] & 0x0f));
		}
		return temp.toString().substring(0, 1).equalsIgnoreCase("0") ? temp.toString().substring(1) : temp.toString();
	}

	/**
	 * @功能: 10进制串转为BCD码
	 * @参数: 10进制串
	 * @结果: BCD码
	 */
	public byte[] str2Bcd(String asc) {
		int len = asc.length();
		int mod = len % 2;
		if (mod != 0) {
			asc = "0" + asc;
			len = asc.length();
		}
		byte abt[] = new byte[len];
		if (len >= 2) {
			len = len / 2;
		}
		byte bbt[] = new byte[len];
		abt = asc.getBytes();
		int j, k;
		for (int p = 0; p < asc.length() / 2; p++) {
			if ((abt[2 * p] >= '0') && (abt[2 * p] <= '9')) {
				j = abt[2 * p] - '0';
			} else if ((abt[2 * p] >= 'a') && (abt[2 * p] <= 'z')) {
				j = abt[2 * p] - 'a' + 0x0a;
			} else {
				j = abt[2 * p] - 'A' + 0x0a;
			}
			if ((abt[2 * p + 1] >= '0') && (abt[2 * p + 1] <= '9')) {
				k = abt[2 * p + 1] - '0';
			} else if ((abt[2 * p + 1] >= 'a') && (abt[2 * p + 1] <= 'z')) {
				k = abt[2 * p + 1] - 'a' + 0x0a;
			} else {
				k = abt[2 * p + 1] - 'A' + 0x0a;
			}
			int a = (j << 4) + k;
			byte b = (byte) a;
			bbt[p] = b;
		}
		return bbt;
	}
	
	/**
	 * 将字符串进行BCD扩展，原来字符串中每一个字节，扩展为两个字节；参数字符串中不包括多字节字符
	 * 
	 * @param data
	 *            BCD扩展后的字符串
	 * @return
	 */
	public String orignalToBCD(String data) {
		byte[] b = data.getBytes();
		byte[] temp = new byte[b.length * 2];
		for (int i = 0; i < b.length; i++) {
			temp[i * 2] = (byte) ((b[i] & 0xf0) >>> 4);
			temp[i * 2] += (byte) '0';
			temp[i * 2 + 1] = (byte) ((b[i] & 0x0f));
			temp[i * 2 + 1] += (byte) '0';
		}
		return temp[0] == (byte) '0' ? new String(temp, 0, temp.length - 1) : new String(temp);
	}

	/**
	 * 将BCD转换扩展后的字符串转换为原来的字符串，长度变为原来的一半；注意，要求都为单字节字符，不然会有不可见字符；
	 * 
	 * @param data
	 * @return
	 */
	public String bcdToOriginal(String data) {
		int len = data.length();
		int mod = len % 2;
		if (mod != 0) {
			data = "0" + data;
			len = data.length();
		}
		byte abt[] = new byte[len];
		if (len >= 2) {
			len = len / 2;
		}
		byte bbt[] = new byte[len];
		abt = data.getBytes();
		int j, k;
		for (int p = 0; p < data.length() / 2; p++) {
			if ((abt[2 * p] >= '0') && (abt[2 * p] <= '9')) {
				j = abt[2 * p] - '0';
			} else if ((abt[2 * p] >= 'a') && (abt[2 * p] <= 'z')) {
				j = abt[2 * p] - 'a' + 0x0a;
			} else {
				j = abt[2 * p] - 'A' + 0x0a;
			}
			if ((abt[2 * p + 1] >= '0') && (abt[2 * p + 1] <= '9')) {
				k = abt[2 * p + 1] - '0';
			} else if ((abt[2 * p + 1] >= 'a') && (abt[2 * p + 1] <= 'z')) {
				k = abt[2 * p + 1] - 'a' + 0x0a;
			} else {
				k = abt[2 * p + 1] - 'A' + 0x0a;
			}
			int a = (j << 4) + k;
			byte b = (byte) a;
			bbt[p] = b;
		}
		//return bbt;
		return new String(bbt);
	}
}
