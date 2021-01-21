package com.github.neho4y.report.controller

import com.github.neho4u.shared.model.user.UserRole
import com.github.neho4y.report.service.ReportService
import com.github.neho4y.user.domain.User
import org.springframework.core.io.Resource
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/reports")
class ReportController(private val reportService: ReportService) {
    @GetMapping("/adminStats/{adminId}")
    suspend fun adminStats(
        @PathVariable adminId: Long,
        @AuthenticationPrincipal user: User
    ): ResponseEntity<Resource> {
        if (user.role != UserRole.ADMIN) {
            throw ResponseStatusException(HttpStatus.FORBIDDEN, "Only allowed for ADMIN")
        }
        return pdfResponse(reportService.generateReportForAdmin(adminId))
    }

    private fun pdfResponse(data: Resource) =
        ResponseEntity.ok()
            .headers {
                it.add("Content-Disposition", "inline")
                it.cacheControl = "must-revalidate, post-check=0, pre-check=0"
            }
            .contentType(MediaType.APPLICATION_PDF)
            .body(data)
}
