package org.alive.learn.thread;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * 自定义的互斥锁，同一时刻只能被一个线程独占，内部采用同步器AQS子类实现，不支持重入
 * 
 * @author hailin84
 * @since 2017.07.31
 */
public class Mutex implements Lock {

	private final Sync sync = new Sync();
	
	@Override
	public void lock() {
		sync.acquire(1);

	}

	@Override
	public void lockInterruptibly() throws InterruptedException {
		sync.acquireInterruptibly(1);

	}

	@Override
	public boolean tryLock() {
		return sync.tryAcquire(1);
	}

	@Override
	public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
		return sync.tryAcquireNanos(1, unit.toNanos(time));
	}

	@Override
	public void unlock() {
		sync.release(1);
	}

	@Override
	public Condition newCondition() {
		return sync.newCondition();
	}

	/**
	 * 使用同步器AQS子类实现锁
	 */
	private static final class Sync extends AbstractQueuedSynchronizer {

		private static final long serialVersionUID = -4988566228491751363L;

		/**
		 * 状态为0时获取锁
		 */
		@Override
		protected boolean tryAcquire(int arg) {
			if (compareAndSetState(0, 1)) {
				setExclusiveOwnerThread(Thread.currentThread());
				return true;
			}
			return false;
		}

		/**
		 * 释放锁，状态设置为0
		 */
		@Override
		protected boolean tryRelease(int arg) {
			if (getState() == 0) {
				throw new IllegalMonitorStateException();
			}
			setExclusiveOwnerThread(null);
			setState(0);
			return true;
		}

		/**
		 * 是否独占
		 */
		@Override
		protected boolean isHeldExclusively() {
			return getState() == 1;
		}

		Condition newCondition() {
			return new ConditionObject();
		}
	}
}
