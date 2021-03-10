package ru.netology.nmedia.controller

import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.service.PostService

@RestController
@RequestMapping("/api/posts", "/api/slow/posts")
class PostController(private val service: PostService) {
    @GetMapping
    // TODO: uncomment for 500 status code generation
    // @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun getAll() = service.getAll()

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long) = service.getById(id)

    @GetMapping("/{id}/newer")
    fun getNewer(@PathVariable id: Long) = service.getNewer(id)

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    fun save(@RequestBody dto: Post) = service.save(dto)

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    fun removeById(@PathVariable id: Long) = service.removeById(id)

    @PostMapping("/{id}/likes")
    @PreAuthorize("hasRole('USER')")
    fun likeById(@PathVariable id: Long) = service.likeById(id)

    @DeleteMapping("/{id}/likes")
    @PreAuthorize("hasRole('USER')")
    fun unlikeById(@PathVariable id: Long) = service.unlikeById(id)

}