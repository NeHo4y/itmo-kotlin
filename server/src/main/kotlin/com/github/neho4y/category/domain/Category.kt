package com.github.neho4y.category.domain

import javax.persistence.*

@Entity
@Table(name = "category")
data class Category(
    @Column
    var description: String,

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence")
    @SequenceGenerator(name = "sequence", allocationSize = 1, sequenceName = "category_seq")
    val id: Long = 0,

    @Column(name = "is_deleted")
    var isDeleted: Boolean = false
)
