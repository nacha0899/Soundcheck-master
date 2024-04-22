package nathan.timothy.prog39402finalproject

import nathan.timothy.prog39402finalproject.ArtistViewHolder
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import nathan.timothy.prog39402finalproject.databinding.ArtistBinding
import kotlin.random.Random

class ArtistRecyclerView(
     val artistList: List<ArtistEntity>,
     val navController: NavController
) : RecyclerView.Adapter<ArtistViewHolder>() {

    private lateinit var binding: ArtistBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistViewHolder{
        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.artist, parent, false)
        return ArtistViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ArtistViewHolder, position: Int) {
        val currentItem = artistList[position]
        holder.bind(currentItem, navController)

        // random colour generator
        val randomcolor = Random.Default
        val red = randomcolor.nextInt(0, 128).toString(16).padStart(2, '0')
        val green = randomcolor.nextInt(0, 128).toString(16).padStart(2, '0')
        val blue = randomcolor.nextInt(0, 128).toString(16).padStart(2, '0')

        val color = "#$red$green$blue"
        holder.binding.artistCard.setCardBackgroundColor(Color.parseColor(color))
    }

    override fun getItemCount() = artistList.size
}
