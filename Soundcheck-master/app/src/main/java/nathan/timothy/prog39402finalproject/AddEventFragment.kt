package nathan.timothy.prog39402finalproject

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import nathan.timothy.prog39402finalproject.databinding.FragmentAddEventBinding

class AddEventFragment : Fragment() {

    private lateinit var musicDatabase:MusicDatabase
    private lateinit var selectedArtists: String

    private val artistViewModel: ArtistViewModel by activityViewModels {
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val binding: FragmentAddEventBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_add_event, container, false)

        /*artistViewModel.retrieveAllArtists()
        Thread.sleep(3000L)*/

        // Setting up multi-select spinner with all artists from database
        var artistList: List<ArtistEntity> = musicDatabase.artistDao().getAllExisting()
        var artists: ArrayList<String> = arrayListOf()
        /*artistViewModel.allArtists.observe(this.viewLifecycleOwner) { items ->
            items.let {
                artistList = it
            }
        }
        Thread.sleep(3000L)*/
        for (artist in artistList) {
            artists.add(artist.name)
        }

        var mySpinner: MultiSelectionSpinner = binding.artistsSpinner
        mySpinner.setItems(artists)

        //Insert button click listener
        val insert = binding.addEventBtn.setOnClickListener{ view : View ->

            var name = binding.nameEditText.text.toString()
            var venue = binding.venueEditText.text.toString()
            var address = binding.addressEditText.text.toString()
            var date = binding.dateEditText.text.toString()
            var time = binding.timeEditText.text.toString()
            var tickets = binding.ticketsEditText.text.toString()
            //var artists = selectedArtists
            var artists = mySpinner.selectedItemsAsString

            if (name.equals("") || venue.equals("") || address.equals("") || date.equals("") ||
                time.equals("") || tickets.equals("") || artists.equals("")) {
                val alertDialog = AlertDialog.Builder(requireContext()).create()
                alertDialog.setTitle("Alert")
                alertDialog.setMessage("No fields except for event link can be blank")
                alertDialog.setButton(
                    AlertDialog.BUTTON_NEUTRAL, "OK"
                ) { dialog, which -> dialog.dismiss() }
                alertDialog.show()
            } else {
                // Initialize a new event with data
                val eventName = binding.nameEditText.text.toString()
                val eventVenue = binding.venueEditText.text.toString()
                val eventAddress = binding.addressEditText.text.toString()
                val eventDate = binding.dateEditText.text.toString()
                val eventTime = binding.timeEditText.text.toString()
                val eventTickets = binding.ticketsEditText.text.toString().toInt()
                val eventArtists = mySpinner.selectedStrings
                val eventLink = binding.linkEditText.text.toString()

                val event = EventEntity(0,
                    name = eventName,
                    venue = eventVenue,
                    address = eventAddress,
                    date = eventDate,
                    time = eventTime,
                    tickets = eventTickets,
                    acts = eventArtists,
                    imageResource = R.drawable.eventicon,
                    link = eventLink
                )

                mySpinner.setSelection(0)
                binding.nameEditText.setText("")
                binding.venueEditText.setText("")
                binding.addressEditText.setText("")
                binding.dateEditText.setText("")
                binding.timeEditText.setText("")
                binding.ticketsEditText.setText("")
                binding.linkEditText.setText("")

                eventViewModel.insertEvent(event)
                toast("New event was added successfully")
            }
        }

        return binding.root
    }

    fun toast(message:String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

}