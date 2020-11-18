package com.github.neho4y.comment.service

import com.github.neho4y.comment.assertEquals
import com.github.neho4y.comment.createDefaultComment
import com.github.neho4y.comment.createDefaultCommentCreationDto
import com.github.neho4y.feedback.domain.repository.FeedbackRepository
import com.github.neho4y.feedback.model.FeedbackCreationDto
import com.github.neho4y.feedback.service.FeedbackService
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
internal class CommentServiceIntegrationTest {
    @Autowired
    private lateinit var commentService: CommentService

    @Autowired
    private lateinit var feedbackService: FeedbackService

    @Autowired
    private lateinit var feedbackRepository: FeedbackRepository
    private var feedbackId: Long = 0

    @BeforeEach
    fun createCommentService() {
        val feedbackCreationDto = FeedbackCreationDto(
            "Test feedback",
            0, 0, 0, "test comment"
        )
        val feedback = feedbackService.createFeedback(feedbackCreationDto)
        feedbackRepository.save(feedback)
        feedbackRepository.flush()
        feedbackId = feedback.id
    }

    @Test
    fun `When comment is added then it can be found`() {
        val feedback = feedbackRepository.getOne(feedbackId)
        val commentCreationDto = createDefaultCommentCreationDto(feedback.id)
        commentService.addComment(commentCreationDto)

        val comments = commentService.getComments(commentCreationDto.feedbackId)
        assertNotNull(comments)
        assertTrue(comments.size == 1)
        comments.first().assertEquals(createDefaultComment(feedback))
    }

    @Test
    fun `When deleted then deleted`() {
        val feedback = feedbackRepository.getOne(feedbackId)
        val commentCreationDto = createDefaultCommentCreationDto(feedback.id)
        commentService.addComment(commentCreationDto)
        val comment = commentService.getComments(commentCreationDto.feedbackId).first()
        commentService.markDeleted(comment.id)
        val comments = commentService.getComments(commentCreationDto.feedbackId)
        assertTrue(comments.isEmpty())
    }

    @Test
    fun `When mark read then check read`() {
        val feedback = feedbackRepository.getOne(feedbackId)
        val commentCreationDto = createDefaultCommentCreationDto(feedback.id)
        commentService.addComment(commentCreationDto)
        var comment = commentService.getComments(commentCreationDto.feedbackId).first()
        commentService.markRead(comment.id)
        comment = commentService.getComments(commentCreationDto.feedbackId).first()
        assertFalse(comment.isUnread)
    }

}