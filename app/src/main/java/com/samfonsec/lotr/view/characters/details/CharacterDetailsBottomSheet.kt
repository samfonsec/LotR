package com.samfonsec.lotr.view.characters.details

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.browser.customtabs.CustomTabsIntent
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.samfonsec.lotr.R
import com.samfonsec.lotr.data.model.Character
import com.samfonsec.lotr.databinding.BsCharacterDetailsBinding
import com.samfonsec.lotr.util.extensions.argument
import com.samfonsec.lotr.util.extensions.hide
import com.samfonsec.lotr.util.extensions.viewBinding
import com.samfonsec.lotr.util.extensions.withArgs


class CharacterDetailsBottomSheet : BottomSheetDialogFragment() {

    private val binding by viewBinding(BsCharacterDetailsBinding::inflate)

    private val character by argument<Character>(ARG_CHARACTER)

    private var onQuotesClicked: ((Character) -> Unit)? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            ivClose.setOnClickListener { dismiss() }
            btQuotes.setOnClickListener { onQuotesClicked?.invoke(character) }
            character.wikiUrl?.let { url ->
                btWiki.setOnClickListener { openUrl(url) }
            } ?: btWiki.hide()
            setupCharacterInformation()
        }
    }

    fun setQuotesAction(action: (Character) -> Unit) {
        onQuotesClicked = action
    }

    private fun setupCharacterInformation() = character.run {
        with(binding) {
            if (gender.isNotNanOrEmpty() && race.isNotNanOrEmpty())
                "$gender $race".let {
                    tvRace.text = it
                }
            tvName.text = name
            tvBirth.setTextOrHide(R.string.birth_format, birth)
            tvDeath.setTextOrHide(R.string.death_format, death)
            tvSpouse.setTextOrHide(R.string.spouse_format, spouse)
            tvHeight.setTextOrHide(R.string.height_format, height)
            tvHair.setTextOrHide(R.string.hair_format, hair)
            tvRealm.setTextOrHide(R.string.realm_format, realm)
        }
    }

    private fun openUrl(url: String) = context?.run {
        CustomTabsIntent.Builder().build().launchUrl(this, Uri.parse(url))
    }

    private fun String.isNotNanOrEmpty() = isNotEmpty() && !equals(NAN)

    private fun String.takeIfNotEmpty() = takeIf { it.isNotNanOrEmpty() }

    private fun TextView.setTextOrHide(@StringRes formatResId: Int, value: String) {
        value.takeIfNotEmpty()?.let {
            text = getString(formatResId, value)
        } ?: hide()
    }

    companion object {
        private const val ARG_CHARACTER = "arg_character"
        private const val NAN = "NaN"

        fun newInstance(character: Character) = CharacterDetailsBottomSheet().withArgs {
            putParcelable(ARG_CHARACTER, character)
        }
    }
}
