package com.github.neho4y.comment.controller

import com.github.neho4u.shared.model.comment.CommentCreationDto
import com.github.neho4u.shared.model.comment.CommentDto
import com.github.neho4y.comment.service.CommentService
import com.github.neho4y.user.domain.User
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/comments")
class CommentController(private val commentService: CommentService) {
    @GetMapping("/{id}")
    suspend fun getCommentsForFeedback(@PathVariable id: Long): List<CommentDto> {
        return commentService.getComments(id)
    }

    @PostMapping("/addComment")
    suspend fun addCommentToFeedback(
        @AuthenticationPrincipal user: User,
        @RequestBody commentCreationDto: CommentCreationDto
    ): Long {
        return commentService.addComment(user.id, commentCreationDto)
    }

    @PutMapping("/delete/{commentId}")
    suspend fun setCommentToDeleted(@PathVariable commentId: Long) {
        commentService.markDeleted(commentId)
    }

    @PutMapping("/read/{commentId}")
    suspend fun setCommentToRead(@PathVariable commentId: Long) {
        commentService.markRead(commentId)
    }
}
