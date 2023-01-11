package org.alive.test.testcase;

import org.alive.test.core.TestCase;

/**
 * 泛型测试代码
 * 
 * @author hailin84
 * @since 2017.05.26
 *
 */
public class GenericsTestCase extends TestCase {

	public GenericsTestCase(String name) {
		super(name);
	}

	public void testOne() {
		SonOne one = new SonOne();
		one.setInfo("this is one --> " + one.getClass().getName());
		System.out.println(one);

		SonTwo<Integer> two = new SonTwo<Integer>();
		two.setInfo(100);
		System.out.println(two);

		SonThree<Double> three = new SonThree<>();
		three.setInfo("this is three --> " + three.getClass().getName());
		three.setWeight(150.4D);
		System.out.println(three);
	}
}

class Father<T> {
	protected T info;

	public T getInfo() {
		return info;
	}

	public void setInfo(T info) {
		this.info = info;
	}

	@Override
	public String toString() {
		return "Father [info=" + info + "]";
	}
	// 泛型方法
	public static <A> A func1(A arg) { return null;}
	public static <B> void func2(B arg1, B arg2) { }
}

class SonOne extends Father<String> {

	@Override
	public String toString() {
		return "SonOne [info=" + info + "]";
	}
}

/**
 * 子类需要继承泛型，必须父类保持一样的声明，不然会报错
 * 
 * @author hailin84
 *
 * @param <T>
 */
class SonTwo<T> extends Father<T> {

	@Override
	public String toString() {
		return "SonTwo [info=" + info + "]";
	}
}

/**
 * 子类自己是泛型，继承的不是父类的泛型
 * 
 * @author hailin84
 *
 * @param <T>
 */
class SonThree<T> extends Father<String> {
	protected T weight;

	public T getWeight() {
		return weight;
	}

	public void setWeight(T weight) {
		this.weight = weight;
	}

	@Override
	public String toString() {
		return "SonThree [weight=" + weight + ", info=" + info + "]";
	}
}
