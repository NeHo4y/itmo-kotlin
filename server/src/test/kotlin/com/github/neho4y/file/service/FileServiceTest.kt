package com.github.neho4y.file.service

import com.github.neho4y.file.createDefaultFile
import com.github.neho4y.file.createDefaultFileCreationDto
import com.github.neho4y.file.domain.repository.FileRepository
import com.github.neho4y.file.model.FileRetrieveDto
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
internal class CommentServiceIntegrationTest {
    @Autowired
    private lateinit var fileService: FileService

    @Test
    fun `When file is added then it can be retrieved`() {

        val fileId = fileService.addFile(createDefaultFileCreationDto())

        assertDoesNotThrow {
            val file = fileService.getFileRepresentation(FileRetrieveDto(fileId))
            val defaultFile = createDefaultFile(fileId)
            assertTrue(file.file == defaultFile.file)
            assertTrue(file.filename == defaultFile.filename)
            assertTrue(fileId == defaultFile.uuid)
        }
    }

    @Test
    fun `When file is deleted then it can not be found`() {

        val fileId = fileService.addFile(createDefaultFileCreationDto())
        fileService.deleteFile(FileRetrieveDto(fileId))
        assertThrows<Exception> {
            fileService.getFileRepresentation(FileRetrieveDto(fileId))
        }
    }

}