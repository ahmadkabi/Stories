package ahmadkabi.storyapp.ui.home.story

import ahmadkabi.storyapp.data.StoryRepository
import ahmadkabi.storyapp.data.source.remote.model.Story
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn

class StoryViewModel(private val storyRepository: StoryRepository) : ViewModel() {
    lateinit var token: String

    fun getStories(): LiveData<PagingData<Story>> = storyRepository.getStories().cachedIn(viewModelScope)

}