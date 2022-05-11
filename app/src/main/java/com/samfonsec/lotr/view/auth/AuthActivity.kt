package com.samfonsec.lotr.view.auth

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import com.samfonsec.lotr.R
import com.samfonsec.lotr.databinding.ActAuthBinding
import com.samfonsec.lotr.util.BiometricUtils
import com.samfonsec.lotr.util.Constants.LOSE_FOCUS_DELAY
import com.samfonsec.lotr.util.Constants.PIN_MAX_LENGTH
import com.samfonsec.lotr.util.extensions.*
import com.samfonsec.lotr.view.main.MainActivity
import com.samfonsec.lotr.viewModel.auth.AuthViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class AuthActivity : AppCompatActivity() {

    private val binding by viewBinding(ActAuthBinding::inflate)

    private val viewModel: AuthViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        if (viewModel.hasPin())
            buildUi()
        else
            launchMainActivity()
    }

    private fun launchMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun buildUi() {
        with(binding) {
            etPassword.doOnTextChanged { text, _, _, _ -> onPinTextChanged(text) }
            tvFingerprint.setOnClickListener { showFingerprintDialog() }
            tvFingerprint.isVisible = isFingerPrintAvailable()

            if (isFingerPrintAvailable() && viewModel.isFingerPrintEnabled())
                delay(DIALOG_DELAY) { showFingerprintDialog() }
            else {
                etPassword.showSoftKeyboard()
                tvFingerprint.hide()
            }
        }
    }

    private fun onPinTextChanged(text: CharSequence?) {
        if (text?.length == PIN_MAX_LENGTH)
            validatePin(text.toString())
        else if (binding.tiPassword.error != null)
            binding.tiPassword.error = null
    }

    private fun showFingerprintDialog() {
        BiometricUtils(this)
            .onSuccess { launchMainActivity() }
            .showDialog()
    }

    private fun validatePin(pin: String?) {
        pin?.let {
            if (viewModel.isValidPin(it))
                launchMainWithDelay()
            else
                binding.tiPassword.error = getString(R.string.invalid_pin)
        }
    }

    private fun launchMainWithDelay() {
        delay {
            with(binding.etPassword) {
                clearFocus()
                hideSoftKeyboard()
            }
            launchMainActivity()
        }
    }

    private fun delay(delayTime: Long = LOSE_FOCUS_DELAY, action: () -> Unit) {
        Handler(Looper.getMainLooper()).postDelayed(action, delayTime)
    }


    companion object {
        private const val DIALOG_DELAY = 150L
    }
}