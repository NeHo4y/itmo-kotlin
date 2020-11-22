package com.github.neho4y.file.model

import java.util.*

class FileCreationDto(
    val file: String,
    val filename: String
)

class FileRetrieveDto(
    val uuid: UUID
)

class FileSendDto(
    val file: String,
    val filename: String
)
