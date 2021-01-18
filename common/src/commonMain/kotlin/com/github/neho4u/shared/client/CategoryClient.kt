package com.github.neho4u.shared.client

import com.github.neho4u.shared.model.category.CategoryDto
import com.github.neho4u.shared.model.category.SubtopicDto
import com.github.neho4u.shared.model.category.TopicDto
import io.ktor.client.*
import io.ktor.client.request.*

class CategoryClient(private val httpClient: HttpClient) {
    suspend fun getCategories(): List<CategoryDto> {
        return httpClient.get("/category/all")
    }

    suspend fun getTopics(categoryId: Long): List<TopicDto> {
        return httpClient.get("/category/$categoryId/topics")
    }

    suspend fun getTopics(): List<TopicDto> {
        return httpClient.get("/category/topic/all")
    }

    suspend fun getSubtopics(topicId: Long): List<SubtopicDto> {
        return httpClient.get("/category/topic/$topicId/subtopics")
    }

    suspend fun getSubtopics(): List<SubtopicDto> {
        return httpClient.get("/category/topic/subtopic/all")
    }
}
