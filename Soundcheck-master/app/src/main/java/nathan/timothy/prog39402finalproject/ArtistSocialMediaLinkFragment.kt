package nathan.timothy.prog39402finalproject

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.toList
import nathan.timothy.prog39402finalproject.databinding.FragmentArtistSocialMediaLinkBinding


class ArtistSocialMediaLinkFragment : Fragment() {

    private lateinit var musicDatabase:MusicDatabase
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
        val binding: FragmentArtistSocialMediaLinkBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_artist_social_media_link,container,false)

        val selectedArtistId = arguments?.getLong("artistId")
        viewModel.retrieveArtistsById(selectedArtistId!!).observe(this.viewLifecycleOwner){
            selectedArtist ->

            val webView: WebView = binding.artistSocialWebView

            // Get WebView settings
            val webSettings: WebSettings = webView.settings

            // Adjust settings for optimal memory usage
            webSettings.cacheMode = WebSettings.LOAD_DEFAULT // Use the default caching mode

            // Enable image loading
            webSettings.loadsImagesAutomatically = true

            webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
            webView.settings.userAgentString = "Mozilla/5.0 (Linux; Android 10; Pixel 4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Mobile Safari/537.36 Spotify/8.6.34.749"


            binding.artistSocialWebView.loadUrl(selectedArtist.links[0].toString())
        }

        return binding.root
    }

    suspend fun <T> Flow<List<T>>.flattenToList() =
        flatMapConcat { it.asFlow() }.toList()

}