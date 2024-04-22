package nathan.timothy.prog39402finalproject

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ArtistViewModel (private val artistDao: ArtistDao) : ViewModel() {

    var allArtists: LiveData<List<ArtistEntity>> = artistDao.getAll().asLiveData()

    // LiveData for the image resource
    private val _imageResource = MutableLiveData<Int>()
    val imageResource: LiveData<Int> get() = _imageResource

    fun setImageResource(resource: Int) {
        _imageResource.value = resource
    }

    fun retrieveArtistsByGenre(genre: String){
        viewModelScope.launch(Dispatchers.IO) {
            allArtists = artistDao.getArtistByGenre(genre).asLiveData()
        }
    }

    fun retrieveAllArtists() {
        viewModelScope.launch(Dispatchers.IO) {
            allArtists = artistDao.getAll().asLiveData()
        }
    }

    fun insertArtist(artist: ArtistEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            artistDao.insert(artist)
        }
    }

    fun retrieveArtistsByName(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            allArtists = artistDao.getArtistsByName(name).asLiveData()
        }
    }


    fun retrieveArtistsById(id: Long): LiveData<ArtistEntity> {
            return artistDao.getArtistsById(id).asLiveData()
    }


    fun deleteAllArtists() {
        viewModelScope.launch(Dispatchers.IO) {
            artistDao.deleteAll()
        }
    }

}

class ArtistViewModelFactory(private val artistDao: ArtistDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ArtistViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ArtistViewModel(artistDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}