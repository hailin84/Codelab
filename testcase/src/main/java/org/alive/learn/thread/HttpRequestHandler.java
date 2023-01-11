package org.alive.learn.thread;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * <p>
 * 简易HTTP request handler
 * 
 * @author hailin84
 * @date 2017.08.30
 */
public class HttpRequestHandler implements Runnable {

	private Socket socket;

	/** web应用根目录 */
	private String basePath;

	public HttpRequestHandler(Socket socket, String basePath) {
		super();
		this.socket = socket;
		this.basePath = basePath;
	}

	@Override
	public void run() {
		String line = null;
		BufferedReader br = null;
		BufferedReader reader = null;
		PrintWriter out = null;
		InputStream in = null;
		try {
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String header = reader.readLine();
			// 由相对路径计算出绝对路径
			String filePath = basePath + header.split(" ")[1];
			out = new PrintWriter(socket.getOutputStream());
			// 如果请求资源的后缀为jpg或者ico，则读取资源并输出
			if (filePath.endsWith("jpg") || filePath.endsWith("ico") || filePath.endsWith("png")) {
				in = new FileInputStream(filePath);
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				int i = 0;
				while ((i = in.read()) != -1) {
					baos.write(i);
				}
				byte[] array = baos.toByteArray();
				out.println("HTTP/1.1 200 OK");
				out.println("Server: Molly");
				out.println("Content-Type: image/png");
				out.println("Content-Length: " + array.length);
				out.println("");
				socket.getOutputStream().write(array, 0, array.length);
			} else {
				// html页面保存为UTF-8，同时页面声明<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />，且响应头中设置了Content-Type为UTF-8；
				// 故从文件中读取数据，以及PrintWriter都需要按UTF-8格式处理
				br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "UTF-8"));
				out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));
				out.println("HTTP/1.1 200 OK");
				out.println("Server: Molly");
				out.println("Content-Type: text/html; charset=UTF-8");
				out.println("");
				while ((line = br.readLine()) != null) {
					// System.out.println(line);
					out.println(line);
				}
			}
			out.flush();
		} catch (Exception ex) {
			out.println("HTTP/1.1 500");
			out.println("");
			out.flush();
		} finally {
			// 连接关闭，所以HTTP请求和响应中的keep-alive无用
			close(br, in, reader, out, socket);
		}
	}

	private void close(Closeable... closeables) {
		if (closeables != null) {
			for (Closeable c : closeables) {
				if (c == null) {
					continue;
				}
				try {
					c.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
