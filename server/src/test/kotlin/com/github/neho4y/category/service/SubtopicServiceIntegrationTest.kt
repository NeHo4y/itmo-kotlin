package com.github.neho4y.category.service

import com.github.neho4y.category.domain.Category
import com.github.neho4y.category.domain.Topic
import com.github.neho4y.category.domain.repository.CategoryRepository
import com.github.neho4y.category.domain.repository.TopicRepository
import com.github.neho4y.category.model.SubtopicCreationDto
import com.github.neho4y.common.exception.AlreadyExistsException
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
internal class SubtopicServiceIntegrationTest {

    @Autowired
    private lateinit var subtopicService: SubtopicService

    @Autowired
    private lateinit var topicRepository: TopicRepository
    private var topicId: Long = 0

    @Autowired
    private lateinit var categoryRepository: CategoryRepository
    private var categoryId: Long = 0

    @BeforeEach
    fun generateTestData() {
        val category = Category("Test category")
        categoryRepository.save(category)
        categoryRepository.flush()
        categoryId = category.id
        val topic = Topic("Test topic", categoryId)
        topicRepository.save(topic)
        topicRepository.flush()
        topicId = topic.id
    }

    @Test
    fun `When add subtopic with the same description then fail`() = runBlocking {
        // given
        val subtopicCreationDto = SubtopicCreationDto("Test subtopic", topicId)
        val sameCategory = subtopicCreationDto.copy()
        subtopicService.createSubtopic(subtopicCreationDto)

        // when
        val exception = assertThrows<AlreadyExistsException> {
            subtopicService.createSubtopic(sameCategory)
        }

        // then
        val message = exception.message ?: ""
        Assertions.assertTrue(message.contains("already exists"))
    }
}
