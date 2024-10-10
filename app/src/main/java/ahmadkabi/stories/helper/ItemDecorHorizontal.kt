package ahmadkabi.stories.helper

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class ItemDecorHorizontal(
    private val leftMarginFirstItem: Int,
    private val topMargin: Int,
    private val rightMarginLastItem: Int,
    private val bottomMargin: Int,
    private val leftMarginInterItem: Int
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.left = leftMarginFirstItem
        } else {
            outRect.left = leftMarginInterItem
        }
        outRect.left = bottomMargin
        outRect.right = topMargin
        if (parent.getChildAdapterPosition(view) == parent.adapter?.itemCount?.minus(1)) {
            outRect.right = rightMarginLastItem
        }
    }
}
