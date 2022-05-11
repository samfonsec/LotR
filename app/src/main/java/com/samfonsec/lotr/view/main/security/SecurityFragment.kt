package com.samfonsec.lotr.view.main.security

import com.samfonsec.lotr.R
import com.samfonsec.lotr.databinding.FragSecurityBinding
import com.samfonsec.lotr.util.extensions.hide
import com.samfonsec.lotr.util.extensions.show
import com.samfonsec.lotr.util.extensions.showSnackbar
import com.samfonsec.lotr.util.extensions.viewBinding
import com.samfonsec.lotr.view.base.BaseFragment
import com.samfonsec.lotr.viewModel.main.security.SecurityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SecurityFragment : BaseFragment() {

    override val binding by viewBinding(FragSecurityBinding::inflate)

    private val viewModel: SecurityViewModel by viewModel()

    override fun buildUi() {
        if (viewModel.hasPin())
            setRemovePinView()
        else
            showCreatePinView()
    }

    private fun showCreatePinView() {
        with(binding) {
            tvLabel.text = getString(R.string.label_create_pin)
            btPin.text = getString(R.string.action_create_pin)
            cbFingerPrint.show()
            btPin.setOnClickListener { showCreatePinDialog() }
        }
    }

    private fun setRemovePinView() {
        with(binding) {
            tvLabel.text = getString(R.string.label_remove_pin)
            btPin.text = getString(R.string.action_remove_pin)
            cbFingerPrint.hide()
            btPin.setOnClickListener { showRemovePinDialog() }
        }
    }

    private fun showCreatePinDialog() {
        HandlePinDialog.newInstance(false).apply {
            onConfirm { pin ->
                viewModel.savePin(pin)
                viewModel.enableFingerprint(binding.cbFingerPrint.isChecked)
                dismiss()
                setRemovePinView()
                context?.showSnackbar(binding.root, getString(R.string.message_pin_created))
            }
        }.show(childFragmentManager, TAG)
    }

    private fun showRemovePinDialog() {
        HandlePinDialog.newInstance(true).apply {
            onConfirm { pin ->
                if (viewModel.isValidPin(pin)) {
                    viewModel.removePin()
                    dismiss()
                    showCreatePinView()
                    context?.showSnackbar(binding.root, getString(R.string.message_pin_removed))
                } else {
                    setError()
                }
            }
        }.show(childFragmentManager, TAG)
    }

    companion object {
        private const val TAG = "HomeFragment"
    }
}