package ahmadkabi.stories.ui.home.story.ui

import ahmadkabi.stories.data.StoryRepository
import ahmadkabi.stories.data.source.remote.ApiResponse
import ahmadkabi.stories.data.source.remote.StatusResponse
import ahmadkabi.stories.data.source.remote.model.Story
import ahmadkabi.stories.ui.add.AddStoryViewModel
import ahmadkabi.stories.ui.home.story.CoroutinesTestRule
import ahmadkabi.stories.ui.home.story.DataDummy
import ahmadkabi.stories.ui.home.story.getOrAwaitValue
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import java.io.File

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class AddStoryViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutinesTestRule = CoroutinesTestRule()

    private val file = File("path")
    private val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
        "image",
        file.name,
        file.asRequestBody("image/jpeg".toMediaTypeOrNull())
    )

    private val description = "description".toRequestBody("text/plain".toMediaType())

    private val addStoryResponse = DataDummy.generateAddStoryResponse()

    @Mock
    private lateinit var storyRepository: StoryRepository
    private lateinit var addStoryViewModel: AddStoryViewModel

    @Before
    fun setUp() {
        addStoryViewModel = AddStoryViewModel(storyRepository)
    }

    @Test
    fun `when Add Story Should Return Error == false`() = runTest {
        val expected = ApiResponse(StatusResponse.SUCCESS, addStoryResponse)

        Mockito.`when`(storyRepository.addStory(imageMultipart, description)).thenReturn(expected)

        addStoryViewModel.addStory(imageMultipart, description)
        val actual = addStoryViewModel.addStory.getOrAwaitValue()

        Assert.assertNotNull(actual)
        Assert.assertEquals(false, actual.body?.error)

    }

    @Test
    fun `when Add Story should return status error`() = runTest {
        val expected = ApiResponse<ArrayList<Story>>(StatusResponse.ERROR)

        Mockito.`when`(storyRepository.addStory(imageMultipart, description)).thenThrow()

        addStoryViewModel.addStory(imageMultipart, description)
        val actual = addStoryViewModel.addStory.getOrAwaitValue()

        Assert.assertEquals(expected.status, actual.status)
    }

}