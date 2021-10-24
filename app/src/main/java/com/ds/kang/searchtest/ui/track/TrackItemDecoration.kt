package com.ds.kang.searchtest.ui.track

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class TrackItemDecoration(private val rect: Rect) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        outRect.top = rect.top
        outRect.bottom = rect.bottom
        outRect.left = rect.left
        outRect.right = rect.right
    }
}