package com.github.neho4y.feedback.service

import com.github.neho4y.category.domain.Category
import com.github.neho4y.category.domain.Subtopic
import com.github.neho4y.category.domain.Topic
import com.github.neho4y.category.domain.repository.CategoryRepository
import com.github.neho4y.category.domain.repository.SubtopicRepository
import com.github.neho4y.category.domain.repository.TopicRepository
import com.github.neho4y.feedback.domain.FeedbackPriority
import com.github.neho4y.feedback.domain.FeedbackStatus
import com.github.neho4y.feedback.model.FeedbackCreationDto
import com.github.neho4y.feedback.model.FeedbackDto
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
internal class FeedbackServiceIntegrationTest {

    @Autowired
    private lateinit var feedbackService: FeedbackService

    @Autowired
    private lateinit var subtopicRepository: SubtopicRepository
    private var subtopicId: Long = 0

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
        val subtopic = Subtopic("Test subtopic", topicId)
        subtopicRepository.save(subtopic)
        subtopicRepository.flush()
        subtopicId = subtopic.id
    }

    @Test
    fun `positive update feedback test`(): Unit = runBlocking {
        // given

        val feedbackCreationDto = FeedbackCreationDto(
            "Test feedback",
            categoryId, topicId, subtopicId, "test comment"
        )
        val savedFeedback = feedbackService.createFeedback(feedbackCreationDto)

        val feedbackDto = FeedbackDto(
            savedFeedback.id,
            "Test new header",
            null,
            null,
            null,
            FeedbackStatus.OPEN,
            FeedbackPriority.GODLIKE
        )

        val updatedFeedback = feedbackService.updateFeedback(feedbackDto)

        assertThat(updatedFeedback.header).isEqualTo(feedbackDto.header)
        assertThat(updatedFeedback.categoryId).isEqualTo(feedbackCreationDto.categoryId)
        assertThat(updatedFeedback.topicId).isEqualTo(feedbackCreationDto.topicId)
        assertThat(updatedFeedback.subtopicId).isEqualTo(feedbackCreationDto.subtopicId)
        assertThat(updatedFeedback.status).isEqualTo(feedbackDto.status)
        assertThat(updatedFeedback.priority).isEqualTo(feedbackDto.priority)
    }
}
