package com.github.neho4u.controller

import android.content.Context
import com.github.neho4u.R
import com.github.neho4u.shared.model.comment.CommentCreationDto
import com.github.neho4u.shared.model.comment.CommentUpdateDto
import com.github.neho4u.utils.Client
import io.ktor.client.features.*

class CommentController(
    private val context: Context?,
    private var noteInterface: NoteInterface
) {
    private suspend fun <T> handleErrors(action: suspend () -> T): T? {
        return try {
            action()
        } catch (t: Throwable) {
            when (t) {
                is ResponseException ->
                    noteInterface.noteSendError(context?.getString(R.string.error_conn) ?: "EГГОГ")
                is HttpRequestTimeoutException ->
                    noteInterface.noteSendError(context?.getString(R.string.error_timeout) ?: "EГГОГ")
                else -> noteInterface.noteSendError(context?.getString(R.string.error_unknown) ?: "EГГОГ")
            }
            null
        }
    }

    suspend fun sendComment(commentDto: CommentCreationDto) {
        handleErrors { Client().use { it.comment().add(commentDto) } }
    }

    suspend fun updateComment(commentId: Long, commentDto: CommentUpdateDto) {
        handleErrors { Client().use { it.comment().update(commentId, commentDto) } }
    }
}
