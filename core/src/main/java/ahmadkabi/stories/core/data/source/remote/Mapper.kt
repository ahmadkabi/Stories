package ahmadkabi.stories.core.data.source.remote

import ahmadkabi.stories.core.data.source.remote.model.LoginResult
import ahmadkabi.stories.domain.model.UserModel

fun LoginResult.toUserModel() = UserModel(
    name = name,
    token = token,
)