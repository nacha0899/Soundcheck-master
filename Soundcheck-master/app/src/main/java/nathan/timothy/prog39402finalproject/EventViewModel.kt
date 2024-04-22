package nathan.timothy.prog39402finalproject

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EventViewModel (private val eventDao: EventDao) : ViewModel() {

    var allEvents: LiveData<List<EventEntity>> = eventDao.getAll().asLiveData()

    fun retrieveAllEvents() {
        viewModelScope.launch(Dispatchers.IO) {
            allEvents = eventDao.getAll().asLiveData()
        }
    }

    fun insertEvent(Event: EventEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            eventDao.insert(Event)
        }
    }

    fun retrieveEventsByName(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            allEvents = eventDao.getEventsByName(name).asLiveData()
        }
    }

    fun retrieveEventsByVenue(venue: String) {
        viewModelScope.launch(Dispatchers.IO) {
            allEvents = eventDao.getEventsByVenue(venue).asLiveData()
        }
    }

    fun retrieveEventsByDate(date: String) {
        viewModelScope.launch(Dispatchers.IO) {
            allEvents = eventDao.getEventsByDate(date).asLiveData()
        }
    }

    fun retrieveEventById(id: Long): LiveData<EventEntity> {
        return eventDao.getEventById(id).asLiveData()
    }

    fun reserveTicket(event: EventEntity) {
        viewModelScope.launch {
            if (event.tickets > 0) {
                // Decrease the number of tickets by 1
                val newEvent = event.copy(tickets = event.tickets - 1)
                eventDao.update(newEvent)
            }
        }
    }

    fun areTicketsAvailable(event: EventEntity): Boolean {
        return (event.tickets > 0)
    }

    fun deleteAllEvents() {
        viewModelScope.launch(Dispatchers.IO) {
            eventDao.deleteAll()
        }
    }

}

class EventViewModelFactory(private val eventDao: EventDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EventViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return EventViewModel(eventDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}