package com.example.floatingicon.config

import android.content.Context
import android.content.SharedPreferences

class FloatingIconConfig(private val context: Context) {

    private val preferences: SharedPreferences = context.getSharedPreferences(
        PREFS_NAME,
        Context.MODE_PRIVATE
    )

    var isEnabled: Boolean
        get() = preferences.getBoolean(KEY_ENABLED, false)
        set(value) = preferences.edit().putBoolean(KEY_ENABLED, value).apply()

    var iconOpacity: Float
        get() = preferences.getFloat(KEY_OPACITY, 1.0f)
        set(value) = preferences.edit().putFloat(KEY_OPACITY, value).apply()

    var iconSize: Int
        get() = preferences.getInt(KEY_SIZE, 56)
        set(value) = preferences.edit().putInt(KEY_SIZE, value).apply()

    var positionX: Int
        get() = preferences.getInt(KEY_POSITION_X, 20)
        set(value) = preferences.edit().putInt(KEY_POSITION_X, value).apply()

    var positionY: Int
        get() = preferences.getInt(KEY_POSITION_Y, 100)
        set(value) = preferences.edit().putInt(KEY_POSITION_Y, value).apply()

    companion object {
        private const val PREFS_NAME = "FloatingIconConfig"
        private const val KEY_ENABLED = "is_enabled"
        private const val KEY_OPACITY = "icon_opacity"
        private const val KEY_SIZE = "icon_size"
        private const val KEY_POSITION_X = "position_x"
        private const val KEY_POSITION_Y = "position_y"
    }
}
