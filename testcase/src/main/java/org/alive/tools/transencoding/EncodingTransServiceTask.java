package org.alive.tools.transencoding;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import org.alive.tools.socketmessage.UIUpdater;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

/**
 * 编码转换服务线程Task，用于启动线程池，实现多线程处理
 * 
 * @author hailin84
 *
 */
public class EncodingTransServiceTask implements Runnable {
	private static Logger logger = Logger.getLogger(EncodingTransTask.class);

	private boolean running = true;

	private String sourcePath;

	private String fileTypes;

	private String encoding;

	private UIUpdater updater;

	/** 消息处理任务线程池 */
	private ExecutorService service = null;

	/** 计数器 */
	private static AtomicInteger count = null;

	public EncodingTransServiceTask(String fileTypes, String encoding, String sourcePath, UIUpdater updater) {
		super();
		this.fileTypes = fileTypes;
		this.encoding = encoding;
		this.sourcePath = sourcePath;
		this.updater = updater;

	}

	public void run() {
		Set<String> fileExtentions = new HashSet<String>();
		String[] types = StringUtils.split(fileTypes);
		fileExtentions.addAll(Arrays.asList(types));

		Queue<String> fq = new ConcurrentLinkedQueue<String>();
		File f = new File(sourcePath);
		if (f.isDirectory()) {
			Collection<File> files = FileUtils.listFiles(f, types, true);
			for (Iterator<File> oneFile = files.iterator(); oneFile.hasNext();) {
				File f2 = oneFile.next();
				fq.add(f2.getAbsolutePath());
			}
		} else {
			fq.add(sourcePath);
		}
		count = new AtomicInteger(fq.size());
		int all = count.get();
		updater.update("status", new Object[] { "S: 0/" + all });
		
		// 线程池最大数为10
		this.service = Executors.newFixedThreadPool(10);

		// 调度文件队列
		while (running) {
			String fpath = fq.poll();

			if (fpath == null) {
				logger.info("文件队列已处理完毕!");
				running = false;
				continue;
			}

			EncodingTransTask transTask = new EncodingTransTask(fpath, fileExtentions, encoding);
			transTask.setCount(count);
			service.execute(transTask);
		}
		this.service.shutdown();

		// 检查任务执行情况，更新界面
		running = true;
		while (running) {
			int done = all - count.get();
			if (done % 10 == 0) {
				updater.update("status", new Object[] { "S: " + done + "/" + all });
			}
			if (count.get() == 0) {
				updater.update("status", new Object[] { "完成: " + done + "/" + all });
				running = false;
				break;
			}
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
