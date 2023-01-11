package org.alive.tools.socketmessage;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/**
 * <p>供后台线程调用更新UI的类，提供通用的UI更新机制，也用于解耦合后台代码和UI，似乎使用事件机制会好点?
 * 最终对UI的更新也是放到EDT线程中执行。</p>
 * 
 * <p>注意如果有两个线程同时调用同一个UIUpdater实例的register和update方法，会引起线程同步问题。
 * 比如WorkerTask和ReadMessageTask两个线程同时调用register方法，同时访问uiMap时就会引起同步问题。</p>
 * 
 * <p>本程序中没有这种情况，对uiMap的访问最终都是在EDT线程中，也就不需要考虑线程同步问题了。</p>
 * 
 * @author hailin84
 * 
 */
public class UIUpdater {

	private static Map<Class<? extends JComponent>, Item> COMPONENT_MAP = new ConcurrentHashMap<Class<? extends JComponent>, Item>();

	// TODO: 添加其他的JComponent
	static {
		COMPONENT_MAP.put(JTextField.class, new Item("setText",
				new Class[] { String.class }));
		COMPONENT_MAP.put(JLabel.class, new Item("setText",
				new Class[] { String.class }));
		COMPONENT_MAP.put(JTextArea.class, new Item("setText",
				new Class[] { String.class }));
		COMPONENT_MAP.put(JButton.class, new Item("setText",
				new Class[] { String.class }));
	}

	private Map<String, JComponent> uiMap = new HashMap<String, JComponent>();

	/**
	 * 注册需要使用UIUpdater进行更新的UI component
	 * 
	 * @param name
	 * @param component
	 */
	public void register(String name, JComponent component) {
		uiMap.put(name, component);
	}

	/**
	 * 更新UI控件的值
	 * 
	 * @param name
	 * @param values
	 */
	public void update(String name, Object[] values) {
		final String fName = name;
		final Object[] fValue = values;
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JComponent component = uiMap.get(fName);
				if (component == null) {
					return;
				}
				Item item = COMPONENT_MAP.get(component.getClass());

				try {
					Method m = component.getClass().getMethod(item.methodName,
							item.parameterTypes);
					m.invoke(component, fValue);
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		});
	}

	static class Item {
		String methodName;
		Class<?>[] parameterTypes;

		public Item(String methodName, Class<?>[] parameterTypes) {
			super();
			this.methodName = methodName;
			this.parameterTypes = parameterTypes;
		}
	}
}
