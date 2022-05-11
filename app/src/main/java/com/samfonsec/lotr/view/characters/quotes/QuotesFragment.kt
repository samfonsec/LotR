package com.samfonsec.lotr.view.characters.quotes

import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.samfonsec.lotr.data.model.Character
import com.samfonsec.lotr.data.model.Quote
import com.samfonsec.lotr.databinding.FragQuoteBinding
import com.samfonsec.lotr.util.Constants
import com.samfonsec.lotr.util.EndlessRecyclerViewScrollListener
import com.samfonsec.lotr.util.extensions.hide
import com.samfonsec.lotr.util.extensions.show
import com.samfonsec.lotr.util.extensions.showErrorSnackbar
import com.samfonsec.lotr.util.extensions.viewBinding
import com.samfonsec.lotr.view.base.BaseFragment
import com.samfonsec.lotr.viewModel.characters.QuotesViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class QuotesFragment : BaseFragment() {

    override val binding by viewBinding(FragQuoteBinding::inflate)

    private val viewModel: QuotesViewModel by viewModel()

    private var character: Character? = null

    private val layoutManager by lazy { LinearLayoutManager(context) }

    private val quotesList: ArrayList<Quote> = arrayListOf()

    private var lastLoadedPage = Constants.FIRST_PAGE

    private val endlessScrollListener by lazy {
        object : EndlessRecyclerViewScrollListener(layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                binding.rvQuotes.removeOnScrollListener(this)
                lastLoadedPage = page
                loadQuotes()
            }
        }
    }

    private val backToTopScrollListener by lazy {
        object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if ((recyclerView.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition() == 0) {
                    binding.fabBackToTop.hide()
                } else if (dy < 0) {
                    binding.fabBackToTop.show()
                }
            }
        }
    }

    override fun buildUi() {
        character = arguments?.getParcelable(ARG_CHARACTER)
        loadQuotes()
        with(binding) {
            rvQuotes.layoutManager = layoutManager
            rvQuotes.adapter = QuoteAdapter().apply { submitList(quotesList) }
            fabBackToTop.setOnClickListener { showFirstPage() }
        }
        addListScrollListeners()
    }

    override fun subscribeUi() {
        with(viewModel) {
            onQuotesResult.observe(viewLifecycleOwner) { onQuotesLoaded(it) }
            onError.observe(viewLifecycleOwner) { onError() }
            onLoading.observe(viewLifecycleOwner) { onLoading(it) }
        }
    }

    private fun loadQuotes() {
        character?.run {
            viewModel.getQuotes(_id, lastLoadedPage)
        } ?: showErrorView()
    }

    private fun addListScrollListeners() {
        with(binding.rvQuotes) {
            addOnScrollListener(backToTopScrollListener)
            addOnScrollListener(endlessScrollListener)
        }
    }

    private fun onQuotesLoaded(quotes: List<Quote>) {
        if (quotes.isEmpty()) {
            showErrorView(enableAttempt = false)
        } else {
            quotesList += quotes
            binding.rvQuotes.show()
            updateList(quotes)
            addListScrollListeners()
        }
    }

    private fun onError() {
        if (lastLoadedPage == Constants.FIRST_PAGE)
            showErrorView()
        else
            activity?.showErrorSnackbar(binding.fabBackToTop) { loadQuotes() }
    }

    private fun showErrorView(enableAttempt: Boolean = true) {
        with(binding.errorView) {
            root.show()
            if (enableAttempt) {
                btTryAgain.show()
                btTryAgain.setOnClickListener {
                    loadQuotes()
                    root.hide()
                }
            } else {
                btTryAgain.hide()
            }
        }
    }

    private fun onLoading(show: Boolean) {
        binding.pbLoading.isVisible = show
    }


    private fun updateList(quotes: List<Quote>) {
        val previousContentSize = quotesList.size
        quotesList += quotes
        binding.rvQuotes.adapter?.notifyItemRangeInserted(previousContentSize, quotesList.size)
    }

    private fun showFirstPage() {
        resetList()
        viewModel.getFirstPage()
    }

    private fun resetList() {
        val previousListSize = quotesList.size
        quotesList.clear()
        endlessScrollListener.resetState()
        with(binding) {
            fabBackToTop.hide()
            rvQuotes.clearOnScrollListeners()
            rvQuotes.adapter?.notifyItemRangeRemoved(0, previousListSize)
        }
    }

    companion object {
        private const val ARG_CHARACTER = "character"
    }
}
