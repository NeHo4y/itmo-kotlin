package com.github.neho4u.view.settings

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.github.neho4u.R
import com.github.neho4u.view.Login

class SettingsActivity : AppCompatActivity(), SettingsFragment.OnInteractionListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.a_settings)
        val toolbar = findViewById<Toolbar>(R.id.s_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        // Display the fragment as the main content.
        supportFragmentManager.beginTransaction()
            .replace(R.id.s_content_frame, SettingsFragment())
            .commit()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {
        private const val TAG = "SettingsActivity"
    }

    override fun logout() {
        startActivity(
            Intent(this, Login::class.java).apply {
                addFlags(
                    Intent.FLAG_ACTIVITY_CLEAR_TOP or
                        Intent.FLAG_ACTIVITY_CLEAR_TASK or
                        Intent.FLAG_ACTIVITY_NEW_TASK
                )
            }
        )
        this.finish()
    }
}
