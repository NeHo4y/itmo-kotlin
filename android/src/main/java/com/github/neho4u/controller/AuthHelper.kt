package com.github.neho4u.controller

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.github.neho4u.model.NetResult
import com.github.neho4u.shared.model.user.LoginParams
import com.github.neho4u.utils.Client
import io.ktor.client.features.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking

class AuthHelper(context: Context) {
    private val prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    fun getSessionKey(password: String): NetResult<String> {
        val loginParams = LoginParams(prefs.getString(PREF_USERNAME, null) ?: "", password)
        return runBlocking {
            try {
                val sessionToken = Client().user().login(loginParams).token
                NetResult(sessionToken, HttpStatusCode.OK, false)
            } catch (e: ResponseException) {
                NetResult("Login error", e.response.status, true)
            } catch (e: Throwable) {
                NetResult("Unknown error", HttpStatusCode.InternalServerError, true)
            }
        }
    }

    fun checkSessionKey(): NetResult<String> {
        return runBlocking {
            try {
                Client().user().getMe()
                NetResult("", HttpStatusCode.OK, false)
            } catch (e: ResponseException) {
                NetResult(e.message ?: "", e.response.status, true)
            } catch (e: Throwable) {
                NetResult("Unknown error", HttpStatusCode.InternalServerError, true)
            }
        }
    }

    companion object {
        const val PREF_USERNAME = "username"
        const val PREF_SESSION_KEY = "whd_session_key"
    }
}
