package ahmadkabi.storyapp.ui.home.story

import ahmadkabi.storyapp.data.source.remote.model.Story

object DataDummy {

    fun generateDummyStory(): List<Story> {

        val newsList = ArrayList<Story>()

        for (i in 0..10) {
            val news = Story(
                "le $i",
                "2022-02-22T22:22:22Z",
                "https://dicoding-web-img.sgp1.cdn.digitaloceanspaces.com/original/commons/feature-1-kurikulum-global-3.png",
                "https://www.dicoding.czom/",
                "https://www.dicoding.czom/",
                0f,
                0f
            )
            newsList.add(news)
        }

        return newsList

    }
}