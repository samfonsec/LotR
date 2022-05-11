package com.samfonsec.lotr.viewModel.main.security

import com.samfonsec.lotr.util.Constants.KEY_AUTH_PIN
import com.samfonsec.lotr.viewModel.base.BaseAuthViewModel

class SecurityViewModel : BaseAuthViewModel() {

    fun removePin() {
        sharedPreferences.edit().remove(KEY_AUTH_PIN).apply()
        enableFingerprint(false)
    }
}