package com.github.neho4y.file.service.impl

import com.github.neho4y.file.domain.FileEntity
import com.github.neho4y.file.domain.repository.FileRepository
import com.github.neho4y.file.model.FileCreationDto
import com.github.neho4y.file.model.FileRetrieveDto
import com.github.neho4y.file.model.FileSendDto
import com.github.neho4y.file.service.FileService
import org.springframework.stereotype.Service
import java.util.*

@Service
class FileServiceImpl(private val fileRepository: FileRepository) : FileService {
    override fun addFile(fileCreationDto: FileCreationDto): UUID {
        var uuid = UUID.randomUUID()
        while (fileRepository.existsById(uuid)) {
            uuid = UUID.randomUUID()
        }
        val file = FileEntity(
            uuid = uuid,
            file = fileCreationDto.file,
            filename = fileCreationDto.filename
        )
        fileRepository.save(file)
        return uuid
    }

    override fun deleteFile(fileRetrieveDto: FileRetrieveDto) {
        fileRepository.findById(fileRetrieveDto.uuid).orElseThrow()
        fileRepository.deleteById(fileRetrieveDto.uuid)
    }

    override fun getFileRepresentation(fileRetrieveDto: FileRetrieveDto): FileSendDto {
        val file = fileRepository.findById(fileRetrieveDto.uuid).orElseThrow()
        return FileSendDto(file.file, file.filename)
    }

}