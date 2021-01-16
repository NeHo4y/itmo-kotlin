package com.github.neho4y.follower.service

import com.github.neho4u.shared.model.follower.FeedbackFollowerType
import com.github.neho4u.shared.model.follower.FollowerFilterDto
import com.github.neho4u.shared.model.user.UserRole
import com.github.neho4y.feedback.domain.Feedback
import com.github.neho4y.feedback.domain.repository.FeedbackRepository
import com.github.neho4y.follower.domain.FeedbackFollower
import com.github.neho4y.follower.model.FollowerDto
import com.github.neho4y.user.domain.User
import com.nhaarman.mockitokotlin2.given
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.annotation.DirtiesContext
import java.time.LocalDateTime
import java.util.*

private const val FEEDBACK_ID = 1337L
private const val USER_ID = 420L

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
internal class FeedbackFollowerServiceTest {

    @Autowired
    private lateinit var service: FeedbackFollowerService

    @MockBean
    private lateinit var feedbackService: FeedbackRepository

    @BeforeEach
    fun createCommentService() {
        given(feedbackService.findById(FEEDBACK_ID)).willReturn(Optional.of(defaultFeedback))
    }

    @Test
    fun `When add follower to feedback then it could be fetched`() = runBlocking {
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
    fun `When add follower to feedback multiple times then no duplicates`() = runBlocking {
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
    fun `When follower is deleted then it could not be found`() = runBlocking {
        // given
        val dto = defaultFeedbackDto.copy(followerType = FeedbackFollowerType.ASSIGNEE)
        val follow = service.addFollowerToFeedback(dto)

        // when
        service.removeFollowerFromFeedback(defaultUser, follow.id)
        val foundForFeedback = service.findFollowsByFilter(FollowerFilterDto(feedbackId = FEEDBACK_ID))
        val foundForUser = service.findFollowsByFilter(FollowerFilterDto(userId = USER_ID))

        // then
        assertThat(foundForFeedback).isEmpty()
        assertThat(foundForUser).isEmpty()
    }

    @Test
    fun `When get without type then return all types`(): Unit = runBlocking {
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

private val defaultFeedback = Feedback(
    "Test feedback",
    LocalDateTime.now(),
    LocalDateTime.now(),
    0,
    0,
    0,
    authorId = USER_ID,
    id = FEEDBACK_ID
)

private val defaultUser = User(
    "email",
    "password",
    "username",
    id = USER_ID,
    role = UserRole.ADMIN
)

private val defaultFeedbackDto = FollowerDto(
    FEEDBACK_ID,
    defaultUser,
    FeedbackFollowerType.WATCHER
)

private val defaultExpectedFeedbackFollower = FeedbackFollower(FEEDBACK_ID, USER_ID, FeedbackFollowerType.WATCHER)

private fun FeedbackFollower.assertEquals(other: FeedbackFollower?) {
    assertThat(feedbackId).isEqualTo(other?.feedbackId)
    assertThat(userId).isEqualTo(other?.userId)
    assertThat(followerType).isEqualTo(other?.followerType)
}
