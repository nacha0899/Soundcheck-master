package nathan.timothy.prog39402finalproject

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "events")
data class EventEntity(

    @PrimaryKey(autoGenerate = true)
    var id: Long,

    @ColumnInfo(name = "name")
    var name: String,

    @ColumnInfo(name = "venue")
    var venue: String,

    @ColumnInfo(name = "address")
    var address: String,

    @ColumnInfo(name = "date")
    var date: String,

    @ColumnInfo(name = "time")
    var time: String,

    @ColumnInfo(name = "tickets")
    var tickets: Int,

    @ColumnInfo(name = "acts")
    var acts: List<String>,

    @ColumnInfo(name = "image")
    val imageResource: Int,

    @ColumnInfo(name = "link")
    val link: String

)