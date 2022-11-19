package ahmadkabi.storyapp.ui.home.story.ui

import ahmadkabi.storyapp.data.StoryRepository
import ahmadkabi.storyapp.data.source.remote.ApiResponse
import ahmadkabi.storyapp.data.source.remote.StatusResponse
import ahmadkabi.storyapp.data.source.remote.model.LoginResponse
import ahmadkabi.storyapp.ui.home.story.CoroutinesTestRule
import ahmadkabi.storyapp.ui.home.story.DataDummy
import ahmadkabi.storyapp.ui.home.story.getOrAwaitValue
import ahmadkabi.storyapp.ui.login.LoginViewModel
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
class LoginViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutinesTestRule = CoroutinesTestRule()

    private val loginResponse = DataDummy.generateLoginResponse()
    private val loginBody = DataDummy.generateLoginBody()

    @Mock
    private lateinit var storyRepository: StoryRepository
    private lateinit var loginViewModel: LoginViewModel

    @Before
    fun setUp() {
        loginViewModel = LoginViewModel(storyRepository)
    }

    @Test
    fun `when Login with correct credential should return Not Null and Success`() = runTest {
        val expected = ApiResponse(StatusResponse.SUCCESS, loginResponse)

        Mockito.`when`(storyRepository.login(loginBody)).thenReturn(expected)

        loginViewModel.login(loginBody)
        val actual = loginViewModel.login.getOrAwaitValue()

        Assert.assertNotNull(actual)
        Assert.assertEquals(expected.body?.loginResult?.token, actual.body?.loginResult?.token)
    }

    @Test
    fun `when Login with wrong credential should Fail`() = runTest {
        val expected = ApiResponse<LoginResponse>(StatusResponse.ERROR)

        Mockito.`when`(storyRepository.login(loginBody)).thenThrow()

        loginViewModel.login(loginBody)
        val actual = loginViewModel.login.getOrAwaitValue()

        Assert.assertEquals(expected.status, actual.status)
    }

}