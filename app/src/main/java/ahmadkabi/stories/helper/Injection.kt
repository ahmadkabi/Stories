package ahmadkabi.stories.helper

import ahmadkabi.stories.core.data.StoryRepository
import ahmadkabi.stories.core.data.source.remote.ApiConfig
import android.content.Context

object Injection {
    fun provideRepository(context: Context): StoryRepository {
        val token = UserPreference(context).getToken() ?: ""
        val apiService = ApiConfig().getApiService()
        return StoryRepository(token, apiService)
    }
}