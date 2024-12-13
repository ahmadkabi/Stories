package ahmadkabi.stories.ui.map

import ahmadkabi.stories.R
import ahmadkabi.stories.databinding.ItemStoryMapBinding
import ahmadkabi.stories.databinding.ItemStoryMapSelectedBinding
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
    PagingDataAdapter<Story, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    private var selectedStory: Story? = null
    lateinit var listener: ItemListener

    inner class NormalViewHolder(private val binding: ItemStoryMapBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(story: Story) {

            Glide
                .with(itemView.context)
                .load(story.photoUrl)
                .transform(
                    CenterCrop(),
                    RoundedCorners(itemView.context.resources.getDimensionPixelSize(R.dimen.dp_30))
                )
                .into(binding.img)

            binding.txAvatar.text = story.name[0].toString().uppercase()
            binding.tvItemName.text = story.name

            itemView.setOnClickListener {
                val optionsCompat: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        itemView.context as Activity,
                        Pair(binding.img, "photo"),
                        Pair(binding.llUser, "user")
                    )
                listener.onItemClickListener(story, optionsCompat)
            }
        }
    }


    inner class SelectedViewHolder(private val binding: ItemStoryMapSelectedBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(story: Story) {

            Glide
                .with(itemView.context)
                .load(story.photoUrl)
                .transform(
                    CenterCrop(),
                    RoundedCorners(itemView.context.resources.getDimensionPixelSize(R.dimen.dp_30))
                )
                .into(binding.img)

            binding.txAvatar.text = story.name[0].toString().uppercase()
            binding.tvItemName.text = story.name

            itemView.setOnClickListener {
                val optionsCompat: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        itemView.context as Activity,
                        Pair(binding.img, "photo"),
                        Pair(binding.llUser, "user")
                    )
                listener.onItemClickListener(story, optionsCompat)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if(snapshot().items[position] == selectedStory){
            VIEW_TYPE_SELECTED
        }else{
            VIEW_TYPE_NORMAL
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding =
            ItemStoryMapBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
//            holder.bind(data)
        }
    }


    inner class MyViewHolder(private val binding: ItemStoryMapBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Story) {

            if (data == selectedStory) {
                binding.img.setBackgroundResource(R.drawable.bg_corner30_teal_overlay50)
            } else {
                binding.img.setBackgroundResource(R.drawable.bg_corner30_primary_overlay50)
            }
//            todo make selected item larger

            Glide
                .with(itemView.context)
                .load(data.photoUrl)
                .transform(
                    CenterCrop(),
                    RoundedCorners(itemView.context.resources.getDimensionPixelSize(R.dimen.dp_30))
                )
                .into(binding.img)

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

    suspend fun setSelectedStory(story: Story) {
        selectedStory = story

        updateItemAt(
            position = snapshot().items.indexOf(story),
            newItem = story
        )
    }

    private suspend fun updateItemAt(position: Int, newItem: Story) {
        val currentItems = snapshot().items.toMutableList()
        if (position in currentItems.indices) {
            currentItems[position] = newItem
            submitData(PagingData.from(currentItems))
            notifyItemRangeChanged(0, currentItems.size)
        }
    }


    interface ItemListener {
        fun onItemClickListener(item: Story, optionsCompat: ActivityOptionsCompat)
    }

    companion object {
        const val VIEW_TYPE_NORMAL = 1
        const val VIEW_TYPE_SELECTED = 2

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