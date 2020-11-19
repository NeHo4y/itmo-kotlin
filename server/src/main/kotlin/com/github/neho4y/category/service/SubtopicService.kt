package com.github.neho4y.category.service

import com.github.neho4y.category.domain.Subtopic
import com.github.neho4y.category.model.SubtopicCreationDto
import com.github.neho4y.category.model.SubtopicDto

interface SubtopicService {
    fun getAllSubtopics(): List<Subtopic>
    fun createSubtopic(subtopicCreationDto: SubtopicCreationDto): Subtopic
    fun getSubtopic(id: Long): Subtopic
    fun updateSubtopic(subtopicDto: SubtopicDto)
    fun deleteSubtopic(id: Long): Subtopic
}
