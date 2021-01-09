package com.github.neho4y.file.service

import com.github.neho4y.file.model.FileCreationDto
import com.github.neho4y.file.model.FileRetrieveDto
import com.github.neho4y.file.model.FileSendDto
import java.util.*

interface FileService {
    suspend fun addFile(fileCreationDto: FileCreationDto): UUID
    suspend fun deleteFile(fileRetrieveDto: FileRetrieveDto)
    suspend fun getFileRepresentation(fileRetrieveDto: FileRetrieveDto): FileSendDto
}
