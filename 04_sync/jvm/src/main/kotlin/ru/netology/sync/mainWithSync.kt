package ru.netology.sync

fun main() {
    var sum = 0
    val max = 1_000_000
    val sync = Any()

    val t1 = Thread {
        for (i in 1..max) {
            synchronized(sync) {
                sum++
            }
        }
    }
    val t2 = Thread {
        for (i in 1..max) {
            synchronized(sync) {
                sum++
            }
        }
    }
    val t3 = Thread {
        for (i in 1..max) {
            synchronized(sync) {
                sum++
            }
        }
    }

    t1.start()
    t2.start()
    t3.start()

    t1.join()
    t2.join()
    t3.join()

    println(sum)
}



