package ru.netology.sync;

public class MainWithWorkerThread {
  private static long sum;

  public static void main(String[] args) throws InterruptedException {
    final var max = 1_000_000;
    final var sync = new Object();

    class WorkerThread extends Thread {
      @Override
      public void run() {
        for (var i = 0; i < max; i++) {
          synchronized (this) {
            sum++;
          }
        }
      }
    }

    final var t1 = new WorkerThread();
    final var t2 = new WorkerThread();
    final var t3 = new WorkerThread();

    t1.start();
    t2.start();
    t3.start();

    t1.join();
    t2.join();
    t3.join();

    System.out.println(sum);
  }
}
