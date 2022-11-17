package ahmadkabi.storyapp.data.source.remote

import ahmadkabi.storyapp.data.source.remote.model.*
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
    suspend fun getStories(
        @Header("Authorization") authorization: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): StoryResponse

    @GET("/v1/stories?location=1")
    fun getMappedStories(
        @Header("Authorization") authorization: String
    ): Call<GetStoriesResponse>

    @Multipart
    @POST("/v1/stories")
    fun addStory(
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
//            .baseUrl("https://quote-api.dicoding.dev/")
            .baseUrl("https://story-api.dicoding.dev/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return retrofit.create(ApiService::class.java)
    }
}