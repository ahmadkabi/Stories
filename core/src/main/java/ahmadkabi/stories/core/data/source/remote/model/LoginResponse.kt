package ahmadkabi.stories.core.data.source.remote.model

data class LoginResponse(
    val error: Boolean,
    val message: String,
    val loginResult: LoginResult
)
