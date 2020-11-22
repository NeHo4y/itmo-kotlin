package com.github.neho4y.category.service

import com.github.neho4y.category.domain.Category
import com.github.neho4y.category.domain.repository.CategoryRepository
import com.github.neho4y.category.model.TopicCreationDto
import com.github.neho4y.common.exception.BasicException
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
internal class TopicServiceIntegrationTest {

    @Autowired
    private lateinit var topicService: TopicService

    @Autowired
    private lateinit var categoryRepository: CategoryRepository
    private var categoryId: Long = 0

    @BeforeEach
    fun generateTestData() {
        val category = Category("Test category")
        categoryRepository.save(category)
        categoryRepository.flush()
        categoryId = category.id
    }

    @Test
    fun `When add topic with the same description then fail`() {
        // given
        val topicCreationDto = TopicCreationDto("Test topic", categoryId)
        val sameCategory = topicCreationDto.copy()
        topicService.createTopic(topicCreationDto)

        // when
        val exception = assertThrows<BasicException> {
            topicService.createTopic(sameCategory)
        }

        // then
        val message = exception.message ?: ""
        assertTrue(message.contains("already exists"))
    }
}
