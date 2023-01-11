package org.alive.test.testcase;

import java.sql.Timestamp;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.alive.test.core.TestCase;

/**
 * Testcase for java collections.
 * 
 * @author hailin84
 *
 */
public class CollectionTestCase extends TestCase {
	
	public CollectionTestCase(String name) {
		super(name);
	}

	/**
	 * @see java.util.PriorityQueue
	 */
	public void mtestPriorityQueue() {
		PriorityQueue<SimpleTask> pq = new PriorityQueue<SimpleTask>(20,
				new Comparator<SimpleTask>() {

			public int compare(SimpleTask o1, SimpleTask o2) {
				if (o1.executeTime > o2.executeTime) {
					return 1;
				}
				if (o1.executeTime == o2.executeTime) {
					return 0;
				}
				return -1;
			}
		});
		
		long time = System.currentTimeMillis();
		pq.add(new SimpleTask("A", time + 100000L));
		pq.add(new SimpleTask("B", time));
		pq.add(new SimpleTask("C", time - 100000L));
		pq.add(new SimpleTask("D", time + 500000L));
		pq.add(new SimpleTask("E", time - 500000L));
		
		while(!pq.isEmpty()) {
			System.out.println(pq.poll().toString()); // Expected sequence: E, C, B, A, D
		}
	}
	
	public void mtestPriorityBlockingQueue() {
		PriorityBlockingQueue<Timestamp> pq = new PriorityBlockingQueue<Timestamp>(20,
				new Comparator<Timestamp>() {

			public int compare(Timestamp o1, Timestamp o2) {
				if (o1.after(o2)) {
					return 1;
				}
				if (o1.before(o2)) {
					return -1;
				}
				return 0;
			}
		});
		
		long time = System.currentTimeMillis();
		pq.add(new Timestamp(time + 10000000L));
		pq.add(new Timestamp(time));
		pq.add(new Timestamp(time - 10000000L));
		pq.add(new Timestamp(time + 50000000L));
		pq.add(new Timestamp(time - 50000000L));
		
		System.out.println(pq);
		while(!pq.isEmpty()) {
			System.out.println(pq.poll().toString()); //
		}
	}
	
	public void testRemove() {
		Map<String, String> data = new ConcurrentHashMap<>();
		data.put("1", "value1");
		data.put("2", "value2");
		data.put("3", "value3");
		data.put("4", "value4");
		data.put("5", "value5");
		
		Iterator<Map.Entry<String, String>> itor = data.entrySet().iterator();
		for (; itor.hasNext();) {
			Map.Entry<String, String> entry = itor.next();
			String key = entry.getKey();
			String value = entry.getValue();
			System.out.println(key + " -- " + value);
			
			if ("3".equals(key)) {
				Thread t = new Thread(new Runnable() {
					
					@Override
					public void run() {
						System.out.println("remove: " + key);
						data.remove(key);
					}
				});
				t.start();
			}
			try {
				TimeUnit.SECONDS.sleep(5);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			System.out.println(data);
			itor.remove();
		}
	}
}


class SimpleTask {
	String name;
	
	long executeTime;

	public SimpleTask(String name, long executeTime) {
		this.name = name;
		this.executeTime = executeTime;
	}

	@Override
	public String toString() {
		return "SimpleTask [name=" + name + ", executeTime=" + executeTime
				+ "]";
	}
}
