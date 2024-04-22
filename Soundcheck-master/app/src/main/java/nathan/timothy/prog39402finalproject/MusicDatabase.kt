package nathan.timothy.prog39402finalproject

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@TypeConverters(DatabaseTypeConverter::class)
@Database(entities = [ArtistEntity::class, EventEntity::class], version = 2) // Incremented version to 2
abstract class MusicDatabase : RoomDatabase() {

    abstract fun artistDao(): ArtistDao
    abstract fun eventDao(): EventDao

    companion object {
        private var INSTANCE: MusicDatabase? = null

        fun getInstance(context: Context): MusicDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    MusicDatabase::class.java,
                    "MusicDB.db"
                )
                    .fallbackToDestructiveMigration() // This will drop the existing tables if migrations fail
                    .allowMainThreadQueries()
                    .build()
            }
            return INSTANCE as MusicDatabase
        }
    }
}
