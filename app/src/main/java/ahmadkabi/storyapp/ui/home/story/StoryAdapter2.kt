package ahmadkabi.storyapp.ui.home.story

import ahmadkabi.storyapp.R
import ahmadkabi.storyapp.data.source.remote.model.Story
import ahmadkabi.storyapp.databinding.ItemStoryBinding
import android.app.Activity
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class StoryAdapter2 :
    PagingDataAdapter<Story, StoryAdapter2.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bind(data)
        }
    }

    inner class MyViewHolder(private val binding: ItemStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Story) {
            Glide
                .with(itemView.context)
                .load(data.photoUrl)
                .transform(
                    CenterCrop(),
                    RoundedCorners(itemView.context.resources.getDimensionPixelSize(R.dimen.dp_30))
                )
                .into(binding.ivItemPhoto)

            binding.txAvatar.text = data.name[0].toString().uppercase()
            binding.tvItemName.text = data.name

            itemView.setOnClickListener {
                val optionsCompat: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        itemView.context as Activity,
                        Pair(binding.ivItemPhoto, "photo"),
                        Pair(binding.llUser, "user")
                    )
                listener.onItemClickListener(data, optionsCompat)
            }

//            setAnimation(itemView, layoutPosition)
            
        }
    }

    private var lastPosition = -1
    private fun setAnimation(viewToAnimate: View, position: Int) {
        if (position > lastPosition) {
            val animation: Animation =
                AnimationUtils.loadAnimation(viewToAnimate.context, android.R.anim.fade_in)
            viewToAnimate.startAnimation(animation)
            lastPosition = position
        }
    }

    lateinit var listener: ItemListener

    interface ItemListener {
        fun onItemClickListener(item: Story, optionsCompat: ActivityOptionsCompat)
    }


    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Story>() {
            override fun areItemsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}