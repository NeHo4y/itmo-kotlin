package com.github.neho4y.report.service.impl

import com.github.neho4u.shared.model.follower.FeedbackFollowerType
import com.github.neho4u.shared.model.follower.FollowerFilterDto
import com.github.neho4y.feedback.domain.repository.FeedbackRepository
import com.github.neho4y.follower.domain.repository.FeedbackFollowerSpecificationRepository
import com.github.neho4y.follower.domain.repository.FollowerSearchSpecification
import com.github.neho4y.report.model.StatusCount
import com.github.neho4y.report.service.ReportService
import com.github.neho4y.user.domain.repository.UserRepository
import com.itextpdf.text.Document
import com.itextpdf.text.pdf.PdfWriter
import com.itextpdf.tool.xml.XMLWorkerHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.core.io.InputStreamResource
import org.springframework.core.io.Resource
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context
import org.thymeleaf.templatemode.TemplateMode
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Service
class ReportServiceImpl(
    private val followerRepository: FeedbackFollowerSpecificationRepository,
    private val feedbackRepository: FeedbackRepository,
    private val userRepository: UserRepository
) : ReportService {
    override suspend fun generateReportForAdmin(adminUser: Long): Resource {
        val html = withContext(Dispatchers.IO) {
            val data = getDataReportForAdmin(adminUser)
            val admin = userRepository.findByIdOrNull(adminUser)
            parseThymeleafTemplate("reports/feedback_for_admin_stats") {
                setVariable("feedbackStatusCounts", data)
                setVariable("admin", admin?.username)
                setVariable("reportTime", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
            }
        }
        return InputStreamResource(html.convertToPdf())
    }

    private suspend fun getDataReportForAdmin(adminUser: Long): List<StatusCount> {
        val filter = FollowerFilterDto(userId = adminUser, followerType = FeedbackFollowerType.ASSIGNEE)
        return followerRepository.findAll(FollowerSearchSpecification(filter)).mapNotNull { follower ->
            feedbackRepository.findByIdOrNull(follower.feedbackId)
        }.groupingBy {
            it.status
        }.eachCount().map {
            StatusCount(it.key.name, it.value)
        }
    }

    private fun parseThymeleafTemplate(template: String, variables: Context.() -> Unit): String {
        val templateResolver = ClassLoaderTemplateResolver().apply {
            suffix = ".html"
            templateMode = TemplateMode.HTML
        }
        val templateEngine = TemplateEngine().apply {
            setTemplateResolver(templateResolver)
        }
        return templateEngine.process(template, Context().apply(variables))
    }

    private fun String.convertToPdf(): InputStream {
        val outputStream = ByteArrayOutputStream()
        val document = Document()
        val writer = PdfWriter.getInstance(document, outputStream)
        document.open()
        XMLWorkerHelper.getInstance().parseXHtml(writer, document, this.byteInputStream())
        document.close()
        return ByteArrayInputStream(outputStream.toByteArray())
    }
}
