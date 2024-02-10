package ahmadkabi.stories.core.data.source.remote

import model.AddStoryResponse
import model.GetStoriesResponse
import model.LoginBody
import model.LoginResponse
import model.RegisterBody
import model.RegisterResponse
import model.StoryResponse
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface ApiService {

    @POST("/v1/register")
    suspend fun register(
        @Body body: RegisterBody
    ): RegisterResponse

    @POST("/v1/login")
    suspend fun login(
        @Body body: LoginBody
    ): LoginResponse

    @GET("/v1/stories")
    suspend fun getStories(
        @Header("Authorization") authorization: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): StoryResponse

    @GET("/v1/stories?location=1")
    suspend fun getMappedStories(
        @Header("Authorization") authorization: String
    ): GetStoriesResponse

    @Multipart
    @POST("/v1/stories")
    suspend fun addStory(
        @Header("Authorization") authorization: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody
    ): AddStoryResponse

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