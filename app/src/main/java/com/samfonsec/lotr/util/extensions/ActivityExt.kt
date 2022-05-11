package com.samfonsec.lotr.util.extensions

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import androidx.biometric.BiometricManager
import androidx.viewbinding.ViewBinding
import com.google.android.material.snackbar.Snackbar
import com.samfonsec.lotr.R

inline fun <T : ViewBinding> Activity.viewBinding(crossinline bindingInflater: (LayoutInflater) -> T) =
    lazy(LazyThreadSafetyMode.NONE) { bindingInflater.invoke(layoutInflater) }

fun Activity.showErrorSnackbar(view: View, action: () -> Unit) {
    Snackbar.make(view, getString(R.string.error_short_message), Snackbar.LENGTH_INDEFINITE).apply {
        setAction(getString(R.string.error_action)) { action() }
        setBackgroundTint(getColor(R.color.lightBackgroundColor))
        setTextColor(getColor(R.color.primaryTextColor))
        setActionTextColor(getColor(R.color.primaryColor))
    }.show()
}

fun Activity.isFingerPrintAvailable() =
    BiometricManager
        .from(this)
        .canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_WEAK) == BiometricManager.BIOMETRIC_SUCCESS
