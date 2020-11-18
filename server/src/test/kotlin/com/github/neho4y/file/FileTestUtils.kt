package com.github.neho4y.file

import com.github.neho4y.file.domain.FileEntity
import com.github.neho4y.file.model.FileCreationDto
import java.util.*

internal fun createDefaultFile(uuid: UUID) = FileEntity(
    uuid = uuid,
    file = "teststring",
    filename = "filename test"
)

internal fun createDefaultFileCreationDto() = FileCreationDto(
    file = "teststring",
    filename = "filename test"
)