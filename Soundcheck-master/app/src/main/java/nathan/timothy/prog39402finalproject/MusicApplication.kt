package nathan.timothy.prog39402finalproject

import android.app.Application

class MusicApplication : Application(){

    val database: MusicDatabase by lazy { MusicDatabase.getInstance(this) }

}