package com.github.neho4u.view.settings

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.github.neho4u.R
import com.github.neho4u.controller.AuthHelper

class SettingsFragment : PreferenceFragmentCompat() {
    private var _listener: OnInteractionListener? = null
    private val listener get() = _listener!!

    private var _prefs: SharedPreferences? = null
    private val prefs get() = _prefs!!

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnInteractionListener) {
            _listener = context
            _prefs = PreferenceManager.getDefaultSharedPreferences(context)
        } else {
            throw RuntimeException("$context must implement SettingsFragment.OnInteractionListener")
        }
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.pref_settings, rootKey)
        findPreference<Preference>("key_pref_logout")
            ?.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            prefs.edit()
                .remove(AuthHelper.PREF_SESSION_KEY)
                .remove(AuthHelper.PREF_USERNAME)
                .apply()
            listener.logout()
            true
        }
    }

    override fun onDetach() {
        super.onDetach()
        _listener = null
        _prefs = null
    }

    interface OnInteractionListener {
        fun logout()
    }

    companion object {
        private const val TAG = "SettingsFragment"
    }
}
