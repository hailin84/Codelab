package org.alive.learn.thread;

import java.util.concurrent.RecursiveTask;

/**
 * <p>
 * Fork/Join示例，计算1+2+3+...+n
 * 
 * @author hailin84
 * @date 2017.08.02
 */
public class CountTask extends RecursiveTask<Long> {

	private static final long serialVersionUID = -8235717587145475423L;
	
	private static final int THRESHOLD = 2;
	
	private long start, end;
	
	public CountTask(long start, long end) {
		super();
		this.start = start;
		this.end = end;
	}



	@Override
	protected Long compute() {
		long sum = 0;
		// 如果低于阀值，则直接计算，否则分裂成两个子任务计算
		boolean canCompute = (end - start) <= THRESHOLD;
		if (canCompute) {
			System.out.printf("%s - 计算[%d, %d]", Thread.currentThread().getName(), start, end).println();
			for (long i = start; i <= end; i++) {
				sum += i;
			}
		} else {
			long middle = (start + end) / 2;
			// 分裂为子任务
			CountTask left = new CountTask(start, middle);
			CountTask right = new CountTask(middle + 1,  end);
			
			// 执行子任务
			left.fork();
			right.fork();
			
			// 等待子任务执行结果
			long leftResult = left.join();
			long rightResult = right.join();
			
			// 合并子任务计算结果
			sum = leftResult + rightResult;
			
			
		}
		return sum;
	}

}
