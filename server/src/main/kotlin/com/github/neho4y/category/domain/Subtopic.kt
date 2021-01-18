package com.github.neho4y.category.domain

import javax.persistence.*

@Entity
@Table(name = "subtopic")
data class Subtopic(
    @Column
    var description: String,

    @Column(name = "topic_id")
    var topicId: Long,

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence")
    @SequenceGenerator(name = "sequence", allocationSize = 1, sequenceName = "subtopic_seq")
    val id: Long = 0,

    @Column(name = "is_deleted")
    var isDeleted: Boolean = false
)
