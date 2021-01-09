package com.github.neho4y.category.service

import com.github.neho4y.category.domain.Category
import com.github.neho4y.category.model.CategoryCreationDto
import com.github.neho4y.category.model.CategoryDto

interface CategoryService {
    suspend fun getAllCategories(): List<Category>
    suspend fun createCategory(categoryCreationDto: CategoryCreationDto): Category
    suspend fun getCategory(id: Long): Category?
    suspend fun updateCategory(categoryDto: CategoryDto): Category
    suspend fun deleteCategory(id: Long): Category
}
