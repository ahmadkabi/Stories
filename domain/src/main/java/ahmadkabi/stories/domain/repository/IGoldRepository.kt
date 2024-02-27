package ahmadkabi.stories.domain.repository

import ahmadkabi.stories.domain.model.Story

interface IStoryRepository {

    fun getStories(): List<Story>

}