package nathan.timothy.prog39402finalproject

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.toList
import nathan.timothy.prog39402finalproject.databinding.FragmentArtistExtraLinkBinding


class ArtistExtraLinkFragment : Fragment() {

    private lateinit var musicDatabase: MusicDatabase
    private val viewModel: ArtistViewModel by activityViewModels {
        ArtistViewModelFactory(
            (activity?.application as MusicApplication).database
                .artistDao()
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
val binding: FragmentArtistExtraLinkBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_artist_extra_link, container, false)

        // Selecting music link depending on the band selected
        val selectedArtistId = arguments?.getLong("artistId")
        viewModel.retrieveArtistsById(selectedArtistId!!).observe(this.viewLifecycleOwner){ selectedArtist->

            binding.artistExtraWebView.settings.cacheMode = WebSettings.LOAD_DEFAULT // Use the default caching mode

            binding.artistExtraWebView.settings.loadsImagesAutomatically = true

            binding.artistExtraWebView.settings.javaScriptEnabled = true

            binding.artistExtraWebView.settings.domStorageEnabled = true
            binding.artistExtraWebView.settings.databaseEnabled = true

            binding.artistExtraWebView.clearCache(true)

            binding.artistExtraWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null)

            binding.artistExtraWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null)

            binding.artistExtraWebView.settings.userAgentString = "Mozilla/5.0 (Linux; Android 10; Pixel 4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Mobile Safari/537.36 " +
                    "Instagram/250.0.0.11.157 (X11; Linux x86_64; en_US) AppleWebKit/527.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36 " +
                    "Spotify/8.6.36.1076 (Android; Linux; U; Android 10; en-US) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30 " +
                    "AppleMusic/3.6.0 (Android 10; en_US) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Mobile Safari/537.36 " +
                    "Facebook/350.0.0.0.0 (Android 10; en_US) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Mobile Safari/537.36 " +
                    "Twitter/10.10.2-release.00 (Android 10; en-US) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Mobile Safari/537.36 " +
                    "YouTube/17.47.36 (Android 10; en_US) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Mobile Safari/537.36"


            // Set WebViewClient to handle URL loading within the WebView
            binding.artistExtraWebView.webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                    // This method will be called when the user clicks on a link within the WebView
                    // Load the clicked URL in the WebView
                    view?.loadUrl(selectedArtist.links[2])
                    // Return true to indicate that the WebView should handle the URL loading
                    return true
                }
            }

            binding.artistExtraWebView.loadUrl(selectedArtist.links[2].toString())

        }



        return binding.root
    }

    suspend fun <T> Flow<List<T>>.flattenToList() =
        flatMapConcat { it.asFlow() }.toList()

}