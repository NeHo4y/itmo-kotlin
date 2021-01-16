package com.github.neho4y.category.service

import com.github.neho4u.shared.model.category.SubtopicCreationDto
import com.github.neho4u.shared.model.category.SubtopicDto

interface SubtopicService {
    suspend fun getAllSubtopics(): List<SubtopicDto>
    suspend fun createSubtopic(subtopicCreationDto: SubtopicCreationDto): SubtopicDto
    suspend fun getSubtopic(id: Long): SubtopicDto
    suspend fun updateSubtopic(subtopicDto: SubtopicDto)
    suspend fun deleteSubtopic(id: Long): SubtopicDto
    suspend fun getSubtopicByTopicId(categoryId: Long): List<SubtopicDto>
}
