package ahmadkabi.storyapp.helper

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class ItemDecorVertical(private val topMarginFirstItem: Int, private val endMargin: Int, private val bottomMarginLastItem: Int, private val startMargin: Int, private val topMarginInterItem: Int) : RecyclerView.ItemDecoration() {

    constructor(margin: Int): this (margin, margin, margin, margin, margin)
    constructor(verticalMargin: Int, horizontalMargin: Int) : this(verticalMargin, horizontalMargin, verticalMargin, horizontalMargin, verticalMargin)
    constructor(topMargin: Int, endMargin: Int, bottomMargin: Int, startMargin: Int): this (topMargin, endMargin, bottomMargin, startMargin, topMargin)

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.top = topMarginFirstItem
        }else{
            outRect.top = topMarginInterItem
        }
        outRect.left = startMargin
        outRect.right = endMargin
        if (parent.getChildAdapterPosition(view) == parent.adapter?.itemCount?.minus(1)) {
            outRect.bottom = bottomMarginLastItem
        }
    }
}
