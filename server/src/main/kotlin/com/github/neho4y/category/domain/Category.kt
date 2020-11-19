package com.github.neho4y.category.domain

import javax.persistence.*

@Entity
@Table(name = "category")
data class Category(

    @Column
    var description: String,

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long = 0,

    @Column(name = "is_deleted")
    var isDeleted: Boolean = false

)
