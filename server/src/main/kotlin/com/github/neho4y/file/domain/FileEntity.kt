package com.github.neho4y.file.domain

import javax.persistence.*

@Table(name = "file")
@Entity
data class FileEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence")
    @SequenceGenerator(name = "sequence", allocationSize = 1, sequenceName = "file_seq")
    val id: Long = 0,
    @Column
    var file: String,
    @Column
    var filename: String
)
