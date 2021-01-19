package com.github.neho4y.category.domain

import javax.persistence.*

@Entity
@Table(name = "topic")
data class Topic(
    @Column
    var description: String,

    @Column(name = "category_id")
    var categoryId: Long,

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence")
    @SequenceGenerator(name = "sequence", allocationSize = 1, sequenceName = "topic_seq")
    val id: Long = 0,

    @Column(name = "is_deleted")
    var isDeleted: Boolean = false
)
