package org.alive.tools.transencoding;

import javax.swing.SwingUtilities;

public class TransEncodingMain {

	public static void main(String[] args) {
		// SwingUtilities.invokeLater是为了将绘制界面、更新界面等操作放到EDT线程中执行
		SwingUtilities.invokeLater(new Runnable() {  
	        public void run() {  
	        	new TransEncodingUI();
	        }  
	    });
	}

}
