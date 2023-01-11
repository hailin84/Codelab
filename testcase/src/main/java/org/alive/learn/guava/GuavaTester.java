package org.alive.learn.guava;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.base.Joiner;
import com.google.common.base.Optional;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.ObjectArrays;
import com.google.common.collect.Sets;

public class GuavaTester {

	public static void main(String args[]) {
		basic();
		joiner();

		sum(5, 5);
	}

	public static void basic() {
		// 1,简化集合的创建和初始化
		Map<String, Map<String, String>> map = Maps.newHashMap();
		List<List<Map<String, String>>> list = Lists.newArrayList();
		List<Integer> personList = Lists.newLinkedList();
		Set<String> personSet = Sets.newHashSet("One", "Two", "Three");
		Map<String, String> personMap = Maps.newHashMap();
		Integer[] intArrays = ObjectArrays.newArray(Integer.class, 10);
		
		trace(map, list, personList, personSet, personMap, intArrays);
	}

	public Integer sum(Optional<Integer> a, Optional<Integer> b) {
		// Optional.isPresent - checks the value is present or not
		System.out.println("First parameter is present: " + a.isPresent());

		System.out.println("Second parameter is present: " + b.isPresent());

		// Optional.or - returns the value if present (); returns
		// the default value passed.
		Integer value1 = a.or(new Integer(0));

		// Optional.get - gets the value, value should be present
		Integer value2 = b.get();

		return value1 + value2;
	}

	public static void joiner() {
		Joiner j = Joiner.on("-").skipNulls();
		System.out.println(j.join("result", 1, 2, 3, null, 6));
		// j.appendTo(appendable, parts)
	}

	public static void spliter() {
		Splitter.on(' ').split("1 2 3"); // ["1", "2", "3"]
		Splitter.on("#").withKeyValueSeparator(":").split("1:2#3:4");// {"1":"2",
																		// "3":"4"}
	}

	public static void optional() {

	}

	private static Integer sum(Integer a, Integer b) {
		Optional<Integer> oa = Optional.of(a);
		Optional<Integer> ob = Optional.of(b);
		return oa.get() + ob.get();

		// return a + b;
	}
	
	private static void trace(Object... params) {
		Joiner j = Joiner.on("\n");
		System.out.println(j.join(params));
	}
}
