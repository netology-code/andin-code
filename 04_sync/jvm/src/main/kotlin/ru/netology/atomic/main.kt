package ru.netology.atomic

import java.util.concurrent.atomic.AtomicLong

fun main() {
    val sum = AtomicLong(0)
    val max = 1_000_000


    val t1 = Thread {
        for (i in 1..max) {
            sum.incrementAndGet()
        }
    }
    val t2 = Thread {
        for (i in 1..max) {
            sum.incrementAndGet()
        }
    }
    val t3 = Thread {
        for (i in 1..max) {
            sum.incrementAndGet()
        }
    }

    t1.start()
    t2.start()
    t3.start()

    t1.join()
    t2.join()
    t3.join()

    println(sum.get())
}
