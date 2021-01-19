package com.github.neho4y.comment

import com.github.neho4u.shared.model.comment.CommentCreationDto
import com.github.neho4u.shared.model.comment.CommentDto
import com.github.neho4u.shared.model.user.UserData
import com.github.neho4y.feedback.domain.Feedback
import kotlinx.datetime.toKotlinLocalDateTime
import org.assertj.core.api.Assertions
import java.time.LocalDateTime

internal fun createDefaultCommentDto(userData: UserData, feedback: Feedback) = CommentDto(
    id = 1,
    authorData = userData,
    feedbackId = feedback.id,
    isUnread = true,
    messageText = "testtext",
    messageType = "testtype",
    creationDate = LocalDateTime.now().toKotlinLocalDateTime()
)

internal fun createDefaultCommentCreationDto(feedbackId: Long) = CommentCreationDto(
    feedbackId = feedbackId,
    messageText = "testtext",
    messageType = "testtype"
)

internal fun CommentDto.assertEquals(other: CommentDto?) {
    Assertions.assertThat(other?.authorData?.username).isEqualTo(authorData.username)
    Assertions.assertThat(other?.authorData?.email).isEqualTo(authorData.email)
    Assertions.assertThat(other?.feedbackId).isEqualTo(feedbackId)
    Assertions.assertThat(other?.isUnread).isEqualTo(isUnread)
    Assertions.assertThat(other?.messageText).isEqualTo(messageText)
    Assertions.assertThat(other?.messageType).isEqualTo(messageType)
}
