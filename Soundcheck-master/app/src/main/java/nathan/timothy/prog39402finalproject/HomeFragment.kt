package nathan.timothy.prog39402finalproject

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.toList
import nathan.timothy.prog39402finalproject.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var musicDatabase: MusicDatabase

    private val artViewModel: ArtistViewModel by activityViewModels {
        ArtistViewModelFactory(
            (activity?.application as MusicApplication).database
                .artistDao()
        )
    }

    private val eventViewModel: EventViewModel by activityViewModels {
        EventViewModelFactory(
            (activity?.application as MusicApplication).database
                .eventDao()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        musicDatabase = MusicDatabase.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding: FragmentHomeBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

        artViewModel.retrieveAllArtists()

        artViewModel.allArtists.observe(this.viewLifecycleOwner) { items ->
            items.let {
                // Shuffle the list of artists
                val shuffledArtists = it.shuffled()

                // Set the shuffled list to the RecyclerView adapter
                binding.HomeArtistRecyclerView.adapter =
                    ArtistRecyclerView(shuffledArtists, findNavController())
            }
        }

        eventViewModel.retrieveAllEvents()

        eventViewModel.allEvents.observe(this.viewLifecycleOwner) { items ->
            items.let {
                // Shuffle list of events
                val shuffledEvents = it.shuffled()

                // Set shuffled list to recycler view adapter
                binding.HomeEventRecyclerView.adapter = EventRecyclerView(shuffledEvents, findNavController())
            }
        }

        // Set LayoutManager and other properties for HomeArtistRecyclerView
        binding.HomeArtistRecyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.HomeArtistRecyclerView.setHasFixedSize(true)
        binding.HomeArtistRecyclerView.isNestedScrollingEnabled = false

        // Set LayoutManager and other properties for HomeEventRecyclerView
        binding.HomeEventRecyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.HomeEventRecyclerView.setHasFixedSize(true)
        binding.HomeEventRecyclerView.isNestedScrollingEnabled = false

        return binding.root
    }
}
