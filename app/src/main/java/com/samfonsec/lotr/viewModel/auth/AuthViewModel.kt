package com.samfonsec.lotr.viewModel.auth

import com.samfonsec.lotr.viewModel.base.BaseAuthViewModel
import com.samfonsec.lotr.util.Constants.KEY_ENABLE_FINGERPRINT

class AuthViewModel : BaseAuthViewModel() {

    fun isFingerPrintEnabled() = sharedPreferences.getBoolean(KEY_ENABLE_FINGERPRINT, false)
}