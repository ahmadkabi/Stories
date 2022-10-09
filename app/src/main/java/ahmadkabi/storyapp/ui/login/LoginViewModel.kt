package ahmadkabi.storyapp.ui.login

import ahmadkabi.storyapp.data.source.remote.ApiResponse
import ahmadkabi.storyapp.data.source.remote.model.GetStoriesResponse
import ahmadkabi.storyapp.data.source.remote.model.Story
import ahmadkabi.storyapp.network.ApiConfig
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel : ViewModel() {

    lateinit var token: String

    private val _stories = MutableLiveData<ApiResponse<ArrayList<Story>>>()
    val stories: LiveData<ApiResponse<ArrayList<Story>>>
        get() = _stories

    fun fetchStories() {

        val service = ApiConfig().getApiService().getStories(
            "Bearer $token",
        )

        service.enqueue(object : Callback<GetStoriesResponse> {
            override fun onResponse(
                call: Call<GetStoriesResponse>,
                response: Response<GetStoriesResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && !responseBody.error) {
                        if (responseBody.listStory.isNotEmpty()) {
                            _stories.value = ApiResponse.success(responseBody.listStory)

                        } else {
                            _stories.value = ApiResponse.empty()
                        }
                    }
                } else {
                    _stories.value = ApiResponse.error()
                }

            }

            override fun onFailure(call: Call<GetStoriesResponse>, t: Throwable) {
                _stories.value = ApiResponse.error()
            }
        })

    }

}