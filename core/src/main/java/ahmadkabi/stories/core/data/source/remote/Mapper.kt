package ahmadkabi.stories.core.data.source.remote

import ahmadkabi.stories.core.data.source.remote.model.LoginResult
import ahmadkabi.stories.domain.model.User

fun LoginResult.toUserModel() = User(
    name = name,
    token = token,
)