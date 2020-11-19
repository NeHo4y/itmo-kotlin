package com.github.neho4y.follower.service

import com.github.neho4y.feedback.domain.Feedback
import com.github.neho4y.follower.domain.FeedbackFollower
import com.github.neho4y.follower.domain.FeedbackFollowerType
import com.github.neho4y.follower.model.FollowerDto
import com.github.neho4y.follower.model.FollowerFilterDto
import com.github.neho4y.user.domain.User
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import java.time.LocalDateTime

private const val FEEDBACK_ID = 1337L
private const val USER_ID = 420L

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
internal class FeedbackFollowerServiceTest {

    @Autowired
    private lateinit var service: FeedbackFollowerService

    @Test
    fun `When add follower to feedback then it could be fetched`() {
        // given
        val expected = defaultExpectedFeedbackFollower

        // when
        service.addFollowerToFeedback(defaultFeedbackDto)
        val foundForFeedback = service.findFollowsByFilter(FollowerFilterDto(feedbackId = FEEDBACK_ID))
        val foundForUser = service.findFollowsByFilter(FollowerFilterDto(userId = USER_ID))

        // then
        assertThat(foundForFeedback.size).isEqualTo(1)
        expected.assertEquals(foundForFeedback.first())
        assertThat(foundForUser.size).isEqualTo(1)
        expected.assertEquals(foundForUser.first())
    }

    @Test
    fun `When add follower to feedback multiple times then no duplicates`() {
        // given
        val expected = defaultExpectedFeedbackFollower

        // when
        service.addFollowerToFeedback(defaultFeedbackDto)
        service.addFollowerToFeedback(defaultFeedbackDto)
        service.addFollowerToFeedback(defaultFeedbackDto)
        val foundForFeedback = service.findFollowsByFilter(FollowerFilterDto(feedbackId = FEEDBACK_ID))
        val foundForUser = service.findFollowsByFilter(FollowerFilterDto(userId = USER_ID))

        // then
        assertThat(foundForFeedback.size).isEqualTo(1)
        expected.assertEquals(foundForFeedback.first())
        assertThat(foundForUser.size).isEqualTo(1)
        expected.assertEquals(foundForUser.first())
    }

    @Test
    fun `When follower is deleted then it could not be found`() {
        // given
        val dto = defaultFeedbackDto.copy(followerType = FeedbackFollowerType.ASSIGNEE)
        val follow = service.addFollowerToFeedback(dto)

        // when
        service.removeFollowerFromFeedback(follow.id)
        val foundForFeedback = service.findFollowsByFilter(FollowerFilterDto(feedbackId = FEEDBACK_ID))
        val foundForUser = service.findFollowsByFilter(FollowerFilterDto(userId = USER_ID))

        // then
        assertThat(foundForFeedback).isEmpty()
        assertThat(foundForUser).isEmpty()
    }

    @Test
    fun `When get without type then return all types`() {
        // given
        service.addFollowerToFeedback(defaultFeedbackDto)
        service.addFollowerToFeedback(defaultFeedbackDto.copy(followerType = FeedbackFollowerType.ASSIGNEE))
        val filter = FollowerFilterDto(feedbackId = FEEDBACK_ID)

        // when
        val foundForFeedback = service.findFollowsByFilter(filter)
        val foundForFeedbackFilter = service.findFollowsByFilter(
            filter.copy(followerType = FeedbackFollowerType.WATCHER)
        )
        val foundForUserFilter = service.findFollowsByFilter(
            FollowerFilterDto(userId = USER_ID, followerType = FeedbackFollowerType.ASSIGNEE)
        )

        // then
        assertThat(foundForFeedback.size).isEqualTo(2)
        assertThat(foundForFeedbackFilter.size).isEqualTo(1)
        assertThat(foundForUserFilter.size).isEqualTo(1)
    }
}

private val defaultFeedbackDto = FollowerDto(
    Feedback(
        "Test feedback",
        LocalDateTime.now(),
        LocalDateTime.now(),
        FEEDBACK_ID,
        FEEDBACK_ID,
        FEEDBACK_ID,
        id = FEEDBACK_ID
    ),
    User("email", "password", "username", id = USER_ID),
    FeedbackFollowerType.WATCHER
)

private val defaultExpectedFeedbackFollower = FeedbackFollower(FEEDBACK_ID, USER_ID, FeedbackFollowerType.WATCHER)

private fun FeedbackFollower.assertEquals(other: FeedbackFollower?) {
    assertThat(feedbackId).isEqualTo(other?.feedbackId)
    assertThat(userId).isEqualTo(other?.userId)
    assertThat(followerType).isEqualTo(other?.followerType)
}
