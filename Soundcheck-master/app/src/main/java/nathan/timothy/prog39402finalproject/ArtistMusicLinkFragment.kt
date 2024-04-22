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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.toList
import nathan.timothy.prog39402finalproject.databinding.FragmentArtistMusicLinkBinding

class ArtistMusicLinkFragment : Fragment() {

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
        val binding: FragmentArtistMusicLinkBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_artist_music_link, container, false)

        // Selecting music link depending on the band selected
        val selectedArtistId = arguments?.getLong("artistId")
        viewModel.retrieveArtistsById(selectedArtistId!!).observe(this.viewLifecycleOwner) { selectedArtist ->

            binding.artistMusicWebView.settings.cacheMode = WebSettings.LOAD_DEFAULT // Use the default caching mode

            binding.artistMusicWebView.settings.loadsImagesAutomatically = true

            binding.artistMusicWebView.settings.javaScriptEnabled = true

            binding.artistMusicWebView.settings.domStorageEnabled = true
            binding.artistMusicWebView.settings.databaseEnabled = true

            binding.artistMusicWebView.clearCache(true)

            binding.artistMusicWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null)

            binding.artistMusicWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null)

            binding.artistMusicWebView.settings.userAgentString =
                "Mozilla/5.0 (Linux; Android 10; Pixel 4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Mobile Safari/537.36 Instagram/250.0.0.11.157 Android (30/11; 480dpi; 1080x2030; Google/Google; Pixel 4; coral; coral; en_US)"

            // Set WebViewClient to handle URL loading within the WebView
            binding.artistMusicWebView.webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                    // This method will be called when the user clicks on a link within the WebView
                    // Load the clicked URL in the WebView
                    view?.loadUrl(selectedArtist.links[1])
                    // Return true to indicate that the WebView should handle the URL loading
                    return true
                }
            }

            binding.artistMusicWebView.loadUrl(selectedArtist.links[1].toString())
        }

        return binding.root
    }

    suspend fun <T> Flow<List<T>>.flattenToList() =
        flatMapConcat { it.asFlow() }.toList()
}
