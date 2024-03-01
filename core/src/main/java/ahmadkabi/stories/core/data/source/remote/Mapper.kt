package ahmadkabi.stories.core.data.source.remote

import ahmadkabi.stories.core.data.source.remote.model.LoginResult
import ahmadkabi.stories.core.data.source.remote.model.RegisterResponse
import ahmadkabi.stories.domain.model.Register
import ahmadkabi.stories.domain.model.User

fun LoginResult.toUserModel() = User(
    name = name,
    token = token,
)

fun RegisterResponse.toRegister() = Register(
    isError = error
)