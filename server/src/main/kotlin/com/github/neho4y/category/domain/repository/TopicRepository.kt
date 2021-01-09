package com.github.neho4y.category.domain.repository

import com.github.neho4y.category.domain.Topic
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface TopicRepository : JpaRepository<Topic, Long> {
    fun existsByDescriptionAndIsDeletedFalseAndCategoryId(description: String, categoryId: Long): Boolean

    fun findAllByIsDeletedFalse(): List<Topic>

    fun findByIdAndIsDeletedFalse(id: Long): Optional<Topic>
}
