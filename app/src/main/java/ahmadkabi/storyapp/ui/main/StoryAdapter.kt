package ahmadkabi.storyapp.ui.main

import ahmadkabi.storyapp.R
import ahmadkabi.storyapp.databinding.ItemStoryBinding
import ahmadkabi.storyapp.network.Story
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

    private val items = arrayListOf<Story>()

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

        Glide
            .with(holder.itemView.context)
            .load(item.photoUrl)
            .transform(
                CenterCrop(),
                RoundedCorners(holder.itemView.context.resources.getDimensionPixelSize(R.dimen.dp_30))
            )
            .into(holder.binding.ivItemPhoto)

        holder.binding.txAvatar.text = item.name[0].toString().uppercase()
        holder.binding.tvItemName.text = item.name

        holder.itemView.setOnClickListener {
            listener.onItemClickListener(item)
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

    fun resetItems(items: List<Story>?) {
        if (items != null) {
            if (this.items.isNotEmpty()) this.items.clear()

            this.items.addAll(items)

            notifyDataSetChanged()
        }
    }

    lateinit var listener: ItemListener

    interface ItemListener {
        fun onItemClickListener(item: Story)
    }

}