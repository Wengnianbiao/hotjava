package nio;

public class IncrementTest {
    private int a; // 不需要 volatile 或原子类

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        IncrementTest test = new IncrementTest();

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 100000000; i++) {
                test.a++;
            }
        });

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 100000000; i++) {
                test.a++;
            }
        });

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("最终结果：" + test.a);
        System.out.println("耗时:" + (System.currentTimeMillis() - start));
    }
}
