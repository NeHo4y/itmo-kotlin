package com.github.neho4u.shared.client

import com.github.neho4u.shared.model.comment.CommentCreationDto
import com.github.neho4u.shared.model.comment.CommentDto
import io.ktor.client.*
import io.ktor.client.request.*

class CommentClient(private val httpClient: HttpClient) {
    suspend fun getForFeedback(feedbackId: Long): List<CommentDto> {
        return httpClient.get("/comments/$feedbackId")
    }

    suspend fun add(commentCreationDto: CommentCreationDto): Long {
        return httpClient.post("/comments/addComment") {
            body = commentCreationDto
        }
    }

    suspend fun setDeleted(commentId: Long) {
        return httpClient.put("/comments/delete/$commentId")
    }

    suspend fun setRead(commentId: Long) {
        return httpClient.put("/comments/read/$commentId")
    }
}
