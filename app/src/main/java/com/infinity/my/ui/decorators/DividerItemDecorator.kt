package com.infinity.my.ui.decorators

import android.content.Context
import android.graphics.Rect
import android.util.TypedValue
import android.view.View
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView

class DividerItemDecorator(@NonNull context: Context) : RecyclerView.ItemDecoration() {

    private val mPadding: Int = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        PADDING_IN_DIPS.toFloat(),
        context.resources.displayMetrics
    ).toInt()

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val itemPosition = parent.getChildAdapterPosition(view)
        if (itemPosition == RecyclerView.NO_POSITION) {
            return
        }
        val adapter = parent.adapter
        if (adapter!!.itemCount % 4 == 0) {
            for (i in 0..4)
                if (itemPosition == adapter.itemCount - i) outRect.bottom = mPadding
        } else {
            for (i in 0 until (adapter.itemCount % 4) + 1)
                if (itemPosition == adapter.itemCount - i) outRect.bottom = mPadding
        }
    }

    companion object {
        private const val PADDING_IN_DIPS = 50
    }
}