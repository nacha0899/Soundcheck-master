package nathan.timothy.prog39402finalproject

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ArtistDao {

    @Query("SELECT * FROM artists")
    fun getAll(): Flow<List<ArtistEntity>>

    @Query("SELECT * FROM artists WHERE name LIKE :name || '%'")
    fun getArtistsByName(name: String) : Flow<List<ArtistEntity>>

    @Query("SELECT * FROM artists WHERE genre LIKE :genre || '%'")
    fun getArtistByGenre(genre: String) : Flow<List<ArtistEntity>>

    @Query("SELECT * FROM artists WHERE id =:id")
    fun getArtistsById(id:Long) : Flow<ArtistEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(artist: ArtistEntity)

    @Query("DELETE FROM artists")
    suspend fun deleteAll()

    @Query("SELECT * FROM artists")
    fun getAllExisting(): List<ArtistEntity>

}