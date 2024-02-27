package ahmadkabi.stories.domain.usecase

import ahmadkabi.stories.domain.model.Story

interface StoryUseCase {

    fun getStories(): List<Story>

}