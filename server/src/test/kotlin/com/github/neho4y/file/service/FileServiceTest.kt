package com.github.neho4y.file.service

import com.github.neho4y.file.createDefaultFile
import com.github.neho4y.file.createDefaultFileCreationDto
import com.github.neho4y.file.model.FileRetrieveDto
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
internal class FileServiceTest {
    @Autowired
    private lateinit var fileService: FileService

    @Test
    fun `When file is added then it can be retrieved`() = runBlocking {
        val fileId = fileService.addFile(createDefaultFileCreationDto())
        val file = fileService.getFileRepresentation(FileRetrieveDto(fileId))
        val defaultFile = createDefaultFile(fileId)
        assertTrue(file.file == defaultFile.file)
        assertTrue(file.filename == defaultFile.filename)
        assertTrue(fileId == defaultFile.id)
    }

    @Test
    fun `When file is deleted then it can not be found`(): Unit = runBlocking {
        val fileId = fileService.addFile(createDefaultFileCreationDto())
        fileService.deleteFile(FileRetrieveDto(fileId))
        assertThrows<Exception> {
            fileService.getFileRepresentation(FileRetrieveDto(fileId))
        }
    }
}
