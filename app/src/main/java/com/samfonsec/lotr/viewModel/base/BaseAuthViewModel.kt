package com.samfonsec.lotr.viewModel.base

import android.content.SharedPreferences
import com.samfonsec.lotr.util.Constants.KEY_AUTH_PIN
import com.samfonsec.lotr.util.Constants.KEY_ENABLE_FINGERPRINT
import org.koin.java.KoinJavaComponent.inject


abstract class BaseAuthViewModel : BaseViewModel() {

    val sharedPreferences by inject(SharedPreferences::class.java)

    fun hasPin() = sharedPreferences.contains(KEY_AUTH_PIN)

    fun savePin(pin: String) = sharedPreferences.edit().putString(KEY_AUTH_PIN, pin).apply()

    fun enableFingerprint(enable: Boolean) = sharedPreferences.edit().putBoolean(KEY_ENABLE_FINGERPRINT, enable).apply()

    fun isValidPin(pin: String) = pin == sharedPreferences.getString(KEY_AUTH_PIN, "")
}