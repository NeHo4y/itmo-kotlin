package com.github.neho4u.shared.client

import com.github.neho4u.shared.model.follower.FollowerCreateDto
import com.github.neho4u.shared.model.follower.FollowerData
import com.github.neho4u.shared.model.follower.FollowerFilterDto
import io.ktor.client.*
import io.ktor.client.request.*

class FollowerClient(private val httpClient: HttpClient) {
    suspend fun getFilter(filter: FollowerFilterDto): List<FollowerData> {
        return httpClient.post("/feedback/follower/filter") {
            body = filter
        }
    }

    suspend fun add(creationDto: FollowerCreateDto): FollowerData {
        return httpClient.post("/feedback/follower") {
            body = creationDto
        }
    }

    suspend fun remove(creationDto: FollowerCreateDto) {
        return httpClient.post("/feedback/follower/remove") {
            body = creationDto
        }
    }
}
