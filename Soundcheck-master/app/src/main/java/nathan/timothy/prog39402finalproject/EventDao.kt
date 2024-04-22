package nathan.timothy.prog39402finalproject

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDao {

    @Query("SELECT * FROM events")
    fun getAll(): Flow<List<EventEntity>>

    @Query("SELECT * FROM events WHERE name LIKE :name || '%'")
    fun getEventsByName(name: String) : Flow<List<EventEntity>>

    @Query("SELECT * FROM events WHERE venue LIKE :venue")
    fun getEventsByVenue(venue: String) : Flow<List<EventEntity>>

    @Query("SELECT * FROM events WHERE date LIKE :date")
    fun getEventsByDate(date: String) : Flow<List<EventEntity>>

    @Query("SELECT * FROM events WHERE id =:id")
    fun getEventById(id: Long) : Flow<EventEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(artist: EventEntity)

    @Update
    suspend fun update(eventEntity: EventEntity)

    @Query("DELETE FROM events")
    suspend fun deleteAll()

    @Query("SELECT * FROM events")
    fun getAllExisting(): List<EventEntity>

    @Query("SELECT * FROM events WHERE id =:id")
    fun getEventByIdNotCoroutine(id: Long) : EventEntity

}