package com.github.neho4y.category.domain.repository

import com.github.neho4y.category.domain.Subtopic
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface SubtopicRepository : JpaRepository<Subtopic, Long> {
    fun existsByDescriptionAndIsDeletedFalseAndTopicId(description: String, topicId: Long): Boolean

    fun findAllByIsDeletedFalse(): List<Subtopic>

    fun findByIdAndIsDeletedFalse(id: Long): Optional<Subtopic>
}
