package org.alive.tools.socketmessage;

import java.awt.Color;

/**
 * UI工具类
 * 
 * @author hailin84
 * 
 */
public final class UIUtil {
	private UIUtil() {
	}

	/**
	 * 从16进制表示的字符串生成Color
	 * 
	 * @param str
	 *            颜色，如#3e62a6
	 * @return
	 */
	public static Color parseColor(String str) {
		int i = Integer.parseInt(str.substring(1), 16);
		return new Color(i);
	}

	/**
	 * 得到Color对象的16进制字符串表示
	 * @param c
	 * @return
	 */
	public static String colorToString(Color c) {
		String r = Integer.toHexString(c.getRed());
		r = r.length() < 2 ? ('0' + r) : r;
		String b = Integer.toHexString(c.getBlue());
		b = b.length() < 2 ? ('0' + b) : b;
		String g = Integer.toHexString(c.getGreen());
		g = g.length() < 2 ? ('0' + g) : g;
		return '#' + r + b + g;
	}
}
