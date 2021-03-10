package ru.netology.nmedia.service

import com.github.javafaker.Faker
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import ru.netology.nmedia.dto.Post

@Service
class ScheduledPostGeneratorService(
    private val postService: PostService,
) {
    private val faker = Faker()
    @Scheduled(fixedRate = 60 * 1000)
    fun generate() {
        postService.saveInitial(
            Post(
                id = 0,
                author = "Game of Thrones",
                authorAvatar = "got.jpg",
                content = faker.gameOfThrones().quote(),
                published = 0,
                likedByMe = false,
                likes = 0,
            )
        )
    }
}