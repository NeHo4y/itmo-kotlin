package com.github.neho4y.category.service

import com.github.neho4u.shared.model.category.CategoryCreationDto
import com.github.neho4y.common.exception.AlreadyExistsException
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
internal class CategoryServiceIntegrationTest {

    @Autowired
    private lateinit var categoryService: CategoryService

    @Test
    fun `When add category with the same description then fail`() = runBlocking {
        // given
        val categoryCreationDto = CategoryCreationDto("Test category")
        val sameCategory = categoryCreationDto.copy()
        categoryService.createCategory(categoryCreationDto)

        // when
        val exception = assertThrows<AlreadyExistsException> {
            categoryService.createCategory(sameCategory)
        }

        // then
        val message = exception.message ?: ""
        Assertions.assertTrue(message.contains("already exists"))
    }
}
