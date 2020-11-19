package com.github.neho4y.category.domain.repository

import com.github.neho4y.category.domain.Category
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface CategoryRepository : JpaRepository<Category, Long> {

    fun existsByDescriptionAndIsDeletedFalse(description: String): Boolean

    fun findAllByIsDeletedFalse(): List<Category>

    fun findByIdAndIsDeletedFalse(id: Long): Optional<Category>
}
