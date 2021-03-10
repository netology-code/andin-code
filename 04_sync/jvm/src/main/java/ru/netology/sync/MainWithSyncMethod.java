package ru.netology.sync;

public class MainWithSyncMethod {
  private static long sum;

  public static void main(String[] args) throws InterruptedException {
    final var max = 1_000_000;
    final var sync = new Object();

    final var runnable = new Runnable() {
      @Override
      public void run() {
        synchronized (this) {
          for (var i = 0; i < max; i++) {
            sum++;
          }
        }
      }
    };

    final var t1 = new Thread(runnable);
    final var t2 = new Thread(runnable);
    final var t3 = new Thread(runnable);

    t1.start();
    t2.start();
    t3.start();

    t1.join();
    t2.join();
    t3.join();

    System.out.println(sum);
  }
}
