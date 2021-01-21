package com.github.neho4y.comment.service

import com.github.neho4u.shared.model.category.CategoryDto
import com.github.neho4u.shared.model.category.SubtopicDto
import com.github.neho4u.shared.model.category.TopicDto
import com.github.neho4u.shared.model.feedback.FeedbackCreationDto
import com.github.neho4y.category.service.CategoryService
import com.github.neho4y.category.service.SubtopicService
import com.github.neho4y.category.service.TopicService
import com.github.neho4y.comment.assertEquals
import com.github.neho4y.comment.createDefaultCommentCreationDto
import com.github.neho4y.comment.createDefaultCommentDto
import com.github.neho4y.feedback.domain.Feedback
import com.github.neho4y.feedback.domain.repository.FeedbackRepository
import com.github.neho4y.feedback.service.FeedbackService
import com.github.neho4y.user.createDefaultUser
import com.github.neho4y.user.model.toUserData
import com.github.neho4y.user.service.UserService
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.stub
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.annotation.DirtiesContext
import java.time.LocalDateTime

private const val USER_ID = 1L
private const val CATEGORY_ID = 1L
private const val TOPIC_ID = 2L
private const val SUBTOPIC_ID = 3L

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
internal class CommentServiceIntegrationTest {
    @Autowired
    private lateinit var commentService: CommentService

    @Autowired
    private lateinit var feedbackService: FeedbackService
    @MockBean
    private lateinit var categoryService: CategoryService
    @MockBean
    private lateinit var topicService: TopicService
    @MockBean
    private lateinit var subtopicService: SubtopicService

    @MockBean
    private lateinit var userService: UserService

    @Autowired
    private lateinit var feedbackRepository: FeedbackRepository
    private var feedbackId: Long = 0

    @BeforeEach
    fun createCommentService() = runBlocking {
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
        val feedbackCreationDto = FeedbackCreationDto(
            "Test feedback",
            CATEGORY_ID, TOPIC_ID, SUBTOPIC_ID, "test comment"
        )
        val feedbackEntity = Feedback(
            "Test feedback",
            LocalDateTime.now(),
            LocalDateTime.now(),
            USER_ID,
            CATEGORY_ID,
            TOPIC_ID,
            SUBTOPIC_ID
        )
        val feedback = feedbackService.createFeedback(USER_ID, feedbackCreationDto)
        feedbackRepository.save(feedbackEntity)
        feedbackRepository.flush()
        feedbackId = feedback.id
    }

    @Test
    fun `When comment is added then it can be found`() = runBlocking {
        val user = createDefaultUser().copy(id = USER_ID)
        val feedback = feedbackRepository.getOne(feedbackId)
        val commentCreationDto = createDefaultCommentCreationDto(feedback.id)
        commentService.addComment(user.id, commentCreationDto)
        val userData = user.toUserData()
        val comments = commentService.getComments(commentCreationDto.feedbackId)
        assertNotNull(comments)
        assertTrue(comments.size == 1)
        comments.first().assertEquals(createDefaultCommentDto(userData, feedback))
    }

    @Test
    fun `When deleted then deleted`() = runBlocking {
        val user = createDefaultUser().copy(id = USER_ID)
        val feedback = feedbackRepository.getOne(feedbackId)
        val commentCreationDto = createDefaultCommentCreationDto(feedback.id)
        commentService.addComment(user.id, commentCreationDto)
        val comment = commentService.getComments(commentCreationDto.feedbackId).first()
        commentService.markDeleted(comment.id)
        val comments = commentService.getComments(commentCreationDto.feedbackId)
        assertTrue(comments.isEmpty())
    }

    @Test
    fun `When mark read then check read`() = runBlocking {
        val user = createDefaultUser().copy(id = USER_ID)
        val feedback = feedbackRepository.getOne(feedbackId)
        val commentCreationDto = createDefaultCommentCreationDto(feedback.id)
        commentService.addComment(user.id, commentCreationDto)
        var comment = commentService.getComments(commentCreationDto.feedbackId).first()
        commentService.markRead(comment.id)
        comment = commentService.getComments(commentCreationDto.feedbackId).first()
        assertFalse(comment.isUnread)
    }
}
