package com.github.neho4u.shared.client

import com.github.neho4u.shared.model.feedback.*
import io.ktor.client.*
import io.ktor.client.request.*

class FeedbackClient(private val httpClient: HttpClient) {
    suspend fun get(id: Long): FeedbackDto {
        return httpClient.get("/feedback/$id")
    }

    suspend fun getFeed(filter: FeedbackFilter): List<FeedbackDto> {
        return httpClient.post("/feedback/feed") {
            body = filter
        }
    }

    suspend fun getFilter(feedbackFilter: FeedbackFilter): List<FeedbackDto> {
        return httpClient.post("/feedback/filter") {
            body = feedbackFilter
        }
    }

    suspend fun create(feedbackCreationDto: FeedbackCreationDto): FeedbackDto {
        return httpClient.post("/feedback") {
            body = feedbackCreationDto
        }
    }

    suspend fun update(id: Long, feedbackUpdate: FeedbackDto): FeedbackDto {
        return httpClient.post("/feedback/$id") {
            body = feedbackUpdate
        }
    }

    suspend fun updateStatus(id: Long, feedbackStatus: FeedbackStatus) {
        return httpClient.post("/feedback/$id/status") {
            body = feedbackStatus
        }
    }

    suspend fun updatePriority(id: Long, feedbackPriority: FeedbackPriority) {
        return httpClient.post("/feedback/$id/priority") {
            body = feedbackPriority
        }
    }
}
