package com.github.neho4y.file.domain.repository

import com.github.neho4y.file.domain.FileEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface FileRepository : JpaRepository<FileEntity, UUID>
