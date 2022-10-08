package ahmadkabi.storyapp.data.source.remote.model
data class GetStoriesResponse(
    val error: Boolean,
    val message: String,
    val listStory: ArrayList<Story>
)