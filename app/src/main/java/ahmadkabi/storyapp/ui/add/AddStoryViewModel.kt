package ahmadkabi.storyapp.ui.add

import ahmadkabi.storyapp.data.source.remote.ApiResponse
import ahmadkabi.storyapp.data.source.remote.model.AddStoryBody
import ahmadkabi.storyapp.data.source.remote.model.AddStoryResponse
import ahmadkabi.storyapp.data.source.remote.ApiConfig
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddStoryViewModel : ViewModel() {

    lateinit var token: String

    var body = MutableLiveData<AddStoryBody>()

    val addStory =
        Transformations.switchMap(body) {
            val result = MutableLiveData<ApiResponse<AddStoryResponse>>()
            val service = ApiConfig().getApiService().addStory(
                "Bearer $token",
                it.imageMultipart,
                it.description
            )

            service.enqueue(object : Callback<AddStoryResponse> {
                override fun onResponse(
                    call: Call<AddStoryResponse>,
                    response: Response<AddStoryResponse>
                ) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if (responseBody != null && !responseBody.error) {
                            result.value = ApiResponse.success(responseBody)
                        } else {
                            result.value = ApiResponse.success(null)
                        }
                    } else {
                        result.value = ApiResponse.success(null)
                    }
                }

                override fun onFailure(call: Call<AddStoryResponse>, t: Throwable) {
                    result.value = ApiResponse.error()
                }
            })

            result
        }
}