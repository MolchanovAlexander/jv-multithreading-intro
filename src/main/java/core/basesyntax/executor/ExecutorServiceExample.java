package core.basesyntax.executor;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ExecutorServiceExample {
    static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(2);

        Callable<String> task1 = () -> {
            Thread.sleep(1000);
            return "Result of Task1";
        };

        Callable<String> task2 = () -> {
            Thread.sleep(2000);
            return "Result of Task2";
        };

        Future<String> future1 = executor.submit(task1);
        Future<String> future2 = executor.submit(task2);

        System.out.println(future1.get());
        System.out.println(future2.get());

        executor.shutdown();
    }
}

