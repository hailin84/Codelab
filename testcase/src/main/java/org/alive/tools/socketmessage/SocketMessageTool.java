package org.alive.tools.socketmessage;

import javax.swing.SwingUtilities;

/**
 * 连接指定的主机、端口，发送TCP消息并接收返回消息。用于测试。
 * 
 * @author hailin84
 *
 */
public class SocketMessageTool {

	public static void main(String[] args) {
		// SwingUtilities.invokeLater是为了将绘制界面、更新界面等操作放到EDT线程中执行
		SwingUtilities.invokeLater(new Runnable() {  
	        public void run() {  
	        	new SocketMessageUI();
	        }  
	    });
	}
}
