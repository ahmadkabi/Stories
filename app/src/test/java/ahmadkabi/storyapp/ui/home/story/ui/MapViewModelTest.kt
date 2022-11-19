package ahmadkabi.storyapp.ui.home.story.ui

import ahmadkabi.storyapp.data.StoryRepository
import ahmadkabi.storyapp.data.source.remote.ApiResponse
import ahmadkabi.storyapp.data.source.remote.StatusResponse
import ahmadkabi.storyapp.data.source.remote.model.Story
import ahmadkabi.storyapp.ui.home.story.CoroutinesTestRule
import ahmadkabi.storyapp.ui.home.story.DataDummy
import ahmadkabi.storyapp.ui.home.story.getOrAwaitValue
import ahmadkabi.storyapp.ui.map.MapViewModel
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class MapViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutinesTestRule = CoroutinesTestRule()

    private val dummyStories = DataDummy.generateDummyStory()

    @Mock
    private lateinit var storyRepository: StoryRepository
    private lateinit var mapViewModel: MapViewModel

    @Before
    fun setUp() {
        mapViewModel = MapViewModel(storyRepository)
    }

    @Test
    fun `when Get Stories Should Not Null and Return Success`() = runTest {
        val expected = ApiResponse(StatusResponse.SUCCESS, dummyStories)

        `when`(storyRepository.getMappedStories()).thenReturn(expected)

        mapViewModel.getMappedStories()
        val actual = mapViewModel.stories.getOrAwaitValue()

        Assert.assertNotNull(actual)
        Assert.assertEquals(expected.body?.size, actual.body?.size)
        Assert.assertEquals(expected.body?.get(1), actual.body?.get(1))

    }

    @Test
    fun `when Get Stories Should Return Error`() = runTest {
        val expected = ApiResponse<ArrayList<Story>>(StatusResponse.ERROR)

        `when`(storyRepository.getMappedStories()).thenReturn(expected)

        mapViewModel.getMappedStories()
        val actual = mapViewModel.stories.getOrAwaitValue()

        Assert.assertNotNull(actual)
        Assert.assertEquals(expected, actual)
    }

}