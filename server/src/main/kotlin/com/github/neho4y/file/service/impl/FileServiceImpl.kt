package com.github.neho4y.file.service.impl

import com.github.neho4y.common.exception.NotFoundException
import com.github.neho4y.file.domain.FileEntity
import com.github.neho4y.file.domain.repository.FileRepository
import com.github.neho4y.file.model.FileCreationDto
import com.github.neho4y.file.model.FileRetrieveDto
import com.github.neho4y.file.model.FileSendDto
import com.github.neho4y.file.service.FileService
import org.springframework.stereotype.Service

@Service
class FileServiceImpl(private val fileRepository: FileRepository) : FileService {
    override suspend fun addFile(fileCreationDto: FileCreationDto): Long {
        val file = FileEntity(
            file = fileCreationDto.file,
            filename = fileCreationDto.filename
        )
        fileRepository.save(file)
        return file.id
    }

    override suspend fun deleteFile(fileRetrieveDto: FileRetrieveDto) {
        fileRepository.findById(fileRetrieveDto.id).orElseThrow { NotFoundException("Unable to find requested file") }
        fileRepository.deleteById(fileRetrieveDto.id)
    }

    override suspend fun getFileRepresentation(fileRetrieveDto: FileRetrieveDto): FileSendDto {
        val file = fileRepository.findById(fileRetrieveDto.id)
            .orElseThrow { NotFoundException("Unable to find requested file") }
        return FileSendDto(file.file, file.filename)
    }
}
