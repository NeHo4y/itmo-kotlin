package com.github.neho4y.category.service

import com.github.neho4y.category.model.CategoryCreationDto
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
    fun `When add category with the same description then fail`() {

        // given
        val categoryCreationDto = CategoryCreationDto("Test category")
        val sameCategory = categoryCreationDto.copy()
        categoryService.createCategory(categoryCreationDto)

        // when
        val exception = assertThrows<Exception> {
            categoryService.createCategory(sameCategory)
        }

    }

}
