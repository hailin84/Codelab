package org.alive.learn.zookeeper;

import java.util.List;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

/**
 * Zookeeper client
 * 
 * @author hailin84
 *
 */
public class TestZookeeper {

	public static void main(String[] args) throws Exception {
		// 创建zookeeper客户端
		ZooKeeper zooKeeper = new ZooKeeper("127.0.0.1:2181", 1000, new Watcher() {

			public void process(WatchedEvent event) {
				System.out.println("EventType:" + event.getType().name());
			}
		});

		// 获取"/" node下的所有子node
		List<String> znodes = zooKeeper.getChildren("/", true);
		for (String path : znodes) {
			System.out.println(path);
		}

		System.in.read();
	}
}
