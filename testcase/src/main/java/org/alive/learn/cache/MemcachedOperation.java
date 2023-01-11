package org.alive.learn.cache;

import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.MemcachedClientBuilder;
import net.rubyeye.xmemcached.XMemcachedClientBuilder;
import net.rubyeye.xmemcached.utils.AddrUtil;

/**
 * Memcached
 * 
 * @author hailin84
 * @date 2017.08.15
 */
public class MemcachedOperation {

	public static void main(String[] args) {
		MemcachedClientBuilder builder = new XMemcachedClientBuilder(AddrUtil

				.getAddresses("10.14.205.174:11211"));

		MemcachedClient memcachedClient;

		try {

			memcachedClient = builder.build();

			memcachedClient.set("hello", 0, "Hello,xmemcached");

			String value = memcachedClient.get("hello");

			System.out.println("hello=" + value);

			memcachedClient.delete("hello");

			value = memcachedClient.get("hello");

			System.out.println("hello=" + value);

			// close memcached client
			memcachedClient.shutdown();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
