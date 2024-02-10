package ahmadkabi.stories.ui.home.story

import ahmadkabi.stories.data.model.*
import ahmadkabi.stories.domain.model.Story

object DataDummy {

    fun generateDummyStory(): ArrayList<Story> {

        val newsList = ArrayList<Story>()

        for (i in 0..10) {
            val news = Story(
                "$i",
                "Username",
                "Story Description",
                "https://www.dicoding.com/",
                "2022-02-22T22:22:22Z",
                0f,
                0f
            )
            newsList.add(news)
        }

        return newsList

    }

    fun generateLoginBody(): model.LoginBody {
        return model.LoginBody(
            "email",
            "password"
        )
    }
    fun generateLoginResponse(): LoginResponse {
        return LoginResponse(
            false,
            "success",
            model.LoginResult(
                "userId",
                "username",
                "token"
            )
        )
    }

    fun generateRegisterBody(): model.RegisterBody {
        return model.RegisterBody(
            "name",
            "email",
            "password"
        )
    }
    fun generateRegisterResponse(): RegisterResponse {
        return RegisterResponse(
            false,
            "success"
        )
    }

    fun generateAddStoryResponse(): model.AddStoryResponse {
        return model.AddStoryResponse(
            false,
            "success"
        )
    }
}