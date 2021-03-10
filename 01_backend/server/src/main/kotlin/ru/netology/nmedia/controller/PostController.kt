package ru.netology.nmedia.controller

import org.springframework.web.bind.annotation.*
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.service.PostService

@RestController
@RequestMapping("/api/posts", "/api/slow/posts")
class PostController(private val service: PostService) {
    @GetMapping
    fun getAll() = service.getAll()

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long) = service.getById(id)

    @PostMapping
    fun save(@RequestBody dto: Post) = service.save(dto)

    @DeleteMapping("/{id}")
    fun removeById(@PathVariable id: Long) = service.removeById(id)
}