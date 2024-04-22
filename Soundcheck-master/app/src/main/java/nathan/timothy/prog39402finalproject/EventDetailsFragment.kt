package nathan.timothy.prog39402finalproject

import android.app.AlertDialog
import android.content.Context
import android.location.Address
import android.location.Geocoder
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import nathan.timothy.prog39402finalproject.databinding.FragmentEventDetailsBinding

class EventDetailsFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentEventDetailsBinding? = null
    private val binding get() = _binding!!
    lateinit var event: EventEntity
    private lateinit var mMap: GoogleMap
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
        _binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_event_details, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val selectedEventId = arguments?.getLong("eventId")
        viewModel.retrieveEventById(selectedEventId!!).observe(this.viewLifecycleOwner)
        { selectedEvent ->
                event = selectedEvent
                bind(event)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun bind(selectedEvent: EventEntity) {
        binding.apply {
            event = selectedEvent
            if (eventArtists.text.toString().isBlank()) {
                for (act in selectedEvent.acts){
                    eventArtists.append("\u2022 " + act.trimStart() + "\n")
                }
            }
            reserveBtn.isEnabled = viewModel.areTicketsAvailable(selectedEvent)
            reserveBtn.setOnClickListener {
                viewModel.reserveTicket(selectedEvent)
                val pref = getActivity()?.getPreferences(Context.MODE_PRIVATE)
                val editor = pref?.edit()
                editor!!.putLong(selectedEvent.name, selectedEvent.id)
                editor!!.commit()
            }

            linkBtn.setOnClickListener{view : View ->
                val bundle = bundleOf("eventId" to selectedEvent.id)
                if (selectedEvent.link == "") {
                    toast("This event does not have a Eventbrite link")
                } else {
                    view.findNavController().navigate(R.id.action_eventDetailsFragment_to_eventLinkFragment, bundle)
                }
            }
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        //mMap.mapType = GoogleMap.MAP_TYPE_HYBRID

        val selectedEventId = arguments?.getLong("eventId")
        viewModel.retrieveEventById(selectedEventId!!).observe(this.viewLifecycleOwner)
        { selectedEvent ->
            val venueLatLng = getLatLngFromAddress(requireContext(), selectedEvent.address)
            val venueLat = venueLatLng.substringBefore(" ").toDouble()
            val venueLng = venueLatLng.substringAfter(" ").toDouble()

            // Add a marker in Toronto and move the camera
            val toronto = LatLng(43.6532, -79.3832)
            val venue = LatLng(venueLat, venueLng)
            mMap.addMarker(MarkerOptions().position(venue).title("Marker at venue"))
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(venue, 16.0f))
        }



    }

    fun getLatLngFromAddress(context: Context, mAddress: String): String {
        val coder = Geocoder(context)
        lateinit var address: List<Address>
        try {
            address = coder.getFromLocationName(mAddress, 5)!!
            if (address == null) {
                return "Failed to find Lat,Lng"
            }
            val location = address[0]
            return "${location.latitude} ${location.longitude}"
        } catch (e: Exception) {
            return "Failed to find Lat,Lng"
        }
    }

    fun toast(message:String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

}