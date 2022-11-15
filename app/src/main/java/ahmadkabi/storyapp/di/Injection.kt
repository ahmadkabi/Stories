package ahmadkabi.storyapp.di

import ahmadkabi.storyapp.data.source.Repository
import ahmadkabi.storyapp.data.source.remote.ApiConfig

object Injection {
    fun provideRepository(): Repository {
        val apiService = ApiConfig().getApiService()
        return Repository(apiService)
    }
}