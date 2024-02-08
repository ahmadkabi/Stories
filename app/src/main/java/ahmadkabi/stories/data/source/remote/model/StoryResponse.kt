package ahmadkabi.stories.data.source.remote.model

data class StoryResponse(
    val error: Boolean,
    val message: String,
    val listStory: List<ahmadkabi.stories.domain.model.Story>
)