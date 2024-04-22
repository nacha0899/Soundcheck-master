package nathan.timothy.prog39402finalproject

import android.view.View
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import nathan.timothy.prog39402finalproject.ArtistEntity
import nathan.timothy.prog39402finalproject.R
import nathan.timothy.prog39402finalproject.databinding.ArtistBinding

class ArtistViewHolder(val binding: ArtistBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(artist: ArtistEntity, navController: NavController) {
        binding.artist = artist

        // ADD NAVIGATION TO ARTIST DETAIL VIEW
        binding.artistCard.setOnClickListener { view: View ->
            val bundle = bundleOf("artistId" to artist.id)

            // Check if the current fragment is HomeFragment
            if (navController.currentDestination?.id == R.id.homeFragment) {
                // Navigate to ArtistDetailsFragment from HomeFragment
                navController.navigate(R.id.action_homeFragment_to_artistDetailsFragment, bundle)
            } else {
                // Navigate to ArtistDetailsFragment using the default action
                navController.navigate(R.id.action_artistFragment_to_artistDetailsFragment, bundle)
            }
        }
    }
}

