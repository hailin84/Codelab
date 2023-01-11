package org.alive.util;

import java.util.Random;

public class RandomUtil {
	
	/**
	 * 生成指定长度的随机字符串
	 * 
	 * @param length
	 * @return
	 */
	public static String randomString(int length) {
		char[] cResult = new char[length];
		int[] flag = { 0, 0, 0 }; // A-Z, a-z, 0-9
		int i = 0;
		while (flag[0] == 0 || flag[1] == 0 || flag[2] == 0 || i < length) {
			i = i % length;
			int f = (int) (Math.random() * 3 % 3);
			if (f == 0)
				cResult[i] = (char) ('A' + Math.random() * 26);
			else if (f == 1) {
				cResult[i] = (char) ('a' + Math.random() * 26);
			} else {
				cResult[i] = (char) ('0' + Math.random() * 10);
			}
			flag[f] = 1;
			i++;
		}
		return new String(cResult);
	}
	
	public static int randomInt() {
		Random r = new Random(System.currentTimeMillis());
		return r.nextInt();
	}

	public static int randomInt(int n) {
		Random r = new Random(System.currentTimeMillis());
		return r.nextInt(n);
	}

	/**
	 * 生成介于min，max之间的随机整数
	 * 
	 * @param min
	 * @param max
	 * @return
	 */
	public static int randomInt(int min, int max) {
		Random random = new Random(System.currentTimeMillis());
		// int x = (int) (Math.random() * max + min);
		int s = random.nextInt(max) % (max - min + 1) + min;
		return s;
	}
}
