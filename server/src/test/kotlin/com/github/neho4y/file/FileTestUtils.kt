package com.github.neho4y.file

import com.github.neho4y.file.domain.FileEntity
import com.github.neho4y.file.model.FileCreationDto

internal fun createDefaultFile(id: Long) = FileEntity(

    id = id,
    file = "teststring",
    filename = "filename test"
)

internal fun createDefaultFileCreationDto() = FileCreationDto(
    file = "teststring",
    filename = "filename test"
)
