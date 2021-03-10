package ru.netology.nmedia.controller

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import ru.netology.nmedia.service.MediaService

@RestController
@RequestMapping("/api/media", "/api/slow/media")
class MediaController(private val service: MediaService) {
    @PostMapping
    fun save(@RequestParam file: MultipartFile) = service.saveMedia(file)
}