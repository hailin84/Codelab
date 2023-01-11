package org.alive.learn.thread;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 简单的Cache类，使用读写锁和HashMap实现，线程安全
 * 
 * @author hailin84
 * @date 2017.08.01
 */
public class SimpleCache {
	static Map<String, Object> data = new HashMap<>();

	static ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();

	static Lock r = rwl.readLock();
	static Lock w = rwl.writeLock();

	public static Object get(String key) {
		r.lock(); // lock不写到try中，防止在lock阶段抛出异常导致的实际未先lock却调用了unlock
		try {
			return data.get(key);
		} finally {
			r.unlock();
		}
	}

	/**
	 * 放入新值，返回旧值，如果有的话
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public static Object put(String key, Object value) {
		w.lock();
		try {
			return data.put(key, value);
		} finally {
			w.unlock();
		}
	}

	public static void clear() {
		w.lock();
		try {
			data.clear();
		} finally {
			w.unlock();
		}
	}
}
