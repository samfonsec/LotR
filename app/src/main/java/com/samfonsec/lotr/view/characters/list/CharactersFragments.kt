package com.samfonsec.lotr.view.characters.list

import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.samfonsec.lotr.R
import com.samfonsec.lotr.data.model.Character
import com.samfonsec.lotr.databinding.FragCharactersBinding
import com.samfonsec.lotr.util.Constants.FIRST_PAGE
import com.samfonsec.lotr.util.EndlessRecyclerViewScrollListener
import com.samfonsec.lotr.util.extensions.hide
import com.samfonsec.lotr.util.extensions.show
import com.samfonsec.lotr.util.extensions.showErrorSnackbar
import com.samfonsec.lotr.util.extensions.viewBinding
import com.samfonsec.lotr.view.base.BaseFragment
import com.samfonsec.lotr.view.characters.details.CharacterDetailsBottomSheet
import com.samfonsec.lotr.viewModel.characters.CharactersViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class CharactersFragments : BaseFragment() {

    override val binding by viewBinding(FragCharactersBinding::inflate)

    private val viewModel: CharactersViewModel by viewModel()

    private val gridLayoutManager by lazy { GridLayoutManager(context, GRID_SPAN_COUNT) }

    private val charactersList: ArrayList<Character> = arrayListOf()

    private var lastLoadedPage = FIRST_PAGE

    private val endlessScrollListener by lazy {
        object : EndlessRecyclerViewScrollListener(gridLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                binding.rvCharacters.removeOnScrollListener(this)
                lastLoadedPage = page
                loadCharacters()
            }
        }
    }

    private val backToTopScrollListener by lazy {
        object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if ((recyclerView.layoutManager as GridLayoutManager).findFirstCompletelyVisibleItemPosition() == 0) {
                    binding.fabBackToTop.hide()
                } else if (dy < 0) {
                    binding.fabBackToTop.show()
                }
            }
        }
    }

    override fun buildUi() {
        loadCharacters()
        with(binding) {
            rvCharacters.layoutManager = gridLayoutManager
            rvCharacters.adapter = CharacterAdapter { onItemClicked(it) }.apply { submitList(charactersList) }
            fabBackToTop.setOnClickListener { showFirstPage() }
        }
        addListScrollListeners()
    }

    override fun subscribeUi() {
        with(viewModel) {
            onCharactersResult.observe(viewLifecycleOwner) { onCharactersLoaded(it) }
            onError.observe(viewLifecycleOwner) { onError() }
            onLoading.observe(viewLifecycleOwner) { onLoading(it) }
        }
    }

    private fun loadCharacters() {
        viewModel.getCharacters(lastLoadedPage)
    }

    private fun addListScrollListeners() {
        with(binding.rvCharacters) {
            addOnScrollListener(backToTopScrollListener)
            addOnScrollListener(endlessScrollListener)
        }
    }

    private fun onCharactersLoaded(characters: List<Character>) {
        charactersList += characters
        binding.rvCharacters.show()
        updateList(characters)
        addListScrollListeners()
    }

    private fun onError() {
        if (lastLoadedPage == FIRST_PAGE)
            showErrorView()
        else
            activity?.showErrorSnackbar(binding.fabBackToTop) { loadCharacters() }
    }

    private fun showErrorView() {
        with(binding.errorView) {
            root.show()
            btTryAgain.setOnClickListener {
                loadCharacters()
                root.hide()
            }
        }
    }

    private fun onLoading(show: Boolean) {
        binding.pbLoading.isVisible = show
    }

    private fun onItemClicked(character: Character) {
        activity?.run {
            CharacterDetailsBottomSheet.newInstance(character).apply {
                setQuotesAction {
                    showCharacterQuotes(it)
                    dismiss()
                }
            }.show(supportFragmentManager, TAG)
        }
    }

    private fun showCharacterQuotes(character: Character) {
        findNavController().navigate(
            R.id.action_characters_to_quotes,
            bundleOf(ARG_CHARACTER to character, ARG_TITLE to character.name)
        )
    }

    private fun updateList(characters: List<Character>) {
        val previousContentSize = charactersList.size
        charactersList += characters
        binding.rvCharacters.adapter?.notifyItemRangeInserted(previousContentSize, charactersList.size)
    }

    private fun showFirstPage() {
        resetList()
        viewModel.getFirstPage()
    }

    private fun resetList() {
        val previousListSize = charactersList.size
        charactersList.clear()
        endlessScrollListener.resetState()
        with(binding) {
            fabBackToTop.hide()
            rvCharacters.clearOnScrollListeners()
            rvCharacters.adapter?.notifyItemRangeRemoved(0, previousListSize)
        }
    }

    companion object {
        private const val TAG = "CharacterDetailsBottomSheet"
        private const val GRID_SPAN_COUNT = 2
        private const val ARG_CHARACTER = "character"
        private const val ARG_TITLE = "title"
    }
}
