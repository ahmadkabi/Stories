package ahmadkabi.storyapp.ui.register

import ahmadkabi.storyapp.data.source.remote.ApiResponse
import ahmadkabi.storyapp.data.source.remote.model.*
import ahmadkabi.storyapp.network.ApiConfig
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel : ViewModel() {

    var body = MutableLiveData<RegisterBody>()

    val login =
        Transformations.switchMap(body) {
            val result = MutableLiveData<ApiResponse<RegisterResponse>>()

            if (body.value != null) {
                val service = ApiConfig().getApiService().register(body.value!!)

                service.enqueue(object : Callback<RegisterResponse> {
                    override fun onResponse(
                        call: Call<RegisterResponse>,
                        response: Response<RegisterResponse>
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

                    override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                        result.value = ApiResponse.error()
                    }
                })
            } else {
                result.value = ApiResponse.error()
            }

            result

        }

}