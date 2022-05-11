package com.samfonsec.lotr.view.main.security

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.DialogFragment
import com.samfonsec.lotr.R
import com.samfonsec.lotr.databinding.DialogRequestPinBinding
import com.samfonsec.lotr.util.Constants.PIN_MAX_LENGTH
import com.samfonsec.lotr.util.extensions.*

class HandlePinDialog : DialogFragment() {

    private val binding by viewBinding(DialogRequestPinBinding::inflate)

    private var onConfirm: ((String) -> Unit)? = null
    private val hasPin by argument<Boolean>(ARG_HAS_PIN)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupWindow()
        buildUi()
    }

    override fun dismiss() {
        binding.etPassword.hideSoftKeyboard()
        super.dismiss()
    }

    private fun setupWindow() {
        if (activity?.isFinishing == false) {
            dialog?.window?.run {
                setLayout(MATCH_PARENT, WRAP_CONTENT)
                setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            }
        }
    }

    private fun buildUi() {
        with(binding) {
            etPassword.showSoftKeyboard()
            etPassword.doOnTextChanged { text, _, _, _ ->
                btConfirm.isEnabled = (text?.length == PIN_MAX_LENGTH).also { pinFilled ->
                    if (!pinFilled && tiPassword.error != null)
                        tiPassword.error = null
                }
            }
            btConfirm.setOnClickListener { onButtonClicked() }
            if (hasPin)
                tiPassword.helperText = null
        }
    }

    private fun onButtonClicked() {
        onConfirm?.invoke(binding.etPassword.text.toString())
    }


    fun onConfirm(action: (String) -> Unit) {
        onConfirm = action
    }

    fun setError() {
        binding.tiPassword.error = getString(R.string.invalid_pin)
    }

    companion object {
        private const val ARG_HAS_PIN = "arg_has_pin"

        fun newInstance(hasPin: Boolean) = HandlePinDialog().withArgs {
            putBoolean(ARG_HAS_PIN, hasPin)
        }
    }
}