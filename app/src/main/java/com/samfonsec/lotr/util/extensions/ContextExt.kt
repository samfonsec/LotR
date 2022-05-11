package com.samfonsec.lotr.util.extensions

import android.content.Context
import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.samfonsec.lotr.R

fun Context.showSnackbar(rootView: View, message: String) {
    Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT).apply {
        view.setMargins(bottom = dimensFromAttr(R.attr.actionBarSize))
    }.show()
}

fun Context.dimensFromAttr(attribute: Int): Int {
    val attributes = obtainStyledAttributes(intArrayOf(attribute))
    val dimension = attributes.getDimensionPixelSize(0, 0)
    attributes.recycle()
    return dimension
}