package nathan.timothy.prog39402finalproject

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "artists")
data class ArtistEntity(

    @PrimaryKey(autoGenerate = true)
    var id: Long,

    @ColumnInfo(name="image")
    val imageResource: Int,

    @ColumnInfo(name = "name")
    var name: String,

    @ColumnInfo(name = "genre")
    var genre: String,

    @ColumnInfo(name = "members")
    var members: List<String>,

    @ColumnInfo(name = "links")
    var links: List<String>

)