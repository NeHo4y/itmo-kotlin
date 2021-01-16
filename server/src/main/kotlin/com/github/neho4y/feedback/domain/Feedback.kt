package com.github.neho4y.feedback.domain

import com.github.neho4u.shared.model.feedback.FeedbackPriority
import com.github.neho4u.shared.model.feedback.FeedbackStatus
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "feedback")
data class Feedback(
    @Column
    var header: String,

    @Column(name = "creation_date")
    var creationDate: LocalDateTime,

    @Column(name = "update_date")
    var updateDate: LocalDateTime,

    @Column(name = "category_id")
    var categoryId: Long,

    @Column(name = "topic_id")
    var topicId: Long,

    @Column(name = "subtopic_id")
    var subtopicId: Long,

    @Column(name = "author_id")
    var authorId: Long,

    @Column(name = "end_date")
    var endDate: LocalDateTime? = null,

    @Column(name = "status_id")
    @Enumerated(EnumType.STRING)
    var status: FeedbackStatus = FeedbackStatus.CREATED,

    @Column(name = "priority_id")
    @Enumerated(EnumType.STRING)
    var priority: FeedbackPriority = FeedbackPriority.MEDIUM,

    @Column(name = "is_actual")
    var isActual: Boolean = true,

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long = 0
)
