package org.alive.learn.cache;

import java.util.Collection;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * <p>
 * 一致性Hash算法，适用于缓存集群；每个缓存结点虚拟为N个虚拟结点，以确保各缓存结点之间的平衡性
 * <p>
 * 底层使用TreeMap实现(红黑树)
 * 
 * @author hailin84
 * @date 2017.08.16
 *
 */
public class ConsistentHash<K, V> {
	private final IHash<K> hashFunction;

	/** 每个实体结点对应的虚拟结点个数，默认为150 */
	private int numberOfReplicas = 150;

	/** 底层的Hash环，红黑树结构，无所谓是否真的构成环形 */
	private final SortedMap<K, V> circle = new TreeMap<K, V>();

	public ConsistentHash(IHash<K> hashFunction, int numberOfReplicas, Collection<V> nodes) {
		this.hashFunction = hashFunction;
		this.numberOfReplicas = numberOfReplicas;

		for (V node : nodes) {
			add(node);
		}
	}

	/**
	 * 如果实体结点为IP,则虚拟结构构成为IP#数字序号
	 * 
	 * @param node
	 */
	public void add(V node) {
		for (int i = 0; i < numberOfReplicas; i++) {
			circle.put(hashFunction.hash(node.toString() + "#" + i), node);
		}
	}

	public void remove(V node) {
		for (int i = 0; i < numberOfReplicas; i++) {
			circle.remove(hashFunction.hash(node.toString() + "#" + i));
		}
	}

	/**
	 * 取得用于存储key的虚拟Node信息，通过Node信息，找到对应的实体结构存储Cache数据
	 * 
	 * @param key
	 * @return
	 */
	public V get(Object key) {
		if (circle.isEmpty()) {
			return null;
		}
		K hash = hashFunction.hash(key);
		if (!circle.containsKey(hash)) {
			SortedMap<K, V> tailMap = circle.tailMap(hash);
			hash = tailMap.isEmpty() ? circle.firstKey() : tailMap.firstKey();
		}
		return circle.get(hash);
	}

}
