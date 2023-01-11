package org.alive.learn.thread;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * <p>
 * 有界队列，当队列为空时，队列的获取操作将会阻塞获取线程，直接队列有元素；当队列空了时，队列的插入操作将会阻塞插入线程，直到有空位；
 * <p>
 * 演示Condition使用
 * 
 * @author hailin84
 * @date 2017.08.02
 *
 */
public class BoundedQueue<T> {
	private static final int DEFAULT_CAP = 8;

	/** 存储数据的数组，Java不支持范型数组，只能在创建的时候转换，或者取值的时候转换(本类采用方式) */
	private Object[] items;

	private int addIndex, removeIndex;
	
	private int size;

	private Lock lock = new ReentrantLock();

	private Condition notEmpty = lock.newCondition();

	private Condition notFull = lock.newCondition();

	/**
	 * cap必须大于0
	 * 
	 * @param cap
	 */
	public BoundedQueue(int cap) {
		this.items = new Object[cap];
	}

	public BoundedQueue() {
		this(DEFAULT_CAP);
	}

	public void add(T item) throws InterruptedException {
		lock.lock();

		try {
			// 注意使用的while而非if，为的是保证有空位才插入数据，防止过早或者意外的通知
			while (size == items.length) {
				notFull.await(); // 释放锁并进入等待状态
			}
			items[addIndex] = item;
			if (++addIndex == items.length) {
				addIndex = 0;
			}
			size++;
			notEmpty.signal(); // 唤醒等待在notEmpty的线程
		} finally {
			lock.unlock();
		}
	}

	@SuppressWarnings("unchecked")
	public T remove() throws InterruptedException {
		lock.lock();
		try {
			while (size == 0) {
				notEmpty.await(); // 释放锁并进入等待状态
			}

			Object x = items[removeIndex];
			if (++removeIndex == items.length) {
				removeIndex = 0;
			}
			size--;
			notFull.signal(); // 唤醒等待在notFull的线程

			return (T) x;
		} finally {
			lock.unlock();
		}
	}

	public int getSize() {
		return size;
	}
}
