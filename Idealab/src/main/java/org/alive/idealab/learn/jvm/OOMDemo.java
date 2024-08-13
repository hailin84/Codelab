package org.alive.idealab.learn.jvm;

//import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 测试OOM
 */
public class OOMDemo {

    private static int stackDepth = 1;

    public static void main(String[] args) throws Exception {
        // headOOM();
//        stackLeek();
        directMemoryOOM();
    }

    /**
     * -Xms20m -Xmx20m -XX:+HeapDumpOnOutOfMemoryError
     */
    public static void headOOM() {
        List<long[]> data = new ArrayList<>();
        while (true) {
            data.add(new long[10000]);
        }
    }

    private static void stackLeek() {
        stackDepth++;
        stackLeek();
    }

    public static void stackOverFlow() {
        try {
            stackLeek();
        } catch (Throwable t) {
            System.out.println("stack depth " + stackDepth);
            throw t;
        }
    }

    /**
     * -Xmx20M -XX:MaxDirectMemorySize=10M
     * @throws IllegalAccessException
     */
    public static void directMemoryOOM() throws IllegalAccessException {
//        Field unsafeField = Unsafe.class.getDeclaredFields()[0];
//        unsafeField.setAccessible(true);
//        Unsafe unsafe = (Unsafe) unsafeField.get(null);
//        while (true) {
//            unsafe.allocateMemory(1024 * 1024 * 1024);
//        }
    }
}
