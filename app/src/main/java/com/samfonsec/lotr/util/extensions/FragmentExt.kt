package com.samfonsec.lotr.util.extensions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_WEAK
import androidx.biometric.BiometricManager.BIOMETRIC_SUCCESS
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.google.android.material.snackbar.Snackbar
import com.samfonsec.lotr.R

inline fun <T : ViewBinding> Fragment.viewBinding(crossinline bindingInflater: (LayoutInflater) -> T) =
    lazy(LazyThreadSafetyMode.NONE) { bindingInflater.invoke(layoutInflater) }

inline fun <reified T> Fragment.argument(key: String): Lazy<T> = lazy {
    val value = arguments?.get(key)
    if (value is T)
        value
    else
        throw IllegalArgumentException("Couldn't find extra with ky $key.")
}

inline fun <T : Fragment> T.withArgs(argsBuilder: Bundle. () -> Unit): T = this.apply {
    arguments = Bundle().apply(argsBuilder)
}

