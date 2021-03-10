package ru.netology.nmedia.controller

import org.springframework.web.bind.annotation.*
import ru.netology.nmedia.dto.PushMessage
import ru.netology.nmedia.service.PushService

/**
 * Use this for only testing purposes
 */
@RestController
@RequestMapping("/api/pushes")
class PushController(private val service: PushService) {
    @PostMapping
    fun send(@RequestParam token: String, @RequestBody message: PushMessage) = service.send(token, message)
}