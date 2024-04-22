package nathan.timothy.prog39402finalproject

import nathan.timothy.prog39402finalproject.EventViewHolder
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import nathan.timothy.prog39402finalproject.databinding.EventBinding
import kotlin.random.Random

class EventRecyclerView(private val eventList: List<EventEntity>, private val navController: NavController) :
    RecyclerView.Adapter<EventViewHolder>() {

    private lateinit var binding: EventBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.event,
            parent,
            false
        )
        return EventViewHolder(binding, navController)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val currentItem = eventList[position]
        holder.bind(currentItem)

        //random colour generator
        var randomcolor = Random.Default
        var red = randomcolor.nextInt(0, 128).toString(16).padStart(2, '0')
        var green = randomcolor.nextInt(0, 128).toString(16).padStart(2, '0')
        var blue = randomcolor.nextInt(0, 128).toString(16).padStart(2, '0')

        var color = "#$red$green$blue"

        binding.eventCard.setCardBackgroundColor(android.graphics.Color.parseColor(color))
    }

    override fun getItemCount() = eventList.size
}
