package com.albatros.kplanner.model.repo

import android.content.SharedPreferences

class PreferencesRepo(private val settings: SharedPreferences) {

    private val editor = settings.edit()
    var opened = getOpenedMode() == MODE_OPENED

    companion object {
        const val KEY_OPENED = "key_opened"
        const val MODE_OPENED = "mode_opened"
        const val MODE_NOT_OPENED = "mode_not_opened"

        const val KEY_EMAIL = "key_email"
        const val KEY_PASSWORD = "key_password"
    }

    fun setEmail(email: String) {
        with(editor) {
            putString(KEY_EMAIL, email)
            commit()
            apply()
        }
    }

    fun setPassword(password: String) {
        with(editor) {
            putString(KEY_PASSWORD, password)
            commit()
            apply()
        }
    }

    fun getEmail() = settings.getString(KEY_EMAIL, "")

    fun getPassword() = settings.getString(KEY_PASSWORD, "")

    private fun getOpenedMode() = settings.getString(KEY_OPENED, MODE_NOT_OPENED)

    fun setOpened() {
        opened = true
        with(editor) {
            putString(KEY_OPENED, MODE_OPENED)
            commit()
            apply()
        }
    }
}