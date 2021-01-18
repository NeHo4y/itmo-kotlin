package com.github.neho4u.controller

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.util.Log
import android.widget.EditText
import com.github.neho4u.model.NetResult
import com.github.neho4u.utils.AndroidTokenProvider
import com.github.neho4u.view.Login
import io.ktor.http.*

class AuthController(
    context: Context,
    private val parent: Login,
    private val username: EditText,
    private val password: EditText
) {
    private val prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    val model = AuthHelper(context)

    fun init() {
        username.setText(prefs.getString(AuthHelper.PREF_USERNAME, ""))
    }

    @SuppressLint("ApplySharedPref")
    fun attemptLogin() {
        val views = arrayListOf(username, password)

        for (view in views) {
            view.setText(view.text.trim())
            if (view.text.isEmpty()) {
                view.error = "Cannot be empty"
                view.requestFocus()
                parent.setLoading(false)
                return
            }
        }

        prefs.edit()
            .putString(AuthHelper.PREF_USERNAME, username.text.toString())
            .commit()

        val sessionKey = model.getSessionKey(password.text.toString())
        sessionKeyResult(sessionKey)
    }

    fun checkKey() {
        val stored = prefs.getString(AuthHelper.PREF_SESSION_KEY, "") ?: ""
        if (stored.isNotEmpty()) {
            AndroidTokenProvider.setToken(stored)
            checkKeyResult(model.checkSessionKey())
        } else {
            parent.sessionInvalid()
        }
    }

    @SuppressLint("ApplySharedPref")
    private fun sessionKeyResult(result: NetResult<String>) {
        if (result.error) {
            parent.setLoading(false)
            when (result.responseCode) {
                HttpStatusCode.Unauthorized -> {
                    password.error = "Username or password incorrect: 401 Unauthorized"
                    password.setText("")
                    password.requestFocus()
                }
                HttpStatusCode.NotFound -> {
                    username.error = "Can't find a compatible API: 404 Not Found"
                    username.requestFocus()
                }
                else -> {
                    val t = Throwable(result.result)
                    Log.e("AuthController", "Unhandled net error", t)
                    username.error = result.result.removePrefix("java.net.UnknownHostException: ")
                    username.requestFocus()
                }
            }
        } else {
            val key = result.result
            prefs.edit()
                .putString(AuthHelper.PREF_SESSION_KEY, key)
                .commit()
            parent.login()
        }
    }

    private fun checkKeyResult(result: NetResult<String>) {
        Log.d("AuthController", "Session status code is " + result.responseCode)

        if (result.error) {
            if (result.responseCode != HttpStatusCode.Unauthorized) {
                val t = Throwable(
                    "AuthController received unexpected response code: ${result.responseCode}"
                )
                Log.e("AuthController", "Unexpected response", t)
            }
            parent.sessionInvalid()
        } else {
            parent.login()
        }
    }
}
