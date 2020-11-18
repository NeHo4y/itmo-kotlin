package com.github.neho4y.file.service

import com.github.neho4y.file.model.FileCreationDto
import com.github.neho4y.file.model.FileRetrieveDto
import com.github.neho4y.file.model.FileSendDto
import java.util.*

interface FileService {
    fun addFile(fileCreationDto: FileCreationDto): UUID
    fun deleteFile(fileRetrieveDto: FileRetrieveDto)
    fun getFileRepresentation(fileRetrieveDto: FileRetrieveDto): FileSendDto
}