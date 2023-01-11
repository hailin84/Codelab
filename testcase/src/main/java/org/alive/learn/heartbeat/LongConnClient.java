package org.alive.learn.heartbeat;

import javax.swing.SwingUtilities;

public class LongConnClient {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {

		// 添加Hook，以便程序退出时先关闭相应的线程
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				ClientStarter.stopAll();
			}
		});

		// 初始化界面
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new LongConnClientUI();
			}
		});
	}
}
