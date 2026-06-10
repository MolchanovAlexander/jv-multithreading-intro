package core.basesyntax.deadlock;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;

public class DeadlockDemo {
    // Two resource objects
    private static final Object resource1 = new Object();
    private static final Object resource2 = new Object();

    public static void main(String[] args) throws InterruptedException {
        Thread.sleep(200);
        ThreadMXBean bean = ManagementFactory.getThreadMXBean();
        System.out.println(Arrays.toString(bean.getAllThreadIds()));
        Thread t1 = new Thread(() -> {
            synchronized (resource1) {
                System.out.println("Thread 1: Locked Resource 1");
                try {
                    Thread.sleep(100); // simulate work
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException(e);
                }
                synchronized (resource2) {
                    System.out.println("Thread 1: Locked Resource 2");
                }
            }
        });
        Thread t2 = new Thread(() -> {
            synchronized (resource2) {
                System.out.println("Thread 2: Locked Resource 2");
                try {
                    Thread.sleep(100); // simulate work
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException(e);
                }
                synchronized (resource1) {
                    System.out.println("Thread 2: Locked Resource 1");
                }
            }
        });


        // Start both threads
        t1.start();
        t2.start();
        for (long id : bean.getAllThreadIds()) {
            ThreadInfo info = bean.getThreadInfo(id);

            if (info != null) {
                System.out.println(
                        "ID=" + info.getThreadId()
                                + " Name=" + info.getThreadName()
                                + " State=" + info.getThreadState()
                );
            }
        }
        Thread.sleep(100);
        Thread.getAllStackTraces()
                .keySet()
                .stream()
                .sorted(Comparator.comparingLong(Thread::threadId))
                .forEach(t ->
                        System.out.printf(
                                "id=%d name=%s state=%s daemon=%s%n",
                                t.threadId(),
                                t.getName(),
                                t.getState(),
                                t.isDaemon()
                        ));
        Thread deadlockChecker = new Thread(new DeadlockChecker());
        deadlockChecker.setDaemon(true);
        deadlockChecker.start();
        System.out.println("_detect_" + Detector.detectDeadLocks());

    }
}
