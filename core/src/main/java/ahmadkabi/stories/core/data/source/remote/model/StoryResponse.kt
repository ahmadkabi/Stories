package ahmadkabi.stories.core.data.source.remote.model

import ahmadkabi.stories.domain.model.Story

data class StoryResponse(
    val error: Boolean,
    val message: String,
    val listStory: List<Story>
)