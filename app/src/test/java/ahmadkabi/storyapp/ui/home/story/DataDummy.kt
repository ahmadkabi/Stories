package ahmadkabi.storyapp.ui.home.story

import ahmadkabi.storyapp.data.source.remote.model.*

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

    fun generateLoginBody(): LoginBody{
        return LoginBody(
            "email",
            "password"
        )
    }
    fun generateLoginResponse(): LoginResponse{
        return LoginResponse(
            false,
            "success",
            LoginResult(
                "userId",
                "username",
                "token"
            )
        )
    }

    fun generateRegisterBody(): RegisterBody{
        return RegisterBody(
            "name",
            "email",
            "password"
        )
    }
    fun generateRegisterResponse(): RegisterResponse{
        return RegisterResponse(
            false,
            "success"
        )
    }

    fun generateAddStoryResponse(): AddStoryResponse{
        return AddStoryResponse(
            false,
            "success"
        )
    }
}