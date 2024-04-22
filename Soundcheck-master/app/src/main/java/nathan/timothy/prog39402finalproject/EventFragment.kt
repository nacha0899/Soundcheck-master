package nathan.timothy.prog39402finalproject

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.toList
import nathan.timothy.prog39402finalproject.databinding.FragmentEventBinding
import nathan.timothy.prog39402finalproject.databinding.FragmentEventDetailsBinding

class EventFragment : Fragment() {

    private lateinit var musicDatabase:MusicDatabase
    private var _binding: FragmentEventBinding? = null
    private val binding get() = _binding!!

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
        _binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_event, container, false)

        var eventList: List<EventEntity> = musicDatabase.eventDao().getAllExisting()
        var venues: ArrayList<String> = arrayListOf()
        var dates: ArrayList<String> = arrayListOf()

        // Setting up venues spinner for searching events by venue
        venues.add("Select a venue")
        for (event in eventList) {
            if (!venues.contains(event.venue)){
                venues.add(event.venue)
            }
        }
        val arrayVenueAdapter = activity?.let {
            ArrayAdapter<String>(
                it,
                android.R.layout.simple_spinner_item,
                venues
            )
        }

        val myVenueSpinner = binding.venueSpinner
        myVenueSpinner.adapter = arrayVenueAdapter
        myVenueSpinner.onItemSelectedListener = object:
            AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long)
            {
                if (position != 0) {
                    viewModel.retrieveEventsByVenue(venues[position])
                    Thread.sleep(1000L)
                    viewModel.allEvents.observe(viewLifecycleOwner) { items ->
                        items.let {
                            binding.recyclerView.adapter = EventRecyclerView(it, findNavController())
                            toast("${it.size} records found.")
                        }
                    }
                    binding.recyclerView.layoutManager = LinearLayoutManager(context)
                    binding.recyclerView.setHasFixedSize(true)
                }
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

        // Setting up dates spinner for searching events by dates
        dates.add("Select a date")
        for (event in eventList) {
            if (!dates.contains(event.date)){
                dates.add(event.date)
            }
        }
        val arrayDateAdapter = activity?.let {
            ArrayAdapter<String>(
                it,
                android.R.layout.simple_spinner_item,
                dates
            )
        }
        val myDateSpinner = binding.dateSpinner
        myDateSpinner.adapter = arrayDateAdapter
        myDateSpinner.onItemSelectedListener = object:
            AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long)
            {
                if (position != 0) {
                    viewModel.retrieveEventsByDate(dates[position])
                    Thread.sleep(1000L)
                    viewModel.allEvents.observe(viewLifecycleOwner) { items ->
                        items.let {
                            binding.recyclerView.adapter = EventRecyclerView(it, findNavController())
                            toast("${it.size} records found.")
                        }
                    }
                    binding.recyclerView.layoutManager = LinearLayoutManager(context)
                    binding.recyclerView.setHasFixedSize(true)
                }
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

        binding.searchEditText.setText("")

        // Get the events list from database
        viewModel.retrieveAllEvents()
        Thread.sleep(3000L)
        viewModel.allEvents.observe(this.viewLifecycleOwner) { items ->
            items.let {
                binding.recyclerView.adapter = EventRecyclerView(it, findNavController())
                toast("${it.size} records found.")
            }
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.setHasFixedSize(true)

        // Setting up searching events by name
        binding.searchButton.setOnClickListener() {

            val searchTerm = binding.searchEditText.text.toString()

            // Get the teams list from database
            if (!searchTerm.equals("")) {
                viewModel.retrieveEventsByName(searchTerm)
            } else {
                viewModel.retrieveAllEvents()
            }

            Thread.sleep(1000L)
            viewModel.allEvents.observe(this.viewLifecycleOwner) { items ->
                items.let {
                    binding.recyclerView.adapter = EventRecyclerView(it, findNavController())
                    toast("${it.size} records found.")
                }
            }
            binding.recyclerView.layoutManager = LinearLayoutManager(context)
            binding.recyclerView.setHasFixedSize(true)
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        binding.venueSpinner.setSelection(0)
        binding.dateSpinner.setSelection(0)
        binding.searchEditText.setText("")
    }

    fun toast(message:String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    suspend fun <T> Flow<List<T>>.flattenToList() =
        flatMapConcat { it.asFlow() }.toList()

}