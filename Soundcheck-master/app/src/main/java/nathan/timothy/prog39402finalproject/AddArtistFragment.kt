package nathan.timothy.prog39402finalproject

import android.app.AlertDialog
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
import nathan.timothy.prog39402finalproject.databinding.FragmentAddArtistBinding

class AddArtistFragment : Fragment() {

    private lateinit var musicDatabase: MusicDatabase
    private lateinit var selectedGenre: String
    private var imageBand: Int = 0

    private val artistViewModel: ArtistViewModel by activityViewModels {
        ArtistViewModelFactory((activity?.application as MusicApplication).database.artistDao())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentAddArtistBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_add_artist, container, false)
        var genres: ArrayList<String> = arrayListOf()

        //ARRAY FOR GENRES
        genres = arrayListOf(
            "Blues", "Country", "Dance", "Disco", "Funk",
            "Hip-Hop", "Jazz", "Metal", "Oldies", "Other", "Pop", "R&B",
            "Reggae", "Rock", "Techno", "Alternative", "Classical",
            "Gospel", "Soul", "Punk", "Electronic", "Comedy",
            "Folk", "Latin", "Celtic", "Acoustic",
            "Opera", "Chamber Music"
        )

        //ARRAY ADAPTER FOR GENRE SELECTION:
        val arrayAdapter = activity?.let {
            ArrayAdapter<String>(
                it,
                android.R.layout.simple_spinner_item,
                genres
            )
        }

        //SPINNER FOR GENRE SELECTION
        val genreSpinner = binding.genreSpinner
        genreSpinner.adapter = arrayAdapter
        genreSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                p0: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedGenre = genres[position]

                // Image Resource Allocator. Allocate a band picture for a band depending on the genre
                imageBand = when {
                    selectedGenre == "Country" || selectedGenre == "Oldies" || selectedGenre == "Folk" || selectedGenre =="Acoustic" ->R.drawable.country
                    selectedGenre == "Dance"|| selectedGenre == "Disco" || selectedGenre == "Funk" || selectedGenre == "R&B" || selectedGenre =="Gospel" || selectedGenre == "Soul" ->R.drawable.disco
                    selectedGenre == "Blues"|| selectedGenre == "Jazz" ->R.drawable.jazz
                    selectedGenre == "Metal"|| selectedGenre == "Rock" || selectedGenre == "Alternative" || selectedGenre == "Punk" ->R.drawable.metal
                    selectedGenre == "Opera"|| selectedGenre == "Chamber Music" ->R.drawable.opera
                    selectedGenre == "Latin"|| selectedGenre == "Celtic" ->R.drawable.latin
                    selectedGenre == "Comedy" ->R.drawable.comedy
                    else -> 0 // Set a default value if no match is found
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                // Do nothing here
            }
        }

        //ADD ARTISTS TO SYSTEM VIA BUTTON

        val addband = binding.addBandBtn.setOnClickListener { view: View ->

            var name = binding.nameEditText.text.toString()
            var genre = selectedGenre
            var members = binding.membersEditText.text.toString()
            var social = binding.links1EditText.text.toString()
            var muLink = binding.links2EditText.text.toString()
            var extraLink = binding.links3EditText.text.toString()

            //ALERTS FOR BLANK OR INCORRECT DATA ENTRY

            if (name.isBlank() || genre.isBlank() || members.isBlank()) {

                val alertDialog = AlertDialog.Builder(requireContext()).create()
                alertDialog.setTitle("Fields Empty")
                alertDialog.setMessage("You cannot have an empty name, genre, or name of members")
                alertDialog.setButton(
                    AlertDialog.BUTTON_POSITIVE, "Ok"
                ) { dialog, which -> dialog.dismiss() }
                alertDialog.show()

            } else {
                // combine edit texts Social media links to a List<String>
                val artistSocial = binding.links1EditText.text.toString()
                val artistMusicLink = binding.links2EditText.text.toString()
                val artistExtraLink = binding.links3EditText.text.toString()

                if (artistSocial.isBlank() || !artistSocial.matches("\\b(https?://)?(www\\.)?(instagram\\.com|facebook\\.com|X\\.com)\\b".toRegex())) {
                    // Handle invalid social link
                }

                if (artistMusicLink.isBlank() || !artistMusicLink.matches("\\b(?:https?://)?(?:www\\.)?(?:open\\.spotify\\.com/|music\\.apple\\.com/|youtube\\.com/c/)[^\\s]+\\b".toRegex())) {
                    // Handle invalid music link
                }

                if (artistExtraLink.isBlank() || (!artistExtraLink.matches("\\b(?:https?://)?(?:www\\.)?(?:open\\.spotify\\.com/|music\\.apple\\.com/|youtube\\.com/c/)[^\\s]+\\b".toRegex())
                            && !artistExtraLink.matches("\\b(https?://)?(www\\.)?(instagram\\.com|facebook\\.com|X\\.com)\\b".toRegex()))
                ) {
                    // Handle invalid extra link
                }

                // Members as a single string
                val memberListText = binding.membersEditText.text.toString()
                // splits the memberlist text into a List<String> delimited by commas
                val membersList = memberListText.split(",").map { it.trim() }

                // combine edit texts Social media links to a List<String>
                val artistLinks = listOf<String>(artistSocial, artistMusicLink, artistExtraLink)

                val artist = ArtistEntity(
                    0,
                    imageResource = imageBand,
                    name = binding.nameEditText.text.toString(),
                    genre = selectedGenre,
                    members = membersList,
                    links = artistLinks
                )

                artistViewModel.insertArtist(artist)
                Toast.makeText(requireContext(), "Successfully Added Artist!", Toast.LENGTH_SHORT).show()

                binding.genreSpinner.setSelection(0)
                binding.nameEditText.setText("")
                binding.links1EditText.setText("")
                binding.links2EditText.setText("")
                binding.links3EditText.setText("")
                binding.membersEditText.setText("")
            }
        }

        return binding.root
    }
}
