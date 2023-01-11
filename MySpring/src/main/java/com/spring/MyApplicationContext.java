package com.spring;

import java.io.File;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>
 * 手写Spring的ApplicationContext类
 * </p>
 *
 * @author hailin84
 * @since 2023/1/10
 */
public class MyApplicationContext {
    private Class configClass;

    private Map<String, Object> singletonObjects = new ConcurrentHashMap<>();

    private Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();

    private List<BeanPostProcessor> beanPostProcessorList = new ArrayList<>();

    public MyApplicationContext(Class configClass) {
        this.configClass = configClass;
        scan(configClass);
        createSingletonBeans();
    }

    private void scan(Class configClass) {
        // 解析configClass
        ComponentScan componentScan = (ComponentScan) configClass.getDeclaredAnnotation(ComponentScan.class);
        if (componentScan == null) {
            return;
        }
        String scanPath = componentScan.value().replace(".", "/");
        // ClassLoader:
        // Bootstrap --- jre/lib
        // Ext       --- jre/ext/lib
        // App       --- classpath
        //
        // 通过ClassLoader加载classpath下面的Bean类
        ClassLoader appClassLoader = MyApplicationContext.class.getClassLoader();
        URL resource = appClassLoader.getResource(scanPath);
        File file = new File(resource.getFile());
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f : files) {
                String filePath = f.getAbsolutePath();
                // 只扫描.class文件，其他文件直接跳过
                if (!filePath.endsWith(".class")) {
                    continue;
                }

                String className = filePath.substring(filePath.indexOf(scanPath.replace("/", "\\")), filePath.indexOf(".class"));
                className = className.replace("\\", ".");
                try {
                    Class c = appClassLoader.loadClass(className);
                    // 没有Component注解，说明不是Spring Bean，不需要继续处理
                    if (!c.isAnnotationPresent(Component.class)) {
                        continue;
                    }

                    Component component = (Component) c.getDeclaredAnnotation(Component.class);
                    String beanName = component.value();

                    BeanDefinition d = new BeanDefinition();
                    d.setClazz(c);
                    if (c.isAnnotationPresent(Scope.class)) {
                        Scope scope = (Scope) c.getDeclaredAnnotation(Scope.class);
                        d.setScope(scope.value());
                    } else {
                        d.setScope("singleton");
                    }
                    beanDefinitionMap.put(beanName, d);

                    // 选择这里直接初始化BeanPostProcessor，跟Spring实现略不一样，忽略
                    if (BeanPostProcessor.class.isAssignableFrom(c)) {
                        try {
                            beanPostProcessorList.add((BeanPostProcessor) c.newInstance());
                        } catch (InstantiationException e) {
                            throw new RuntimeException(e);
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException(e);
                        }
                    }
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private void createSingletonBeans() {
        for (Map.Entry<String, BeanDefinition> entry : beanDefinitionMap.entrySet()) {
            String beanName = entry.getKey();
            if ("singleton".equals(entry.getValue().getScope())) {
                singletonObjects.put(beanName, createBean(beanName, entry.getValue()));
            }
        }
    }

    public Object createBean(String beanName, BeanDefinition definition) {
        Class c = definition.getClazz();
        try {
            Object instance = c.newInstance();
            // 依赖注入
            for (Field declaredField : c.getDeclaredFields()) {
                if (declaredField.isAnnotationPresent(Autowired.class)) {
                    Object bean = getBean(declaredField.getName());
                    declaredField.setAccessible(true);
                    declaredField.set(instance, bean);
                }
            }

            // BeanNameAware回调
            if (instance instanceof BeanNameAware) {
                ((BeanNameAware) instance).setBeanName(beanName);
            }

            // 初始化，效果跟PostConstruct一样
            for (BeanPostProcessor beanPostProcessor : beanPostProcessorList) {
                instance = beanPostProcessor.postProcessBeforeInitialization(instance, beanName);
            }

            // 初始化，InitializingBean
            if (instance instanceof InitializingBean) {
                try {
                    ((InitializingBean) instance).afterPropertiesSet();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            // 初始化后。如果是AOP则会产生代理对象，代理对象里的依赖不会自动注入。
            // 代理对象里的target保存原来的普通对象，调用的时候先执行AOP方法逻辑，再调用
            // target上的方法。
            for (BeanPostProcessor beanPostProcessor : beanPostProcessorList) {
                instance = beanPostProcessor.postProcessAfterInitialization(instance, beanName);
            }

            return instance;
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public Object getBean(String beanName) {
        BeanDefinition d = beanDefinitionMap.get(beanName);
        if (d == null) {
            throw new NullPointerException("No bean named " + beanName);
        }
        if ("singleton".equals(d.getScope())) {
            return singletonObjects.get(beanName);
        }

        return createBean(beanName, d);
    }
}
