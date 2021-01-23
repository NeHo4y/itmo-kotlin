package com.github.neho4u.controller

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.widget.EditText
import androidx.preference.PreferenceManager
import com.github.neho4u.model.NetResult
import com.github.neho4u.utils.AndroidTokenProvider
import com.github.neho4u.view.Login
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AuthController(
    context: Context,
    private val parent: Login,
    private val username: EditText,
    private val password: EditText
) {
    private val prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    private val helper = AuthHelper(context)

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

        GlobalScope.launch(Dispatchers.Default) {
            val sessionKey = helper.getSessionKey(password.text.toString())
            sessionKeyResult(sessionKey)
        }
    }

    @SuppressLint("ApplySharedPref")
    private fun sessionKeyResult(result: NetResult<String>) {
        if (result.error) {
            parent.runOnUiThread {
                when (result.responseCode) {
                    HttpStatusCode.Unauthorized -> with(password) {
                        error = "Username or password incorrect: 401 Unauthorized"
                        setText("")
                        requestFocus()
                    }
                    HttpStatusCode.NotFound -> with(username) {
                        error = "Can't find a compatible API: 404 Not Found"
                        requestFocus()
                    }
                    HttpStatusCode.RequestTimeout -> with(username) {
                        error = "Timeout: 408 Timeout"
                        requestFocus()
                    }
                    else -> with(username) {
                        Log.e("AuthController", "Unhandled net error", Throwable(result.result))
                        error = result.result.removePrefix("java.net.UnknownHostException: ").substring(0, 50)
                        requestFocus()
                    }
                }
                parent.setLoading(false)
            }
        } else {
            val key = result.result
            AndroidTokenProvider.setToken(key)
            prefs.edit()
                .putString(AuthHelper.PREF_SESSION_KEY, key)
                .commit()
            parent.login()
        }
    }

    fun checkKey() {
        val stored = prefs.getString(AuthHelper.PREF_SESSION_KEY, "") ?: ""
        if (stored.isNotEmpty()) {
            parent.setLoading(true)
            AndroidTokenProvider.setToken(stored)
            GlobalScope.launch(Dispatchers.Default) {
                checkKeyResult(helper.checkSessionKey())
            }
        } else {
            sessionInvalid()
        }
    }

    private fun checkKeyResult(result: NetResult<String>) {
        Log.d("AuthController", "Session status code is " + result.responseCode)

        if (result.error) {
            parent.runOnUiThread {
                when (result.responseCode) {
                    HttpStatusCode.Unauthorized -> { }
                    HttpStatusCode.RequestTimeout -> {
                        with(username) {
                            error = "Timeout: 408 Timeout"
                            requestFocus()
                        }
                    }
                    else -> {
                        val t = Throwable("AuthController received unexpected response code: ${result.responseCode}")
                        Log.e("AuthController", "Unexpected response", t)
                        with(username) {
                            error = "Unknown error: ${result.responseCode.value}, Check your connection"
                            requestFocus()
                        }
                    }
                }
            }
            sessionInvalid()
        } else {
            parent.login()
        }
    }

    private fun sessionInvalid() {
        AndroidTokenProvider.setToken(null)
        parent.sessionInvalid()
    }
}
