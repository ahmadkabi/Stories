package ahmadkabi.storyapp.helper

import ahmadkabi.storyapp.data.StoryRepository
import ahmadkabi.storyapp.data.source.remote.ApiConfig
import android.content.Context

object Injection {
    fun provideRepository(context: Context): StoryRepository {
        val token = UserPreference(context).getToken()!!
        val apiService = ApiConfig().getApiService()
        return StoryRepository(token, apiService)
    }
}