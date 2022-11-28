package ru.netology.nmedia.service

import com.github.javafaker.Faker
import jakarta.transaction.Transactional
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import ru.netology.nmedia.dto.Post

@Service
@Transactional
class ScheduledPostGeneratorService(
    private val userService: UserService,
    private val postService: PostService,
) {
    private val faker = Faker()

    @Scheduled(initialDelay = 60 * 1000, fixedRate = 5 * 60 * 1000)
    fun generate() {
        val user = userService.getByLogin("got")

        postService.saveInitial(
            Post(
                id = 0,
                authorId = user.id,
                author = user.name,
                authorAvatar = user.avatar,
                content = faker.gameOfThrones().quote(),
                published = 0,
                likedByMe = false,
                likes = 0,
            )
        )
    }
}