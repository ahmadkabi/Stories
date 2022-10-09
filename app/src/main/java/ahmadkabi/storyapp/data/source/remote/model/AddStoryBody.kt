package ahmadkabi.storyapp.data.source.remote.model

import okhttp3.MultipartBody
import okhttp3.RequestBody

data class AddStoryBody(
    val imageMultipart: MultipartBody.Part,
    val description: RequestBody
)
