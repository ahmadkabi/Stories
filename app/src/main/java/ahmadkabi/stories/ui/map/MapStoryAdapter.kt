package ahmadkabi.stories.ui.map

import ahmadkabi.stories.R
import ahmadkabi.stories.databinding.ItemStoryMapBinding
import ahmadkabi.stories.domain.model.Story
import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.paging.PagingData
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class MapStoryAdapter :
    PagingDataAdapter<Story, MapStoryAdapter.MyViewHolder>(DIFF_CALLBACK) {

    private lateinit var selectedStory: Story

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            ItemStoryMapBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bind(data)
        }
    }

    inner class MyViewHolder(private val binding: ItemStoryMapBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Story) {

            Glide
                .with(itemView.context)
                .load(data.photoUrl)
                .transform(
                    CenterCrop(),
                    RoundedCorners(itemView.context.resources.getDimensionPixelSize(R.dimen.dp_30))
                )
                .into(binding.img)

            //todo reflect selected item to the ui
            binding.txAvatar.text = data.name[0].toString().uppercase()
            binding.tvItemName.text = data.name

            itemView.setOnClickListener {
                val optionsCompat: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        itemView.context as Activity,
                        Pair(binding.img, "photo"),
                        Pair(binding.llUser, "user")
                    )
                listener.onItemClickListener(data, optionsCompat)
            }

        }
    }

    private suspend fun updateItemAt(position: Int, newItem: Story) {

        val currentItems = snapshot().items.toMutableList()
        if (position in currentItems.indices) {
            currentItems[position] = newItem

            submitData(PagingData.from(currentItems))
        }
    }

    suspend fun setSelectedStory(story: Story){
        selectedStory = story

        updateItemAt(
            position = snapshot().items.indexOf(story),
            newItem = story
        )

    }

    lateinit var listener: ItemListener

    interface ItemListener {
        fun onItemClickListener(item: Story, optionsCompat: ActivityOptionsCompat)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Story>() {
            override fun areItemsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}