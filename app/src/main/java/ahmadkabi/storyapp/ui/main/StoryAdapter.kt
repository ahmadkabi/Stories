package ahmadkabi.storyapp.ui.main

import ahmadkabi.storyapp.R
import ahmadkabi.storyapp.databinding.ItemStoryBinding
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class StoryAdapter :
    RecyclerView.Adapter<StoryAdapter.MyViewHolder>() {

    private val items = arrayListOf<String>()

    inner class MyViewHolder(val binding: ItemStoryBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): StoryAdapter.MyViewHolder {
        return MyViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_story,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: StoryAdapter.MyViewHolder, position: Int) {
        val item = items[position]

//        holder.binding.txName.text = item

        Glide
            .with(holder.itemView.context)
            .load("https://story-api.dicoding.dev/images/stories/photos-1664615087142_m3gc6pE4.jpg")
            .transform(
                CenterCrop(),
                RoundedCorners(holder.itemView.context.resources.getDimensionPixelSize(R.dimen.dp_30))
            )
            .into(holder.binding.imgStory)


        holder.itemView.setOnClickListener {
            listener.onItemClickListener(holder.adapterPosition, item)
        }

        setAnimation(holder.itemView, position)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onViewDetachedFromWindow(holder: StoryAdapter.MyViewHolder) {
        holder.itemView.rootView.clearAnimation()
    }

    private var lastPosition = -1
    private fun setAnimation(viewToAnimate: View, position: Int) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            val animation: Animation =
                AnimationUtils.loadAnimation(viewToAnimate.context, android.R.anim.fade_in)
            viewToAnimate.startAnimation(animation)
            lastPosition = position
        }
    }

    fun addItem(item: String) {
        this.items.add(item)
        notifyItemInserted(items.lastIndex)
    }

    fun addItems(items: List<String>) {
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    lateinit var listener: ItemListener

    interface ItemListener {
        fun onItemClickListener(position: Int, item: String)
    }

}