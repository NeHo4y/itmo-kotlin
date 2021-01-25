package com.github.neho4y.feedback.service

import com.github.neho4u.shared.model.category.CategoryDto
import com.github.neho4u.shared.model.category.SubtopicDto
import com.github.neho4u.shared.model.category.TopicDto
import com.github.neho4u.shared.model.feedback.FeedbackCreationDto
import com.github.neho4u.shared.model.feedback.FeedbackDto
import com.github.neho4u.shared.model.feedback.FeedbackPriority
import com.github.neho4u.shared.model.feedback.FeedbackStatus
import com.github.neho4y.category.domain.Category
import com.github.neho4y.category.domain.Subtopic
import com.github.neho4y.category.domain.Topic
import com.github.neho4y.category.domain.repository.CategoryRepository
import com.github.neho4y.category.domain.repository.SubtopicRepository
import com.github.neho4y.category.domain.repository.TopicRepository
import com.github.neho4y.category.service.CategoryService
import com.github.neho4y.category.service.SubtopicService
import com.github.neho4y.category.service.TopicService
import com.github.neho4y.user.createDefaultUser
import com.github.neho4y.user.model.toUserData
import com.github.neho4y.user.service.UserService
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.stub
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.toKotlinLocalDateTime
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.annotation.DirtiesContext
import java.time.LocalDateTime
import java.util.*

private const val USER_ID = 1L
private const val CATEGORY_ID = 3L
private const val TOPIC_ID = 5L
private const val SUBTOPIC_ID = 9L

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
internal class FeedbackServiceIntegrationTest {

    @Autowired
    private lateinit var feedbackService: FeedbackService

    @MockBean
    private lateinit var userService: UserService
    @MockBean
    private lateinit var categoryService: CategoryService
    @MockBean
    private lateinit var topicService: TopicService
    @MockBean
    private lateinit var subtopicService: SubtopicService

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
        userService.stub {
            onBlocking { findById(USER_ID) }
                .doReturn(createDefaultUser())
        }
        categoryService.stub {
            onBlocking { getCategory(CATEGORY_ID) }
                .doReturn(CategoryDto(CATEGORY_ID, "Test category"))
        }
        topicService.stub {
            onBlocking { getTopic(TOPIC_ID) }
                .doReturn(TopicDto(TOPIC_ID, "Test topic", CATEGORY_ID))
        }
        subtopicService.stub {
            onBlocking { getSubtopic(SUBTOPIC_ID) }
                .doReturn(SubtopicDto(SUBTOPIC_ID, "Test subtopic", TOPIC_ID))
        }
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
        val user = createDefaultUser().copy(id = USER_ID)
        val userData = user.toUserData()

        val feedbackCreationDto = FeedbackCreationDto(
            "Test feedback",
            categoryId, topicId, subtopicId, "test comment"
        )
        val savedFeedback = feedbackService.createFeedback(user.id, feedbackCreationDto)

        val feedbackDto = FeedbackCreationDto(
            "Test new header",
            categoryId,
            topicId,
            subtopicId,
            "comment"
        )

        val updatedFeedback = feedbackService.updateFeedback(feedbackDto, savedFeedback.id)

        assertThat(updatedFeedback.header).isEqualTo(feedbackDto.header)
    }
}
