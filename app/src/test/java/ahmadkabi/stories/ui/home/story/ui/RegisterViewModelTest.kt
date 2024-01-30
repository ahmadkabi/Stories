package ahmadkabi.stories.ui.home.story.ui

import ahmadkabi.stories.data.StoryRepository
import ahmadkabi.stories.data.source.remote.ApiResponse
import ahmadkabi.stories.data.source.remote.StatusResponse
import ahmadkabi.stories.data.source.remote.model.LoginResponse
import ahmadkabi.stories.ui.home.story.CoroutinesTestRule
import ahmadkabi.stories.ui.home.story.DataDummy
import ahmadkabi.stories.ui.home.story.getOrAwaitValue
import ahmadkabi.stories.ui.register.RegisterViewModel
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class RegisterViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutinesTestRule = CoroutinesTestRule()

    private val registerResponse = DataDummy.generateRegisterResponse()
    private val registerBody = DataDummy.generateRegisterBody()

    @Mock
    private lateinit var storyRepository: StoryRepository
    private lateinit var registerViewModel: RegisterViewModel

    @Before
    fun setUp() {
        registerViewModel = RegisterViewModel(storyRepository)
    }

    @Test
    fun `when Register with correct credential should return error == false`() = runTest {
        val expected = ApiResponse(StatusResponse.SUCCESS, registerResponse)

        Mockito.`when`(storyRepository.register(registerBody)).thenReturn(expected)

        registerViewModel.register(registerBody)
        val actual = registerViewModel.register.getOrAwaitValue()

        Assert.assertNotNull(actual)
        Assert.assertEquals(false, actual.body?.error)
    }

    @Test
    fun `when Register with wrong credential should Fail`() = runTest {
        val expected = ApiResponse<LoginResponse>(StatusResponse.ERROR)

        Mockito.`when`(storyRepository.register(registerBody)).thenThrow()

        registerViewModel.register(registerBody)
        val actual = registerViewModel.register.getOrAwaitValue()

        Assert.assertEquals(expected.status, actual.status)
    }

}