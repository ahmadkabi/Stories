package ahmadkabi.storyapp.widget

import ahmadkabi.storyapp.R
import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView

class CheckableImageView(context: Context, attrs: AttributeSet) :
    AppCompatImageView(context, attrs) {

    private var isChecked: Boolean
    private var checkedDrawable: Int
    private var uncheckedDrawable: Int
    private lateinit var callback: (CheckableImageView) -> Unit

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.StateImageView,
            0,
            0
        ).apply {

            isChecked = getBoolean(R.styleable.StateImageView_isChecked, false)
            checkedDrawable = getResourceId(R.styleable.StateImageView_checkedDrawable, 0)
            uncheckedDrawable = getResourceId(R.styleable.StateImageView_uncheckedDrawable, 0)

        }

        setImageResource()

        setOnClickListener {
            isChecked = !isChecked
            setImageResource()
            callback(this)
        }
    }

    fun isChecked() = isChecked

    /** update isChecked programmatically */
    fun check(isChecked: Boolean) {
        this.isChecked = isChecked
        setImageResource()
        callback(this)
    }

    private fun setImageResource() {
        if (isChecked) setImageResource(checkedDrawable)
        else setImageResource(uncheckedDrawable)
    }

    fun setOnClickCallback(callback: (CheckableImageView) -> Unit) {
        this.callback = callback
    }

}