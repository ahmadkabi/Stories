package ahmadkabi.storyapp.data.source.remote.model

data class StoryResponse(
    val error: Boolean,
    val message: String,
    val listStory: List<Story>
)