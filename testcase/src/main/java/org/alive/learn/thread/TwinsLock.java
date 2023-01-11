package org.alive.learn.thread;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * 自定义锁。同一时刻最多支持两个线程同时访问
 * 
 * @author hailin84
 * @date 2017.08.01
 */
public class TwinsLock implements Lock, Serializable {

	private static final long serialVersionUID = -7286210721292000188L;

	private final Sync sync = new Sync(2); // 定义为2，即只支持最多2个并发访问
	
	static final class Sync extends AbstractQueuedSynchronizer {
		private static final long serialVersionUID = -3382713664232592683L;
		
		Sync(int count) {
			if (count <= 0) {
				throw new IllegalArgumentException("count must be greater than 0");
			}
			setState(count);
		}

		@Override
		protected int tryAcquireShared(int arg) {
			for (;;) {
				int current = getState();
				int newCount = current - arg;
				if (newCount < 0 || compareAndSetState(current, newCount)) {
					return newCount;
				}
			}
		}

		@Override
		protected boolean tryReleaseShared(int arg) {
			for (;;) {
				int current = getState();
				int newCount = current + arg;
				if (compareAndSetState(current, newCount)) {
					return true;
				}
			}
		}
		
		
	}
	
	@Override
	public void lock() {
		sync.acquireShared(1);
		
	}

	@Override
	public void lockInterruptibly() throws InterruptedException {
	}

	@Override
	public boolean tryLock() {
		return false;
	}

	@Override
	public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
		return false;
	}

	@Override
	public void unlock() {
		sync.releaseShared(1);
		
	}

	@Override
	public Condition newCondition() {
		return null;
	}

}
