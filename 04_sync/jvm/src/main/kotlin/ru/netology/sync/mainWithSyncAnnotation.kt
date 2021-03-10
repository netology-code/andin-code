package ru.netology.sync

fun main() {
    var sum = 0
    val max = 1_000_000

    val runnable = object : Runnable {
        @Synchronized
        override fun run() {
            for (i in 1..max) {
                sum++
            }
        }
    }

    val t1 = Thread(runnable)
    val t2 = Thread(runnable)
    val t3 = Thread(runnable)

    t1.start()
    t2.start()
    t3.start()

    t1.join()
    t2.join()
    t3.join()

    println(sum)
}



