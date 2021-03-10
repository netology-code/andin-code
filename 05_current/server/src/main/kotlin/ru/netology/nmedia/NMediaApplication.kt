package ru.netology.nmedia

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.service.PostService

@SpringBootApplication
class NMediaApplication {
    @Bean
    fun runner(service: PostService) = CommandLineRunner {
        with(service) {
            save(
                Post(
                    id = 0,
                    author = "Netology",
                    authorAvatar = "netology.jpg",
                    content = "Привет, это новая Нетология!",
                    published = 0,
                    likedByMe = false,
                    likes = 0,
                )
            )
            save(
                Post(
                    id = 0,
                    author = "Сбер",
                    authorAvatar = "sber.jpg",
                    content = "Привет, это новый Сбер!",
                    published = 0,
                    likedByMe = false,
                    likes = 0,
                )
            )
            save(
                Post(
                    id = 0,
                    author = "Тинькофф",
                    authorAvatar = "tcs.jpg",
                    content = "Нам и так норм!",
                    published = 0,
                    likedByMe = false,
                    likes = 0,
                )
            )
        }
    }
}

fun main(args: Array<String>) {
    runApplication<NMediaApplication>(*args)
}
