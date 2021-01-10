package com.github.neho4y.comment.controller

import com.github.neho4u.shared.model.comment.CommentCreationDto
import com.github.neho4u.shared.model.comment.CommentDto
import com.github.neho4y.comment.service.CommentService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/comments")
class CommentController(private val commentService: CommentService) {
    @GetMapping("/{id}")
    suspend fun getCommentsForFeedback(@PathVariable id: Long): List<CommentDto> {
        return commentService.getComments(id).map { comment -> comment.toCommentDto() }
    }

    @PostMapping("/addComment")
    suspend fun addCommentToFeedback(@RequestBody commentCreationDto: CommentCreationDto): Long {
        return commentService.addComment(commentCreationDto)
    }

    @GetMapping("/delete/{commentId}")
    suspend fun setCommentToDeleted(@PathVariable commentId: Long) {
        commentService.markDeleted(commentId)
    }

    @GetMapping("/read/{commentId}")
    suspend fun setCommentToRead(@PathVariable commentId: Long) {
        commentService.markRead(commentId)
    }
}
