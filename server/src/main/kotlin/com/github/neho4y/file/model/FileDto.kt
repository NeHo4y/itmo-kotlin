package com.github.neho4y.file.model

import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class FileCreationDto(
    val file: String,
    val filename: String
)

@Serializable
data class FileRetrieveDto(
    val id: Long
)

@Serializable
data class FileSendDto(
    val file: String,
    val filename: String
)
