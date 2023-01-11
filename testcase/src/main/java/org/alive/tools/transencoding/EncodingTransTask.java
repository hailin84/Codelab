package org.alive.tools.transencoding;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import org.alive.util.EncodingDetectUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;

/**
 * 编码转换Task，转换一个文件
 * 
 * @author hailin84
 * @since 2017.05.11
 *
 */
public class EncodingTransTask implements Runnable {
	private static Logger logger = Logger.getLogger(EncodingTransTask.class);

	private String file;

	private Set<String> fileExtentions;

	private String encoding;

	/** 计数器 */
	private AtomicInteger count; 
	
	public EncodingTransTask(String file, Set<String> fileExtentions, String encoding) {
		super();
		this.file = file;
		this.fileExtentions = fileExtentions;
		this.encoding = encoding;
	}
	
	public AtomicInteger getCount() {
		return count;
	}

	public void setCount(AtomicInteger count) {
		this.count = count;
	}



	public void run() {
		if (fileExtentions.contains(FilenameUtils.getExtension(file))) {
			final Charset c = EncodingDetectUtil.detect(file);
			if (!EncodingDetectUtil.isTheSameEncoding(c.name(), encoding)) {
				String fileNew = file + ".new";
				File oldF = new File(file);
				File newF = new File(fileNew);
				try {
					String content = FileUtils.readFileToString(oldF, c);
					FileUtils.writeStringToFile(newF, content, encoding);

					oldF.delete();
					newF.renameTo(oldF);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				logger.info(MessageFormat.format("{0}: {1} --> {2} -- 转换", new Object[] { file, c.name(), encoding }));
				// FileUtils.copyFile(new File(file), new File(fileNew));
			} else {
				logger.info(
						MessageFormat.format("{0}: {1} --> {2} -- 无需要转换", new Object[] { file, c.name(), encoding }));
			}
		} else {
			logger.info(MessageFormat.format("{0}: 扩展名不符，无需转换", new Object[] { file }));
		}
		
		count.decrementAndGet();
	}

}
