package nathan.timothy.prog39402finalproject

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.toList
import nathan.timothy.prog39402finalproject.databinding.FragmentEventBinding
import nathan.timothy.prog39402finalproject.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private lateinit var musicDatabase:MusicDatabase

    private val viewModel: EventViewModel by activityViewModels {
        EventViewModelFactory(
            (activity?.application as MusicApplication).database
                .eventDao()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        musicDatabase = MusicDatabase.getInstance(requireContext())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val binding: FragmentProfileBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_profile, container, false)

        binding.eventBtn.setOnClickListener { view : View ->
            view.findNavController().navigate(R.id.action_profileFragment_to_addEventFragment)
        }

        binding.artistBtn.setOnClickListener{view : View ->
            view.findNavController().navigate(R.id.action_profileFragment_to_addArtistFragment)
        }

        val allEvents = musicDatabase.eventDao().getAllExisting()
        var bookedEvents: ArrayList<EventEntity> = arrayListOf()
        val pref = getActivity()?.getPreferences(Context.MODE_PRIVATE)
        for (event in allEvents) {
            if (pref!!.contains(event.name)) {
                val eventId = pref!!.getLong(event.name, -1L)
                bookedEvents.add(musicDatabase.eventDao().getEventByIdNotCoroutine(eventId))

                // Retrieving events from the view model by coroutine causes the recyclerview
                // to not populate on time
                /*viewModel.retrieveEventById(eventId).observe(this.viewLifecycleOwner)
                { selectedEvent ->
                    bookedEvents.add(selectedEvent)
                }*/
            }
        }
        //Thread.sleep(4000L)
        binding.recyclerView.adapter = EventProfileRecyclerView(bookedEvents)
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.setHasFixedSize(true)

        return binding.root
    }

    fun toast(message:String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    suspend fun <T> Flow<List<T>>.flattenToList() =
        flatMapConcat { it.asFlow() }.toList()

}