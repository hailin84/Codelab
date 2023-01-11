package org.alive.util;

import java.io.FileInputStream;
import java.nio.charset.Charset;

import org.mozilla.universalchardet.UniversalDetector;

/**
 * 文件编码检测工具类
 * <p>
 * 使用cpdetector改为使用来自mozilla的rg.mozilla.universalchardet
 * 
 * @author hailin84
 * @since 2017.05.11
 */
public class EncodingDetectUtil {
	public static String DEFAULT_ENCODING = "GB18030";

	/**
	 * <pre>
	 * private static CodepageDetectorProxy detector;
	 * 
	 * static {
	 * 	detector = CodepageDetectorProxy.getInstance();
	 * 	detector.add(new ParsingDetector(false));
	 * 	detector.add(JChardetFacade.getInstance());// 需要第三方JAR包:antlr.jar、chardet.jar.
	 * 	detector.add(ASCIIDetector.getInstance());
	 * 	detector.add(UnicodeDetector.getInstance());
	 * }
	 * </pre>
	 */
	public static Charset detect(String file) {
		Charset c = null;
		byte[] buf = new byte[4096];
		try {
			FileInputStream fis = new FileInputStream(file);
			UniversalDetector detector = new UniversalDetector(null);
			int nread;
			while ((nread = fis.read(buf)) > 0 && !detector.isDone())
				detector.handleData(buf, 0, nread);

			fis.close();

			detector.dataEnd();
			String encoding = detector.getDetectedCharset();

			if (encoding == null) {
				encoding = DEFAULT_ENCODING;
			}

			return Charset.forName(encoding);
			// c = detector.detectCodepage(f.toURI().toURL());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return c;
	}

	public static boolean isChineseEncoding(String file) {
		Charset c = detect(file);

		if (c == null) {
			return false;
		}
		String name = c.name();
		return "GB2312|GBK|GB18030".contains(name);
	}

	public static boolean isUTF8Encoding(String file) {
		Charset c = detect(file);
		if (c == null) {
			return false;
		}
		String name = c.name();
		return "UTF-8".equals(name);
	}

	public static boolean isTheSameEncoding(String c1, String c2) {
		if ("GB2312|GBK|GB18030".contains(c1)) {
			c1 = "GB18030";
		}
		if ("GB2312|GBK|GB18030".contains(c2)) {
			c2 = "GB18030";
		}

		return c1.equals(c2);
	}

}
