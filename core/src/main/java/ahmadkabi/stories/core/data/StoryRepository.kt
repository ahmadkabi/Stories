package ahmadkabi.stories.core.data

import ahmadkabi.stories.domain.model.Story
import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import ahmadkabi.stories.core.data.source.remote.model.LoginBody
import ahmadkabi.stories.core.data.source.remote.model.RegisterBody
import okhttp3.MultipartBody
import okhttp3.RequestBody
import ahmadkabi.stories.core.data.source.remote.ApiResponse
import ahmadkabi.stories.core.data.source.remote.ApiService
import ahmadkabi.stories.core.data.source.remote.toAddStory
import ahmadkabi.stories.core.data.source.remote.toRegister
import ahmadkabi.stories.core.data.source.remote.toUserModel

class StoryRepository(private val token: String, private val apiService: ApiService) {

    fun getStories(): LiveData<PagingData<Story>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                StoryPagingSource(token, apiService)
            }
        ).liveData
    }

    suspend fun getMappedStories() =
        ApiResponse.success(apiService.getMappedStories("Bearer $token").listStory)

    suspend fun login(body: LoginBody) =
        ApiResponse.success(
            apiService.login(body).loginResult.toUserModel()
        )

    suspend fun register(body: RegisterBody) =
        ApiResponse.success(apiService.register(body).toRegister())

    suspend fun addStory(
        file: MultipartBody.Part,
        description: RequestBody
    ) = ApiResponse.success(
        apiService.addStory(
            "Bearer $token",
            file,
            description
        ).toAddStory()
    )

}