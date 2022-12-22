package ru.netology.nmedia

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class NMediaApplication

fun main(args: Array<String>) {
    runApplication<NMediaApplication>(*args)
}
