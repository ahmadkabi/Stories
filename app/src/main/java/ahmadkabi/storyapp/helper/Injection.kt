package ahmadkabi.storyapp.helper

import ahmadkabi.storyapp.data.QuoteDatabase
import ahmadkabi.storyapp.data.QuoteRepository
import ahmadkabi.storyapp.data.source.remote.ApiConfig
import android.content.Context

object Injection {
    fun provideRepository(context: Context): QuoteRepository {
        val database = QuoteDatabase.getDatabase(context)
        val apiService = ApiConfig().getApiService()
        return QuoteRepository(database, apiService)
    }
}