package org.alive.idealab.learn.jvm;

/**
 * 图灵学院诸葛老师JMM课程示例代码
 */
public class VolatileVisibilityTest {
    private static boolean initFlag = false;
    public static void main(String[] args) throws InterruptedException {
        // 期望效果是等tryEndLoop执行之后，initFlag=true，loop也很快结束。实际上loop不会结束，因为initFlag变量不是volatile的。
        new Thread(VolatileVisibilityTest::loop).start();
        Thread.sleep(2000);
        new Thread(VolatileVisibilityTest::tryEndLoop).start();
    }

    public static void loop() {
        System.out.println("================ start");
        while (!initFlag) {
            // 加上sleep，不论initFlag是否volatile均可以正常结束
//            try {
//                Thread.sleep(0);
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
        }
        System.out.println("================ success");
    }

    public static void tryEndLoop() {
        System.out.println("prepare data...");
        initFlag = true;
        System.out.println("prepare data end...");
    }
}
