package com.github.neho4y.category.service

import com.github.neho4y.category.domain.Subtopic
import com.github.neho4y.category.model.SubtopicCreationDto
import com.github.neho4y.category.model.SubtopicDto

interface SubtopicService {
    suspend fun getAllSubtopics(): List<Subtopic>
    suspend fun createSubtopic(subtopicCreationDto: SubtopicCreationDto): Subtopic
    suspend fun getSubtopic(id: Long): Subtopic
    suspend fun updateSubtopic(subtopicDto: SubtopicDto)
    suspend fun deleteSubtopic(id: Long): Subtopic
}
