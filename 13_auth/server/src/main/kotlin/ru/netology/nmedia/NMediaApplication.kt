package ru.netology.nmedia

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.util.ResourceUtils
import ru.netology.nmedia.dto.Attachment
import ru.netology.nmedia.dto.Comment
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.enumeration.AttachmentType
import ru.netology.nmedia.service.CommentService
import ru.netology.nmedia.service.PostService
import ru.netology.nmedia.service.UserService

@EnableScheduling
@SpringBootApplication
class NMediaApplication {
    @Bean
    fun runner(
        userService: UserService,
        postService: PostService,
        commentService: CommentService,
        @Value("\${app.media-location}") mediaLocation: String,
    ) = CommandLineRunner {
        ResourceUtils.getFile("classpath:static").copyRecursively(
            ResourceUtils.getFile(mediaLocation),
            true,
        )

        val netology = userService.create(login = "netology", pass = "secret", name = "Netology", avatar = "netology.jpg")
        val sber = userService.create(login = "sber", pass = "secret", name = "Сбер", avatar = "sber.jpg")
        val tcs = userService.create(login = "tcs", pass = "secret", name = "Тинькофф", avatar = "tcs.jpg")
        val got = userService.create(login = "got", pass = "secret", name = "Game of Thrones", avatar = "got.jpg")
        val student = userService.create(login = "student", pass = "secret", name = "Студент", avatar = "netology.jpg")

        userService.saveInitialToken(student.id, "x-token")

        val firstPost = postService.saveInitial(
            Post(
                id = 0,
                authorId = netology.id,
                author = netology.name,
                authorAvatar = netology.avatar,
                content = "Привет, это новая Нетология!",
                published = 0,
                likedByMe = false,
                likes = 0,
            )
        )
        val secondPost = postService.saveInitial(
            Post(
                id = 0,
                authorId = sber.id,
                author = sber.name,
                authorAvatar = sber.avatar,
                content = "Привет, это новый Сбер!",
                published = 0,
                likedByMe = false,
                likes = 0,
            )
        )
        val thirdPost = postService.saveInitial(
            Post(
                id = 0,
                authorId = tcs.id,
                author = tcs.name,
                authorAvatar = tcs.avatar,
                content = "Нам и так норм!",
                published = 0,
                likedByMe = false,
                likes = 0,
            )
        )
        val fourthPost = postService.saveInitial(
            Post(
                id = 0,
                authorId = netology.id,
                author = netology.name,
                authorAvatar = netology.avatar,
                content = "Подкасты любят за возможность проводить время с пользой и слушать познавательные лекции или беседы во время прогулок или домашних дел. Интересно, что запустить свой подкаст и обсуждать интересные темы может любой.",
                published = 0,
                likedByMe = false,
                likes = 0,
                attachment = Attachment(
                    url = "podcast.jpg",
                    description = "Как запустить свой подкаст: подборка статей",
                    type = AttachmentType.IMAGE,
                ),
            )
        )
        val fifthPost = postService.saveInitial(
            Post(
                id = 0,
                authorId = sber.id,
                author = sber.name,
                authorAvatar = sber.avatar,
                content = "Появился новый способ мошенничества \uD83D\uDE21 Злоумышленники звонят от имени банка и говорят, что для клиента выпущена новая, особо защищённая карта, которую можно добавить в приложение Кошелёк на смартфоне. Под диктовку мошенника человек привязывает к Кошельку его карту, причём указывает своё имя. Если карту пополнить, деньги уйдут мошеннику.\n\nДело в том, что в Кошелёк можно добавить любую, даже чужую карту, а имя поставить какое угодно. Но чужая банковская карта не будет отображаться, например, в СберБанк Онлайн.",
                published = 0,
                likedByMe = false,
                likes = 0,
                attachment = Attachment(
                    url = "sbercard.jpg",
                    description = "Предлагают новую карту? Проверьте, не мошенничество ли это!",
                    type = AttachmentType.IMAGE,
                ),
            )
        )
        val sixthPost = postService.saveInitial(
            Post(
                id = 0,
                authorId = student.id,
                author = student.name,
                authorAvatar = student.avatar,
                content = "Just demo post for check authentication",
                published = 0,
                likedByMe = false,
                likes = 0,
            )
        )
        with(commentService) {
            saveInitial(
                Comment(
                    id = 0,
                    postId = firstPost.id,
                    authorId = netology.id,
                    author = netology.name,
                    authorAvatar = netology.avatar,
                    content = "Отлично!",
                    published = 0,
                    likedByMe = false,
                    likes = 0,
                )
            )
            saveInitial(
                Comment(
                    id = 0,
                    postId = firstPost.id,
                    authorId = sber.id,
                    author = sber.name,
                    authorAvatar = sber.avatar,
                    content = "Мы тоже обновились!",
                    published = 0,
                    likedByMe = false,
                    likes = 0,
                )
            )
            saveInitial(
                Comment(
                    id = 0,
                    postId = secondPost.id,
                    authorId = netology.id,
                    author = netology.name,
                    authorAvatar = netology.avatar,
                    content = "Новый логотип прекрасен!",
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
