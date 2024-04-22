package nathan.timothy.prog39402finalproject

import androidx.recyclerview.widget.RecyclerView
import nathan.timothy.prog39402finalproject.databinding.ArtistBinding

class ArtistProfileViewHolder(private val binding: ArtistBinding): RecyclerView.ViewHolder(binding.root)
{
    fun bind(artist: ArtistEntity){
        binding.artist = artist
    }
}