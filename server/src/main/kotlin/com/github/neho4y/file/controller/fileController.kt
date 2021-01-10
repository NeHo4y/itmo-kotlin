package com.github.neho4y.file.controller

import com.github.neho4y.file.model.FileRetrieveDto
import com.github.neho4y.file.model.FileSendDto
import com.github.neho4y.file.service.FileService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/files")
class fileController(private val fileService: FileService) {
    @GetMapping("/get/{id}")
    suspend fun getFile(@PathVariable id: Long): FileSendDto {
        return fileService.getFileRepresentation(FileRetrieveDto(id = id))
    }
}
