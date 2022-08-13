package com.albatros.kplanner.model.repo

import android.content.SharedPreferences

class PreferencesRepository(private val settings: SharedPreferences) {

    private val editor = settings.edit()
    var opened = getOpenedMode() == MODE_OPENED

    companion object {
        const val KEY_OPENED = "key_opened"
        const val MODE_OPENED = "mode_opened"
        const val MODE_NOT_OPENED = "mode_not_opened"
        const val NIGHT_MODE = "night_mode"
        const val LIGHT_MODE = "light_mode"

        const val KEY_EMAIL = "key_email"
        const val KEY_PASSWORD = "key_password"
        const val KEY_MODE = "key_mode"
    }

    enum class UiMode {
        LIGHT, DARK
    }

    fun setMode(mode: UiMode) = if (mode == UiMode.LIGHT) setLightMode() else setNightMode()

    private fun setNightMode() {
        with(editor) {
            putString(KEY_MODE, NIGHT_MODE)
            commit()
            apply()
        }
    }

    fun getUiMode() =
        if (settings.getString(KEY_MODE, LIGHT_MODE) == LIGHT_MODE) UiMode.LIGHT else UiMode.DARK

    private fun setLightMode() {
        with(editor) {
            putString(KEY_MODE, LIGHT_MODE)
            commit()
            apply()
        }
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
            apply()
        }
    }
}