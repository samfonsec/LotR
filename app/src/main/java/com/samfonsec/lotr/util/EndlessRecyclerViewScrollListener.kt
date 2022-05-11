package com.samfonsec.lotr.util

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.samfonsec.lotr.util.Constants.FIRST_PAGE

abstract class EndlessRecyclerViewScrollListener : RecyclerView.OnScrollListener {

    private var visibleThreshold = 5
    private var currentPage = FIRST_PAGE
    private var previousTotalItemCount = 0
    private var loading = true
    private val startingPageIndex = FIRST_PAGE
    private var layoutManager: RecyclerView.LayoutManager

    constructor(layoutManager: LinearLayoutManager) {
        this.layoutManager = layoutManager
    }

    constructor(layoutManager: GridLayoutManager) {
        this.layoutManager = layoutManager
        visibleThreshold *= layoutManager.spanCount
    }

    override fun onScrolled(view: RecyclerView, dx: Int, dy: Int) {
        val totalItemCount = layoutManager.itemCount
        val lastVisibleItemPosition = getLastVisibleItem()

            if (totalItemCount < previousTotalItemCount) {
                this.currentPage = this.startingPageIndex
                this.previousTotalItemCount = totalItemCount
                if (totalItemCount == 0) {
                    this.loading = true
                }
            }

        if (loading && totalItemCount > previousTotalItemCount) {
            loading = false
            previousTotalItemCount = totalItemCount
        }

        if (!loading && lastVisibleItemPosition + visibleThreshold > totalItemCount) {
            currentPage++
            onLoadMore(currentPage, totalItemCount, view)
            loading = true
        }
    }

    abstract fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView)

    private fun getLastVisibleItem() = when (layoutManager) {
        is GridLayoutManager -> (layoutManager as GridLayoutManager).findLastVisibleItemPosition()
        is LinearLayoutManager -> (layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
        else -> 0
    }

    fun resetState() {
        this.currentPage = this.startingPageIndex
        this.previousTotalItemCount = 0
        this.loading = true
    }
}