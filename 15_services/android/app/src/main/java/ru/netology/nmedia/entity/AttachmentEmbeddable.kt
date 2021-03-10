package ru.netology.nmedia.entity

import ru.netology.nmedia.dto.Attachment
import ru.netology.nmedia.enumeration.AttachmentType

data class AttachmentEmbeddable(
    var url: String,
    var type: AttachmentType,
) {
    fun toDto() = Attachment(url, type)

    companion object {
        fun fromDto(dto: Attachment?) = dto?.let {
            AttachmentEmbeddable(it.url, it.type)
        }
    }
}


