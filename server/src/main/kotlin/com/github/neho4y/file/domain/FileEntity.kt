package com.github.neho4y.file.domain

import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Table(name = "file")
@Entity
data class FileEntity(
    @Id
    var uuid: UUID,
    @Column
    var file: String,
    @Column
    var filename: String
)
