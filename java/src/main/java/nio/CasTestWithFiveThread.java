package nio;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

public class CasTestWithFiveThread {
    private volatile int a;

    private static final Unsafe unsafe;

    // 获取字段偏移量，只执行一次
    private static final long A_OFFSET;

    static {
        try {
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            unsafe = (Unsafe) field.get(null);

            // 获取字段偏移地址，只执行一次
            A_OFFSET = unsafe.objectFieldOffset(CasTestWithFiveThread.class.getDeclaredField("a"));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Failed to initialize Unsafe instance", e);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        long start = System.currentTimeMillis();
        CasTestWithFiveThread casTest = new CasTestWithFiveThread();

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 40000000; i++) {
                casTest.increment();
            }
        });

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 40000000; i++) {
                casTest.increment();
            }
        });

        Thread t3 = new Thread(() -> {
            for (int i = 0; i < 40000000; i++) {
                casTest.increment();
            }
        });

        Thread t4 = new Thread(() -> {
            for (int i = 0; i < 40000000; i++) {
                casTest.increment();
            }
        });

        Thread t5 = new Thread(() -> {
            for (int i = 0; i < 40000000; i++) {
                casTest.increment();
            }
        });

        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t5.start();

        t1.join();
        t2.join();
        t3.join();
        t4.join();
        t5.join();

        System.out.println("结果:" + casTest.a);
        System.out.println("耗时:" + (System.currentTimeMillis() - start));
    }

    private void increment() {
        while (true) {
            if (unsafe.compareAndSwapInt(this, A_OFFSET, a, a + 1)) {
                break;
            }
        }
    }
}
