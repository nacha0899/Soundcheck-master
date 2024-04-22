package nathan.timothy.prog39402finalproject

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.toList
import nathan.timothy.prog39402finalproject.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var musicDatabase:MusicDatabase

    private val artistViewModel: ArtistViewModel by viewModels {
       ArtistViewModelFactory(
            (application as MusicApplication).database
                .artistDao()
        )
    }
    private val eventViewModel: EventViewModel by viewModels {
        EventViewModelFactory(
            (application as MusicApplication).database
                .eventDao()
        )
    }

    val images = listOf(R.drawable.morissonheights,R.drawable.theband, R.drawable.fulldeck, R.drawable.jumpmoon)
    val artists = listOf("Morrison Heights", "The Band", "Full Deck", "Jump Moon")
    val genres = listOf("Rock", "Pop", "Punk", "Rock")
    val members = listOf(listOf("Matthew", "Daniel", "Mel"), listOf("Ron", "Lev", "Rob", "Scott"), listOf("Jesse", "Aram", "Daniel","Karan"), listOf("Mourad, Erick, Nick, Gabriel"))
    val links = listOf(listOf("https://www.instagram.com/morissonheights/", "https://www.youtube.com/watch?v=gO-tUOcZzAw",""), listOf("https://www.instagram.com/the_band_official_/?hl=en", "https://www.youtube.com/watch?v=JsdUzN20Sow", ""), listOf("https://www.instagram.com/fulldeckband/", "https://open.spotify.com/artist/2JAFikbnHURfxQFISQrG4Q?dl_branch=1&si=mbKog-L_S9GWVdoMdLgp3w&utm_medium=share&utm_source=linktree&nd=1&dlsi=b7d39d808d61454d","https://fulldeckofficialba.wixsite.com/fulldeck"), listOf("https://www.instagram.com/officialjumpmoon/?img_index=1","https://open.spotify.com/artist/5GZsTLBY3QsgrLyslimJvv","https://www.youtube.com/watch?time_continue=21&v=H-HTZJ3WocA"))

    val events = listOf("Shindig", "Big Summer Blowout")
    val venues = listOf("Horseshoe Tavern", "Cameron House")
    val addresses = listOf("370 Queen Street W", "408 Queen Street W")
    val dates = listOf("November 30, 2023", "December 25, 2023")
    val times = listOf("8:00 pm", "9:00 pm")
    val tickets = listOf(200, 500)
    val acts = listOf(listOf("Morrison Heights", "The Band"), listOf("Full Deck"))
    val eventImages = listOf(R.drawable.shingid, R.drawable.bigsummerblowout)
    val eventLinks = listOf("https://www.eventbrite.ca/e/20-year-merivale-high-school-reunion-tickets-720697494607?aff=ebdssbdestsearch", "https://www.eventbrite.ca/e/b222-fall-2023-launch-event-registration-768482159827?aff=ebdssbdestsearch&keep_tld=1")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)

        val navController = findNavController(R.id.myNavHostFragment)
        binding.navbar.setupWithNavController(navController)

        // Initialize the room database with 3 dummy records if it is empty
        artistViewModel.deleteAllArtists()
        eventViewModel.deleteAllEvents()
        Thread.sleep(3000L)
        musicDatabase = MusicDatabase.getInstance(applicationContext)
        for (i in 0..3) {
            val artist = ArtistEntity(i.toLong() + 1L,
                imageResource = images[i],
                name = artists[i],
                genre = genres[i],
                members = members[i],
                links = links[i]
            )
            artistViewModel.insertArtist(artist)
        }
        for (i in 0..1) {
            val event = EventEntity(i.toLong() + 1L,
                name = events[i],
                venue = venues[i],
                address = addresses[i],
                date = dates[i],
                time = times[i],
                tickets = tickets[i],
                acts = acts[i],
                imageResource = eventImages[i],
                link = eventLinks[i]
            )
            eventViewModel.insertEvent(event)
        }

        clearPrefs()
    }

    override fun onBackPressed() {
        val navController = findNavController(R.id.myNavHostFragment)
        val navHostFragment: Fragment? = supportFragmentManager.findFragmentById(R.id.myNavHostFragment)
        if (navHostFragment?.childFragmentManager?.fragments?.get(0) is EventDetailsFragment) {
            if (navHostFragment?.childFragmentManager?.fragments?.get(1) is EventFragment) {
                navController.navigate(R.id.action_eventDetailsFragment_to_eventFragment)
            } else if (navHostFragment?.childFragmentManager?.fragments?.get(1) is ProfileFragment) {
                navController.navigate(R.id.action_eventDetailsFragment_to_profileFragment)
            }
        } else if (navHostFragment?.childFragmentManager?.fragments?.get(0) is AddArtistFragment) {
            navController.navigate(R.id.action_addArtistFragment_to_profileFragment)
        } else if (navHostFragment?.childFragmentManager?.fragments?.get(0) is AddEventFragment) {
            navController.navigate(R.id.action_addEventFragment_to_profileFragment)
        } else if (navHostFragment?.childFragmentManager?.fragments?.get(0) is EventLinkFragment) {
            val selectedEventId: Long = (navHostFragment?.childFragmentManager?.fragments?.get(0)
                    as EventLinkFragment).arguments?.getLong("eventId")!!
            val bundle = bundleOf("eventId" to selectedEventId)
            navController.navigate(R.id.action_eventLinkFragment_to_eventDetailsFragment, bundle)
        }
    }

        // Clears the SharedPreferences when app history needs to be reset
    fun clearPrefs() {
        val pref = getPreferences(Context.MODE_PRIVATE)
        val editor = pref.edit()
        editor.clear()
        editor.commit()
    }

    suspend fun <T> Flow<List<T>>.flattenToList() =
        flatMapConcat { it.asFlow() }.toList()
}