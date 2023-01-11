package org.alive.learn.cache;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * <p>
 * 通过摘要算法生成消息摘要，再根据摘要计算Hash，好处是Hash值比较分散；此方法性能略差；
 * <p>
 * 默认采用MD5算法计算摘要，可以通过参数传递摘要算法；
 * 
 * @author hailin84
 * @date 2017.08.16
 */
public class MessageDigestHash implements IHash<Integer> {
	private MessageDigest md = null;

	private String algorithm = "MD5";

	public MessageDigestHash(String alg) {
		if (alg != null && alg.length() > 0) {
			algorithm = alg;
		}
		try {
			md = MessageDigest.getInstance(algorithm);
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalStateException(algorithm + " algorythm not supportted");
		}
	}

	@Override
	public Integer hash(Object key) {
		md.reset();
		md.update(key.toString().getBytes());
		byte[] bKey = md.digest();
		Integer res = ((bKey[3] & 0xFF) << 24) | ((bKey[2] & 0xFF) << 16) | ((bKey[1] & 0xFF) << 8) | (bKey[0] & 0xFF);
		return res & 0xffffffff;
	}
}
