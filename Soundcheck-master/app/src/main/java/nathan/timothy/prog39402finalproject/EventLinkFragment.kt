package nathan.timothy.prog39402finalproject

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import nathan.timothy.prog39402finalproject.databinding.FragmentEventLinkBinding

class EventLinkFragment : Fragment() {

    private lateinit var musicDatabase: MusicDatabase

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding: FragmentEventLinkBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_event_link, container, false
        )

        val selectedEventId = arguments?.getLong("eventId")
        viewModel.retrieveEventById(selectedEventId!!).observe(this.viewLifecycleOwner) { selectedEvent ->

            binding.eventWebView.settings.cacheMode =
                WebSettings.LOAD_DEFAULT // Use the default caching mode

            binding.eventWebView.settings.loadsImagesAutomatically = true

            binding.eventWebView.settings.javaScriptEnabled = true

            binding.eventWebView.settings.domStorageEnabled = true
            binding.eventWebView.settings.databaseEnabled = true

            binding.eventWebView.clearCache(true)

            binding.eventWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null)

            binding.eventWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null)

            // Set WebViewClient to handle URL loading within the WebView
            binding.eventWebView.webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                    // This method will be called when the user clicks on a link within the WebView
                    // Load the clicked URL in the WebView
                    view?.loadUrl(selectedEvent.link)
                    // Return true to indicate that the WebView should handle the URL loading
                    return true
                }
            }

            binding.eventWebView.loadUrl(selectedEvent.link)
        }

        return binding.root
    }
}
