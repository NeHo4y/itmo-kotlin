package com.github.neho4y.category.service

import com.github.neho4y.category.domain.Category
import com.github.neho4y.category.model.CategoryCreationDto
import com.github.neho4y.category.model.CategoryDto

interface CategoryService {
    fun getAllCategories(): List<Category>
    fun createCategory(categoryCreationDto: CategoryCreationDto): Category
    fun getCategory(id: Long): Category?
    fun updateCategory(categoryDto: CategoryDto): Category
    fun deleteCategory(id: Long): Category
}