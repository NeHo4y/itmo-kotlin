package com.github.neho4y.category.service

import com.github.neho4u.shared.model.category.CategoryCreationDto
import com.github.neho4u.shared.model.category.CategoryDto

interface CategoryService {
    suspend fun getAllCategories(): List<CategoryDto>
    suspend fun createCategory(categoryCreationDto: CategoryCreationDto): CategoryDto
    suspend fun getCategory(id: Long): CategoryDto?
    suspend fun updateCategory(categoryDto: CategoryDto): CategoryDto
    suspend fun deleteCategory(id: Long): CategoryDto
}
