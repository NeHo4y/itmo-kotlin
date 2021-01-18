package com.github.neho4u.view

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.inputmethod.EditorInfo
import androidx.databinding.DataBindingUtil
import com.github.neho4u.R
import com.github.neho4u.controller.AuthController
import com.github.neho4u.databinding.ALoginBinding

class Login : AppCompatActivity() {

    private lateinit var auth: AuthController
    private lateinit var binding: ALoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.a_login)
        binding.loginToolbar.title = getString(R.string.login)

        // initialize method variables
        auth = AuthController(
            applicationContext,
            this,
            binding.etUsername,
            binding.etPassword
        )

        // startup functions
        auth.init()
        // start session key check process
        binding.etPassword.hint = getString(R.string.session_check)
        auth.checkKey()

        // start login process if enter key is pressed in password field
        binding.etPassword.setOnEditorActionListener { _, actionId, _ ->
            var handled = false
            if (actionId == EditorInfo.IME_ACTION_GO) {
                setLoading(true)
                auth.attemptLogin()
                handled = true
            }
            handled
        }

        // start login process if login button is clicked
        binding.bLogin.setOnClickListener {
            setLoading(true)
            auth.attemptLogin()
        }
    }

    // toggles views between loading state
    fun setLoading(loading: Boolean) {
        if (loading) {
            binding.bLogin.setText(R.string.loading)
            binding.bLogin.isEnabled = false
        } else {
            binding.bLogin.setText(R.string.login)
            binding.bLogin.isEnabled = true
        }
    }

    // called after session key has been verified
    fun login() {
        // for some reason starting intents takes a second or two these days
        // so we disable the login views and start the transition process
        setLoading(true)
        binding.bLogin.text = getString(R.string.logging_in)
        binding.etPassword.setOnEditorActionListener(null)

        // also log dat ish
        val params = Bundle()
        params.putBoolean("success", true)

        val i = Intent(this, DrawerView::class.java)
        startActivity(i)

        // quit this activity so it doesn't show up in the back stack
        finish()
    }

    // reset view to default
    fun sessionInvalid() {
        binding.etPassword.hint = resources.getString(R.string.password)
    }
}
