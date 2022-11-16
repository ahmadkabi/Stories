package ahmadkabi.storyapp.di

import ahmadkabi.storyapp.data.source.Repository
import ahmadkabi.storyapp.data.source.remote.ApiConfig
import ahmadkabi.storyapp.helper.UserPreference
import android.content.Context

object Injection {
    fun provideRepository(context: Context): Repository {
        val token = UserPreference(context).getToken()!!
        val apiService = ApiConfig().getApiService()
        return Repository(token, apiService)
    }
}