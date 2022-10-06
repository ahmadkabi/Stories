package ahmadkabi.storyapp.network

import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface ApiService {

    @POST("/v1/register")
    fun register(
        @Body body: RegisterBody
    ): Call<RegisterResponse>

    @POST("/v1/login")
    fun login(
        @Body body: LoginBody
    ): Call<LoginResponse>

    @GET("/v1/stories")
    fun getStories(
        @Header("Authorization") authorization: String
    ): Call<GetStoriesResponse>

    @Multipart
    @POST("/v1/stories")
    fun uploadImage(
        @Header("Authorization") authorization: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody
    ): Call<AddStoryResponse>

}

class ApiConfig {
    fun getApiService(): ApiService {
        val loggingInterceptor =
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://story-api.dicoding.dev/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return retrofit.create(ApiService::class.java)
    }
}

data class AddStoryResponse(
    val error: Boolean,
    val message: String
)

data class RegisterBody(
    val name: String,
    val email: String,
    val password: String
)

data class RegisterResponse(
    val error: Boolean,
    val message: String
)

data class LoginBody(
    val email: String,
    val password: String
)

data class LoginResponse(
    val error: Boolean,
    val message: String,
    val loginResult: LoginResult
)

data class LoginResult(
    val userId: String,
    val name: String,
    val token: String
)

data class GetStoriesResponse(
    val error: Boolean,
    val message: String,
    val listStory: ArrayList<Story>
)

data class Story(
    val id: String,
    val name: String,
    val description: String,
    val photoUrl: String,
    val createdAt: Any,
    val lat: Float,
    val lon: Float
)
