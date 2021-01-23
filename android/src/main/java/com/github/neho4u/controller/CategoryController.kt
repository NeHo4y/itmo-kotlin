package com.github.neho4u.controller

import com.github.neho4u.utils.Client
import com.github.neho4u.view.DrawerView
import io.ktor.client.features.*

class CategoryController(
    private val parent: DrawerView
) {
    private suspend fun <T> handleErrors(action: suspend () -> T): T? {
        return try {
            action()
        } catch (t: Throwable) {
            when (t) {
                is ResponseException -> parent.showError("Server error :${t.response.status}")
                is HttpRequestTimeoutException -> parent.showError("Timeout error")
                else -> parent.showError(t.toString())
            }
            null
        }
    }

    suspend fun getCategories() = handleErrors {
        Client().use {
            val categories = it.category().getCategories()
            val topics = it.category().getTopics()
            val subtopics = it.category().getSubtopics()
            Triple(categories, topics, subtopics)
        }
    }
}
