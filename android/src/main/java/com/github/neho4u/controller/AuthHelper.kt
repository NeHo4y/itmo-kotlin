package com.github.neho4u.controller

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.util.Log
import com.github.neho4u.model.NetResult
import com.github.neho4u.shared.model.user.LoginParams
import com.github.neho4u.utils.Client
import io.ktor.client.features.*
import io.ktor.http.*

class AuthHelper(context: Context) {
    private val prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    suspend fun getSessionKey(password: String): NetResult<String> {
        val loginParams = LoginParams(prefs.getString(PREF_USERNAME, null) ?: "", password)
        return handleErrors {
            val sessionToken = Client().use { it.user().login(loginParams).token }
            NetResult(sessionToken, HttpStatusCode.OK, false)
        }
    }

    suspend fun checkSessionKey(): NetResult<String> {
        return handleErrors {
            Client().use { it.user().getMe() }
            NetResult("", HttpStatusCode.OK, false)
        }
    }

    private suspend fun handleErrors(action: suspend () -> NetResult<String>): NetResult<String> {
        return try {
            action()
        } catch (e: ResponseException) {
            NetResult(e.message ?: "", e.response.status, true)
        } catch (e: HttpRequestTimeoutException) {
            NetResult(e.message ?: "", HttpStatusCode.RequestTimeout, true)
        } catch (e: Throwable) {
            Log.e("Net error", "Unknown error", e)
            NetResult("Unknown error", HttpStatusCode.InternalServerError, true)
        }
    }

    companion object {
        const val PREF_USERNAME = "username"
        const val PREF_SESSION_KEY = "whd_session_key"
    }
}
