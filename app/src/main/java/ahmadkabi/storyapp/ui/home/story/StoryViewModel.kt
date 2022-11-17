package ahmadkabi.storyapp.ui.home.story

import ahmadkabi.storyapp.data.StoryRepository
import ahmadkabi.storyapp.data.source.remote.model.Story
import ahmadkabi.storyapp.helper.Injection
import android.content.Context
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn

class StoryViewModel(storyRepository: StoryRepository) : ViewModel() {

    lateinit var token: String

    val story: LiveData<PagingData<Story>> =
        storyRepository.getStories().cachedIn(viewModelScope)

}


class ViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return StoryViewModel(Injection.provideRepository(context)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}