package ru.netology.nmedia.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.nmedia.dto.PostDto
import ru.netology.nmedia.model.Post

@Entity
data class PostEntity(
    @PrimaryKey(autoGenerate = true)
    val localId: Long,
    val author: String,
    val authorAvatar: String,
    val content: String,
    val published: String,
    val likedByMe: Boolean,
    val likes: Int = 0,
    val serverId: Long? = null,
) {
    fun toDto() = PostDto(
        id = serverId ?: 0L,
        author = author,
        authorAvatar = authorAvatar,
        content = content,
        published = published,
        likedByMe = likedByMe,
        likes = likes,
    )

    fun toModel() = Post(localId, author, authorAvatar, content, published, likedByMe, likes, serverId)

    companion object {
        fun fromDto(dto: PostDto) =
            PostEntity(
                localId = 0,
                author = dto.author,
                authorAvatar = dto.authorAvatar,
                content = dto.content,
                published = dto.published,
                likedByMe = dto.likedByMe,
                likes = dto.likes,
                serverId = dto.id,
            )

        fun fromModel(model: Post) =
            with(model) {
                PostEntity(
                    localId = localId,
                    author = author,
                    authorAvatar = authorAvatar,
                    content = content,
                    published = published,
                    likedByMe = likedByMe,
                    likes = likes,
                    serverId = serverId,
                )
            }
    }
}

fun List<PostEntity>.toDto(): List<PostDto> = map(PostEntity::toDto)

fun List<PostEntity>.toModel(): List<Post> = map(PostEntity::toModel)

fun List<PostDto>.toEntity(): List<PostEntity> = map(PostEntity::fromDto)
