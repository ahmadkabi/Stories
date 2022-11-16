package ahmadkabi.storyapp.data

import ahmadkabi.storyapp.data.source.remote.ApiService
import ahmadkabi.storyapp.data.source.remote.model.QuoteResponseItem
import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData

class QuoteRepository(private val quoteDatabase: QuoteDatabase, private val apiService: ApiService) {
    fun getQuote(): LiveData<PagingData<QuoteResponseItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                QuotePagingSource(apiService)
            }
        ).liveData
    }
}