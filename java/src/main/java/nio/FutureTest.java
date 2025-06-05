package nio;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class FutureTest {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> System.out.println("hello!"));
        future.get();// 输出 "hello!"
        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> "hello!");
        String s = future2.get();
        System.out.println(s);
    }
}
