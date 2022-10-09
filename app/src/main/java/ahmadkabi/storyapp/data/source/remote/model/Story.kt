package ahmadkabi.storyapp.data.source.remote.model

data class Story(
    val id: String,
    val name: String,
    val description: String,
    val photoUrl: String,
    val createdAt: Any,
    val lat: Float,
    val lon: Float
)