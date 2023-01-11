package org.alive.learn.thread;

/**
 * <p>
 * 简单的Thread Pool实现，《Java并发编程艺术》示例代码
 * 
 * @author hailin84
 * @date 2017.08.29
 *
 */
public interface ThreadPool<Job extends Runnable> {
	void execute(Job job);
	
	void shutdown();
	
	// 增加工作线程
	void addWorkers(int num);
	
	// 减少工作线程
	void removeWorkers(int num);
	
	int getJobSize();
}
