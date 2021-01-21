package com.github.neho4y.report.service

import org.springframework.core.io.Resource

interface ReportService {
    suspend fun generateReportForAdmin(adminUser: Long): Resource
}
