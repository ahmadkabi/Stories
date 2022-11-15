package ahmadkabi.storyapp.data.source.remote.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "story")
data class Story(

    @PrimaryKey
    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("description")
    val description: String,

    @field:SerializedName("photoUrl")
    val photoUrl: String,

    @field:SerializedName("createdAt")
    val createdAt: Any,

    @field:SerializedName("lat")
    val lat: Float?,

    @field:SerializedName("lon")
    val lon: Float?

)