package com.github.neho4y.category.service.impl

import com.github.neho4y.category.domain.Category
import com.github.neho4y.category.domain.repository.CategoryRepository
import com.github.neho4y.category.model.CategoryCreationDto
import com.github.neho4y.category.model.CategoryDto
import com.github.neho4y.category.service.CategoryService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.lang.RuntimeException


@Service
class CategoryServiceImpl(
    private val categoryRepository: CategoryRepository
) : CategoryService {

    private val log = LoggerFactory.getLogger(this::class.java)

    override fun createCategory(categoryCreationDto: CategoryCreationDto): Category {
        if (categoryRepository.existsByDescriptionAndIsDeletedFalse(categoryCreationDto.description)) {
            throw RuntimeException()
        }
        val newCategory = Category(categoryCreationDto.description)
        val savedCategory = categoryRepository.save(newCategory)
        log.info("Category ${savedCategory.description} is saved under id ${savedCategory.id}")
        return savedCategory
    }

    override fun getAllCategories(): List<Category> {
        return categoryRepository.findAllByIsDeletedFalse()
    }

    override fun getCategory(id: Long) = categoryRepository.findById(id).orElseThrow()

    override fun updateCategory(categoryDto: CategoryDto): Category {
        val category = categoryRepository.findById(categoryDto.id).orElseThrow()
        if (categoryRepository.existsByDescriptionAndIsDeletedFalse(categoryDto.description)) {
            log.info("Category ${category.description} already exists")
            throw RuntimeException()
        }
        category.description = categoryDto.description
        val updatedCategory = categoryRepository.save(category)
        log.info("Category ${category.id} is updated with description ${categoryDto.description}")
        return updatedCategory
    }

    override fun deleteCategory(id: Long): Category {
        val category = categoryRepository.findById(id).orElseThrow()
        return categoryRepository.save(category.copy(isDeleted = true))
    }

}
