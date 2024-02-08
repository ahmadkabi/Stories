package ahmadkabi.stories.ui.home.story

import ahmadkabi.stories.R
import ahmadkabi.stories.domain.model.Story
import ahmadkabi.stories.databinding.ItemStoryBinding
import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class StoryListAdapter :
    PagingDataAdapter<ahmadkabi.stories.domain.model.Story, StoryListAdapter.MyViewHolder>(DIFF_CALLBACK) {

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
        fun bind(data: ahmadkabi.stories.domain.model.Story) {


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

        }
    }

    lateinit var listener: ItemListener

    interface ItemListener {
        fun onItemClickListener(item: ahmadkabi.stories.domain.model.Story, optionsCompat: ActivityOptionsCompat)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ahmadkabi.stories.domain.model.Story>() {
            override fun areItemsTheSame(oldItem: ahmadkabi.stories.domain.model.Story, newItem: ahmadkabi.stories.domain.model.Story): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ahmadkabi.stories.domain.model.Story, newItem: ahmadkabi.stories.domain.model.Story): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}