package com.github.neho4y.follower.domain

import javax.persistence.*

@Entity
@Table(name = "feedback_follower")
data class FeedbackFollower(
    @Column(name = "feedback_id")
    val feedbackId: Long,

    @Column(name = "user_id")
    val userId: Long,

    @Column(name = "follower_type")
    @Enumerated(EnumType.STRING)
    val followerType: FeedbackFollowerType,

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long = 0
)

enum class FeedbackFollowerType {
    ASSIGNEE,
    WATCHER
}
