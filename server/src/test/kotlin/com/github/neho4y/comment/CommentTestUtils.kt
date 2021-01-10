package com.github.neho4y.comment

import com.github.neho4u.shared.model.comment.CommentCreationDto
import com.github.neho4y.comment.domain.Comment
import com.github.neho4y.feedback.domain.Feedback
import org.assertj.core.api.Assertions
import java.time.LocalDateTime

internal fun createDefaultComment(feedback: Feedback) = Comment(
    authorId = 1,
    feedbackId = feedback,
    isDeleted = false,
    messageDate = LocalDateTime.of(2020, 10, 12, 15, 0),
    isUnread = true,
    messageText = "testtext",
    messageType = "testtype"
)

internal fun createDefaultCommentCreationDto(feedbackId: Long) = CommentCreationDto(
    feedbackId = feedbackId,
    authorId = 1,
    messageText = "testtext",
    messageType = "testtype"
)

internal fun Comment.assertEquals(other: Comment?) {
    Assertions.assertThat(other?.authorId).isEqualTo(authorId)
    Assertions.assertThat(other?.messageText).isEqualTo(messageText)
    Assertions.assertThat(other?.messageType).isEqualTo(messageType)
}
