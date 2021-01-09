package com.github.neho4y.comment.domain

import com.github.neho4y.feedback.domain.Feedback
import java.time.LocalDateTime
import javax.persistence.*

@Table(name = "feedback_message")
@Entity
data class Comment(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence")
    @SequenceGenerator(name = "sequence", allocationSize = 1, sequenceName = "feedback_seq")
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feedback_id")
    var feedbackId: Feedback,

    @Column(name = "author_id")
    var authorId: Long = 0,

    @Column(name = "message_type")
    var messageType: String,

    @Column(name = "message_date")
    var messageDate: LocalDateTime,

    @Column(name = "message_text")
    var messageText: String,

    @Column(name = "is_unread")
    var isUnread: Boolean = true,

    @Column(name = "is_deleted")
    var isDeleted: Boolean = false
)
