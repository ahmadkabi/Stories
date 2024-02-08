package ahmadkabi.stories.data.source.remote.model

data class GetStoriesResponse(
    val error: Boolean,
    val message: String,
    val listStory: ArrayList<ahmadkabi.stories.domain.model.Story>
)