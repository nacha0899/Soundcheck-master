package nathan.timothy.prog39402finalproject

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import nathan.timothy.prog39402finalproject.databinding.EventBinding
import kotlin.random.Random

class EventProfileRecyclerView (private val eventList:List<EventEntity>): RecyclerView.Adapter<EventProfileViewHolder>()  {

    private lateinit var binding: EventBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventProfileViewHolder {
        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.event, parent, false)
        return EventProfileViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EventProfileViewHolder, position: Int) {
        val currentItem = eventList[position]
        holder.bind(currentItem)

//random colour generator
        var randomcolor = Random.Default
        var red = randomcolor.nextInt(0,128).toString(16).padStart(2,'0')
        var green = randomcolor.nextInt(0,128).toString(16).padStart(2,'0')
        var blue = randomcolor.nextInt(0,128).toString(16).padStart(2,'0')

        var color = "#$red$green$blue"

        binding.eventCard.setCardBackgroundColor(Color.parseColor(color))

    }

    override fun getItemCount() =  eventList.size
}