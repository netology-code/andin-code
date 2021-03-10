package ru.netology.nmedia.service

import org.apache.tika.Tika
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.util.MimeTypeUtils
import org.springframework.util.ResourceUtils
import org.springframework.web.multipart.MultipartFile
import ru.netology.nmedia.dto.Media
import ru.netology.nmedia.exception.BadContentTypeException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*

@Service
class MediaService(@Value("\${app.media-location}") private val mediaLocation: String) {
    private val path = ResourceUtils.getFile(mediaLocation).toPath()
    private val tika = Tika()

    init {
        Files.createDirectories(path)
    }

    fun saveMedia(file: MultipartFile): Media = save("media", file)

    fun saveAvatar(file: MultipartFile): Media = save("avatars", file)

    fun save(folder: String, file: MultipartFile): Media {
        val mediaType: String = tika.detect(file.inputStream)
        val id = UUID.randomUUID().toString() + when (mediaType) {
            MimeTypeUtils.IMAGE_JPEG_VALUE -> ".jpg"
            MimeTypeUtils.IMAGE_PNG_VALUE -> ".png"
            else -> throw BadContentTypeException()
        }
        file.transferTo(path.resolve(Paths.get(folder, id)))
        return Media(id)
    }
}
