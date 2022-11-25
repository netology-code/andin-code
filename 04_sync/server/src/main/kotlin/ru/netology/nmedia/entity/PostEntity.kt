package ru.netology.nmedia.entity

import ru.netology.nmedia.dto.Post
import jakarta.persistence.Id
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType

@Entity
data class PostEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Long,
    var author: String,
    var content: String,
    var published: Long,
    var likedByMe: Boolean,
    var likes: Int = 0,
) {
    fun toDto() = Post(id, author, content, published, likedByMe, likes)

    companion object {
        fun fromDto(dto: Post) = PostEntity(dto.id, dto.author, dto.content, dto.published, dto.likedByMe, dto.likes)
    }
}