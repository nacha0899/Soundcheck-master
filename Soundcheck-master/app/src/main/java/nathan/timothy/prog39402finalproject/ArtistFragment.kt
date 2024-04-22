package nathan.timothy.prog39402finalproject

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.toList
import nathan.timothy.prog39402finalproject.databinding.FragmentArtistBinding

class ArtistFragment : Fragment() {

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
        val binding: FragmentArtistBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_artist, container, false)
        var genres: ArrayList<String> = arrayListOf()

        genres = arrayListOf(
            "Blues", "Country", "Dance", "Disco", "Funk",
            "Hip-Hop", "Jazz", "Metal", "Oldies", "Other", "Pop", "R&B",
            "Reggae", "Rock", "Techno", "Alternative", "Classical",
            "Gospel", "Soul", "Punk", "Electronic", "Comedy",
            "Folk", "Latin", "Celtic", "Acoustic",
            "Opera", "Chamber Music"
        )

        binding.searchEditText.setText("")

        // Get the artist list from the database
        viewModel.retrieveAllArtists()
        Thread.sleep(3000L)
        viewModel.allArtists.observe(this.viewLifecycleOwner) { items ->
            items.let {
                binding.recyclerView.adapter = ArtistRecyclerView(it, findNavController())
                Toast.makeText(requireContext(), "${it.size}", Toast.LENGTH_SHORT).show()
            }
        }

        // Genre spinner adapter
        val arrayGenreAdapter = activity?.let {
            ArrayAdapter<String>(
                it,
                android.R.layout.simple_spinner_item,
                genres
            )
        }

        val myGenreSpinner = binding.genreSpinner
        myGenreSpinner.adapter = arrayGenreAdapter
        myGenreSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, view: View?, position: Int, id: Long) {

                if (position != 0) {
                    viewModel.retrieveArtistsByGenre(genres[position])
                    Thread.sleep(1000L)
                    viewModel.allArtists.observe(viewLifecycleOwner) { items ->
                        items.let {
                            binding.recyclerView.adapter = ArtistRecyclerView(it, findNavController())
                            Toast.makeText(
                                requireContext(),
                                "${it.size} Bands Found that match your genre!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                    binding.recyclerView.layoutManager = LinearLayoutManager(context)
                    binding.recyclerView.setHasFixedSize(true)
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                // DOES NOTHING-SKIP
            }
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.setHasFixedSize(true)

        binding.searchButton.setOnClickListener() {
            val searchWord = binding.searchEditText.text.toString()

            // Get bands that match the searched band
            if (!searchWord.equals("")) {
                viewModel.retrieveArtistsByName(searchWord)
            } else {
                viewModel.retrieveAllArtists()
            }

            Thread.sleep(1000L)
            viewModel.allArtists.observe(this.viewLifecycleOwner) { items ->
                items.let {
                    binding.recyclerView.adapter = ArtistRecyclerView(it, findNavController())
                    Toast.makeText(
                        requireContext(),
                        "${it.size} Bands found that match your searched name!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            binding.recyclerView.layoutManager = LinearLayoutManager(context)
            binding.recyclerView.setHasFixedSize(true)
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        val spinner = this.view?.findViewById<Spinner>(R.id.genreSpinner)
        spinner?.setSelection(0)
    }

    suspend fun <T> Flow<List<T>>.flattenToList() =
        flatMapConcat { it.asFlow() }.toList()
}
