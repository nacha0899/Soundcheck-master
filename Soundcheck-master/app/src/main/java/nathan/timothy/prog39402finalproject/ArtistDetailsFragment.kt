package nathan.timothy.prog39402finalproject

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.google.android.gms.maps.OnMapReadyCallback
import nathan.timothy.prog39402finalproject.databinding.ArtistBinding.bind
import nathan.timothy.prog39402finalproject.databinding.FragmentArtistDetailsBinding


class ArtistDetailsFragment : Fragment() {

    private var _binding: FragmentArtistDetailsBinding? = null

    private val binding get() = _binding!!
    lateinit var artist: ArtistEntity
    private lateinit var musicDatabase: MusicDatabase

    private val viewModel: ArtistViewModel by activityViewModels {
        ArtistViewModelFactory(
            (activity?.application as MusicApplication).database.artistDao()
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
        _binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_artist_details, container, false)


        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val selectedArtistId = arguments?.getLong("artistId")
        viewModel.retrieveArtistsById(selectedArtistId!!)
            .observe(this.viewLifecycleOwner) { selectedArtist ->
                artist = selectedArtist
                bind(artist)
            }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private  fun bind(selectedArtist: ArtistEntity){
        binding.apply {
            artist = selectedArtist
//            if(nameBackground.text.toString().isBlank()){
//                nameBackground.append(artist.name)
//            }

            musicBtn.setOnClickListener{view: View ->
                val bundle = bundleOf("artistId" to selectedArtist.id)
                if(selectedArtist.links[1].isBlank()){
                    Toast.makeText(requireContext(), "Band does not have a link to their music!", Toast.LENGTH_SHORT).show()
                }else{
                    view.findNavController().navigate(R.id.action_artistDetailsFragment_to_artistMusicLinkFragment, bundle)
                }
            }

            socialBtn.setOnClickListener{view: View ->
                val bundle = bundleOf("artistId" to selectedArtist.id)
                if(selectedArtist.links[0].isBlank()){
                    Toast.makeText(requireContext(), "Band doe snot have a link to their social media!", Toast.LENGTH_SHORT).show()
                }else{
                    view.findNavController().navigate(R.id.action_artistDetailsFragment_to_artistSocialMediaLinkFragment,bundle)
                }
            }


            plusBtn.setOnClickListener{view: View ->
                val bundle = bundleOf("artistId" to selectedArtist.id)
                if(selectedArtist.links[2].isBlank()){
                    Toast.makeText(requireContext(), "Band does not have a link to their extra media!", Toast.LENGTH_SHORT).show()
                }else{
                    view.findNavController().navigate(R.id.action_artistDetailsFragment_to_artistExtraLinkFragment, bundle)
                }
            }

        }
    }

}