package com.github.neho4u.controller

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.util.Log
import android.widget.EditText
import com.github.neho4u.R
import com.github.neho4u.model.AuthModel
import com.github.neho4u.model.NetResult
import com.github.neho4u.view.Login
import org.json.JSONException
import org.json.JSONObject


class AuthController(private val context: Context, val parent: Login,
                     private val server: EditText, val username: EditText,
                     val password: EditText) {

    private val use_ssl = true
    private val prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    val model = AuthModel(context,this)

    /*
    ************************
    INITIALIZATION FUNCTIONS
    ************************
     */


    //grab the previously used values from preferences and insert them into the edittexts
    fun initETVals(){
        server.setText(prefs.getString(AuthModel.PREF_SERVER, ""))
        username.setText(prefs.getString(AuthModel.PREF_USERNAME, ""))
    }

    /*
    *********
    DATA GETS
    *********
     */

    @SuppressLint("ApplySharedPref")
    fun attemptLogin(){
        val views = arrayListOf(server, username, password)

        //tell the user if they've entered the server incorrectly
        if(server.text.toString().contains("http://", true) ||
                server.text.toString().contains("https://", true) ||
                server.text.toString().contains("/", true) ||
                server.text.toString().contains("\\", true)){
            server.error = "Only type the FQDN of the server: don't include http or anything after the TLD"
            server.requestFocus()
            parent.setLoading(false)
            return
        }

        if(server.text.toString().contains(" ")){
            server.error = "Spaces are not supported in a DNS address"
            server.requestFocus()
            parent.setLoading(false)
            return
        }

        //make sure none of them are empty
        for(view in views){
            view.setText(view.text.trim())
            if(view.text.toString() == ""){
                view.error = "Cannot be empty"
                view.requestFocus()
                parent.setLoading(false)
                return
            }
        }

        //insert the data we've collected
        val editor = prefs.edit()
        editor.putString(AuthModel.PREF_SERVER, server.text.toString())
        editor.putString(AuthModel.PREF_USERNAME, username.text.toString())
        editor.putBoolean(AuthModel.PREF_USE_SSL, use_ssl)
        editor.commit()//we need this data available immediately
        //try to get a session key
        model.getSessionKey(password.text.toString())

    }

    fun checkKey(){
        //if we have a session key saved
        if(prefs.contains(AuthModel.PREF_SESSION_KEY) && prefs.getString(AuthModel.PREF_SESSION_KEY, "") != ""){
            model.checkSessionKey()
        } else {
            parent.sessionInvalid()
        }
    }

    /*
    ************
    DATA PROCESS
    ************
     */

    @SuppressLint("ApplySharedPref")
    fun sessionKeyResult(result: NetResult){
        if(result.error){
            parent.setLoading(false)
            when {
                result.responseCode == 401 -> {
                    password.error = "Username or password incorrect: 401 Unauthorized"
                    password.setText("")
                    password.requestFocus()
                }
                result.responseCode == 404 -> {
                    server.error = "Can't find a compatible API: 404 Not Found"
                    server.requestFocus()
                }
                result.responseCode == -5 -> {
                    server.error = result.result
                    server.requestFocus()
                }
                else -> {
                    val t = Throwable(result.result)
                    Log.e("AuthController","Unhandled net error",t)
                    server.error = result.result.removePrefix("java.net.UnknownHostException: ")
                    server.requestFocus()
                }
            }
        } else {
            //handle login
            try{
            val o = JSONObject(result.result)
            val currentTechId = o.getString("currentTechId")
            val key = o.getString("sessionKey")
            if(key != null) {
                prefs.edit()
                        .putString(AuthModel.PREF_SESSION_KEY, key)
                        .putString(AuthModel.PREF_COOKIE, result.cookie)
                        .putString(AuthModel.PREF_TECH_ID, currentTechId)
                        .commit()
                parent.login()
                }
            } catch (e: JSONException){

                val t = Throwable("Expected JSON with sessionKey object but received: " + result.result + " ::: " + e.message)
                Log.e("AuthController","Server response did not return correct JSON",t)
                server.setText(context.getString(R.string.login_invalid_no_sessionkey))
                parent.setLoading(false)

            }

        }
    }

    fun checkKeyResult(result: NetResult){
        Log.d("AuthController","Session status code is " + result.responseCode)

        if(result.error){
            if(result.responseCode != 401){
                val t = Throwable("AuthController received unexpected response from model: code: " +
                        result.responseCode + " | data: " + result.result + " | error: " + result.error.toString())
                Log.e("AuthController","Unexpected response from model",t)
            }
            parent.sessionInvalid()
        } else {
            parent.login()
        }
    }

}