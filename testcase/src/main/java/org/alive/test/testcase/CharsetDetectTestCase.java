package org.alive.test.testcase;

import java.io.IOException;
import java.io.InputStream;

import org.alive.test.core.TestCase;
import org.mozilla.universalchardet.UniversalDetector;

/**
 * juniversalchardet
 * 
 * @author hailin84
 *
 */
public class CharsetDetectTestCase extends TestCase {
	static String DEFAULT_ENCODING = "GBK";

	private String[] files = new String[] { "/GBK.txt", "/UTF-8.txt", "/UTF-8BOM.txt", "/UTF-8 Full English.txt" };

	public CharsetDetectTestCase(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	public void test1() {
		for (String f : files) {
			try {
				System.out.println(f + " \t\t" + detectEncoding(f));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static String detectEncoding(String file) throws IOException {
		byte[] buf = new byte[4096];
		InputStream fis = CharsetDetectTestCase.class.getResourceAsStream(file);
		// FileInputStream fis = new FileInputStream(file);
		UniversalDetector detector = new UniversalDetector(null);
		int nread;
		while ((nread = fis.read(buf)) > 0 && !detector.isDone())
			detector.handleData(buf, 0, nread);

		fis.close();

		detector.dataEnd();
		String encoding = detector.getDetectedCharset();

		if (encoding == null)
			encoding = DEFAULT_ENCODING;
		return encoding;
	}
}
