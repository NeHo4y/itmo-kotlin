package com.github.neho4y.category.service.impl

import com.github.neho4u.shared.model.category.CategoryCreationDto
import com.github.neho4u.shared.model.category.CategoryDto
import com.github.neho4y.category.controller.toDto
import com.github.neho4y.category.domain.Category
import com.github.neho4y.category.domain.repository.CategoryRepository
import com.github.neho4y.category.service.CategoryService
import com.github.neho4y.common.exception.AlreadyExistsException
import com.github.neho4y.common.exception.NotFoundException
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.stream.Collectors

@Service
class CategoryServiceImpl(
    private val categoryRepository: CategoryRepository
) : CategoryService {

    companion object {
        private val log = LoggerFactory.getLogger(this::class.java)
    }

    override suspend fun createCategory(categoryCreationDto: CategoryCreationDto): CategoryDto {
        if (categoryRepository.existsByDescriptionAndIsDeletedFalse(categoryCreationDto.description)) {
            throw AlreadyExistsException("Category ${categoryCreationDto.description} already exists")
        }
        val newCategory = Category(categoryCreationDto.description)
        val savedCategory = categoryRepository.save(newCategory)
        log.info("Category ${savedCategory.description} is saved under id ${savedCategory.id}")
        return savedCategory.toDto()
    }

    override suspend fun getAllCategories(): List<CategoryDto> {
        return categoryRepository.findAllByIsDeletedFalse().stream()
            .map { it.toDto() }
            .collect(Collectors.toList())
    }

    override suspend fun getCategory(id: Long): CategoryDto =
        categoryRepository.findById(id).orElseThrow { NotFoundException("Unable to find category") }.toDto()

    override suspend fun updateCategory(categoryDto: CategoryDto): CategoryDto {
        val category =
            categoryRepository.findById(categoryDto.id).orElseThrow { NotFoundException("Unable to find category") }
        if (categoryRepository.existsByDescriptionAndIsDeletedFalse(categoryDto.description)) {
            val msg = "Category ${category.description} already exists"
            log.error(msg)
            throw AlreadyExistsException(msg)
        }
        category.description = categoryDto.description
        val updatedCategory = categoryRepository.save(category)
        log.info("Category ${category.id} is updated with description ${categoryDto.description}")
        return updatedCategory.toDto()
    }

    override suspend fun deleteCategory(id: Long): CategoryDto {
        val category = categoryRepository.findById(id).orElseThrow { NotFoundException("Unable to find category") }
        return categoryRepository.save(category.copy(isDeleted = true)).toDto()
    }
}
