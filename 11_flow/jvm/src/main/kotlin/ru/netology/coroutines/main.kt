package ru.netology.coroutines

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking

fun main() {
    runBlocking {
        val values = flow {
            repeat(10) {
                delay(1000L)
                println("emit $it")
                emit(it)
            }
        }
        values.collect { println(it) }
    }
}
