package core.basesyntax.executor;

import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class InvokeAllExample {
    static void main(String[] args) throws InterruptedException {
        System.out.println("started at: " + LocalTime.now());
        try (ExecutorService executor = Executors.newFixedThreadPool(20)) {

            Callable<String> task1 = () -> "Task 1 Result";
            Callable<String> task2 = () -> "Task 2 Result";
            Callable<String> task3 = () -> "Task 3 Result";

            List<Callable<String>> taskList = List.of(task1, task2, task3);

            List<Future<String>> futures = executor.invokeAll(taskList);
            System.out.println(futures);
            executor.shutdown();
            for (Future<String> future : futures) {
                System.out.println(future.get());
            }
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        System.out.println("finished at: " + LocalTime.now());
    }
}
