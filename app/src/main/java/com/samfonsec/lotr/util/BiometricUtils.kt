package com.samfonsec.lotr.util

import androidx.biometric.BiometricPrompt
import androidx.biometric.BiometricPrompt.ERROR_NEGATIVE_BUTTON
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.samfonsec.lotr.R

class BiometricUtils(private val activity: FragmentActivity) {

    private var biometricPrompt: BiometricPrompt? = null
    private var onSuccess: (() -> Unit)? = null
    private var onError: (() -> Unit)? = null


    fun onSuccess(action: () -> Unit): BiometricUtils {
        onSuccess = action
        return this
    }

    fun showDialog() {
        biometricPrompt = BiometricPrompt(
            activity,
            ContextCompat.getMainExecutor(activity),
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    onSuccess?.invoke()
                }

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    if (errorCode != ERROR_NEGATIVE_BUTTON)
                        onError?.invoke()
                }
            }
        )
        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(activity.getString(R.string.app_name))
            .setNegativeButtonText(activity.getString(R.string.action_insert_pin))
            .setConfirmationRequired(false)
            .build()

        biometricPrompt?.authenticate(promptInfo)
    }
}