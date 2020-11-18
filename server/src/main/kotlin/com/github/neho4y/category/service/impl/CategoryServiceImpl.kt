package com.github.neho4y.category.service.impl

import com.github.neho4y.category.domain.Category
import com.github.neho4y.category.domain.repository.CategoryRepository
import com.github.neho4y.category.model.CategoryCreationDto
import com.github.neho4y.category.model.CategoryDto
import com.github.neho4y.category.service.CategoryService
import com.github.neho4y.common.exception.BasicException
import com.github.neho4y.common.exception.NotFoundException
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service


@Service
class CategoryServiceImpl(
    private val categoryRepository: CategoryRepository
) : CategoryService {

    private val log = LoggerFactory.getLogger(this::class.java)

    override fun createCategory(categoryCreationDto: CategoryCreationDto): Category {
        if (categoryRepository.existsByDescriptionAndIsDeletedFalse(categoryCreationDto.description)) {
            throw BasicException("Category ${categoryCreationDto.description} already exists")
        }
        val newCategory = Category(categoryCreationDto.description)
        val savedCategory = categoryRepository.save(newCategory)
        log.info("Category ${savedCategory.description} is saved under id ${savedCategory.id}")
        return savedCategory
    }

    override fun getAllCategories(): List<Category> {
        return categoryRepository.findAllByIsDeletedFalse()
    }

    override fun getCategory(id: Long): Category =
        categoryRepository.findById(id).orElseThrow { NotFoundException("Unable to find category") }

    override fun updateCategory(categoryDto: CategoryDto): Category {
        val category =
            categoryRepository.findById(categoryDto.id).orElseThrow { NotFoundException("Unable to find category") }
        if (categoryRepository.existsByDescriptionAndIsDeletedFalse(categoryDto.description)) {
            val msg = "Category ${category.description} already exists"
            log.error(msg)
            throw BasicException(msg)
        }
        category.description = categoryDto.description
        val updatedCategory = categoryRepository.save(category)
        log.info("Category ${category.id} is updated with description ${categoryDto.description}")
        return updatedCategory
    }

    override fun deleteCategory(id: Long): Category {
        val category = categoryRepository.findById(id).orElseThrow { NotFoundException("Unable to find category") }
        return categoryRepository.save(category.copy(isDeleted = true))
    }

}
