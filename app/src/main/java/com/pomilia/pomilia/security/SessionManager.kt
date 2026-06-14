package com.pomilia.pomilia.security

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

@Suppress("DEPRECATION")
class SessionManager(
    context: Context
) {

    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val securePreferences = EncryptedSharedPreferences.create(
        context,
        SECURE_PREFS_NAME,
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun saveSession(username: String) {
        securePreferences.edit()
            .putString(KEY_USERNAME, username)
            .putString(KEY_DEMO_TOKEN, "pomilia_demo_token_123")
            .putBoolean(KEY_IS_LOGGED_IN, true)
            .apply()
    }

    fun getUsername(): String? {
        return securePreferences.getString(KEY_USERNAME, null)
    }

    fun getDemoToken(): String? {
        return securePreferences.getString(KEY_DEMO_TOKEN, null)
    }

    fun isLoggedIn(): Boolean {
        return securePreferences.getBoolean(KEY_IS_LOGGED_IN, false)
    }

    fun clearSession() {
        securePreferences.edit()
            .clear()
            .apply()
    }

    companion object {
        private const val SECURE_PREFS_NAME = "pomilia_secure_prefs"
        private const val KEY_USERNAME = "username"
        private const val KEY_DEMO_TOKEN = "demo_token"
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
    }
}