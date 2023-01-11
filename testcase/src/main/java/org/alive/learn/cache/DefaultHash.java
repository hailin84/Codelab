package org.alive.learn.cache;

public class DefaultHash implements IHash<Integer> {

	@Override
	public Integer hash(Object key) {
		return key.hashCode();
	}

}
