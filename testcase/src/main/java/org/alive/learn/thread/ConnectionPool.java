package org.alive.learn.thread;

import java.sql.Connection;
import java.util.LinkedList;

/**
 * <p>
 * 简单的Connection Pool实现，《Java并发编程艺术》示例代码
 * 
 * @author hailin84
 * @date 2017.08.29
 *
 */
public class ConnectionPool {
	private LinkedList<Connection> pool = new LinkedList<Connection>();

	public ConnectionPool(int initSize) {
		if (initSize > 0) {
			for (int i = 0; i < initSize; i++) {
				pool.addLast(ConnectionDriver.createConnection());
			}
		}
	}
	
	public Connection fetchConnection(int mills) throws InterruptedException {
		synchronized (pool) {
			if (mills <= 0) {
				while (pool.isEmpty()) {
					pool.wait();
				}
				return pool.removeFirst();
			} else {
				long future = System.currentTimeMillis() + mills;
				long remaining = mills;
				while (pool.isEmpty() && remaining > 0) {
					pool.wait(remaining);
					remaining = future - System.currentTimeMillis();
				}
				return pool.isEmpty() ? null : pool.removeFirst();
			}
		}
	}
	
	public void releaseConnection(Connection conn) {
		if (conn != null) {
			synchronized (pool) {
				// 连接释放后通知其他线程，因只有一个连接，故只需要notify一个等待的线程即可，不需要notifyAll
				pool.addLast(conn);
				pool.notify();
			}
		}
	}
}
