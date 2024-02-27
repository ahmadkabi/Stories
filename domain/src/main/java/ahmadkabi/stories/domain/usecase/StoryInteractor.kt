package ahmadkabi.stories.domain.usecase

import ahmadkabi.stories.domain.model.Story
import ahmadkabi.stories.domain.repository.IStoryRepository

class StoryInteractor(private val goldRepository: IStoryRepository) : StoryUseCase {
    override fun getStories(): List<Story> = goldRepository.getStories()


}