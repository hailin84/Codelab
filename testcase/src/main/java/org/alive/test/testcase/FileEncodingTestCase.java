package org.alive.test.testcase;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;

import org.alive.test.core.TestCase;

//import info.monitorenter.cpdetector.io.ASCIIDetector;
//import info.monitorenter.cpdetector.io.CodepageDetectorProxy;
//import info.monitorenter.cpdetector.io.JChardetFacade;
//import info.monitorenter.cpdetector.io.ParsingDetector;
//import info.monitorenter.cpdetector.io.UnicodeDetector;

/**
 * Test file encoding detected
 * 
 * @author hailin84
 *
 */
public class FileEncodingTestCase extends TestCase {
	private String[] files = new String[] { "/GBK.txt", "/UTF-8.txt", "/UTF-8BOM.txt" };

	public FileEncodingTestCase(String name) {
		super(name);
	}

	public void testEncodingDetect() {

		detectOne("GBK", files[0]);
		detectOne("UTF-8 without BOM", files[1]);
		detectOne("UTF-8 with BOM", files[2]);

	}

	private void detectOne(String type, String file) {
		byte[] bom = new byte[] { '\0', '\0', '\0' };
		InputStream in = this.getClass().getResourceAsStream(file);
		StringBuffer sbMsg = new StringBuffer();
		sbMsg.append(type).append(":\n");
		try {
			in.read(bom);
			int k = 0;
			sbMsg.append("\t").append(k++).append("=").append(bom[0]).append("\n");
			sbMsg.append("\t").append(k++).append("=").append(bom[1]).append("\n");
			sbMsg.append("\t").append(k++).append("=").append(bom[2]);
			System.out.println(sbMsg);

			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void testDetectByCpDetector() {
		/**
		 * <pre>
		 * 1、cpDetector内置了一些常用的探测实现类,这些探测实现类的实例可以通过add方法加进来,如:ParsingDetector、 JChardetFacade、ASCIIDetector、UnicodeDetector. 
		 * 2、detector按照“谁最先返回非空的探测结果,就以该结果为准”的原则. 
		 * 3、cpDetector是基于统计学原理的,不保证完全正确.
		 * </pre>
		 */
		
		/**
		CodepageDetectorProxy detector = CodepageDetectorProxy.getInstance();
		detector.add(new ParsingDetector(false));
		detector.add(JChardetFacade.getInstance());// 需要第三方JAR包:antlr.jar、chardet.jar.
		detector.add(ASCIIDetector.getInstance());
		detector.add(UnicodeDetector.getInstance());
		Charset charset = null;
		
		for (String file : files) {
			URL url = this.getClass().getResource(file);
			try {
				charset = detector.detectCodepage(url);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			System.out.println(file + " ---> " + charset.name());
		} */
	}
}
