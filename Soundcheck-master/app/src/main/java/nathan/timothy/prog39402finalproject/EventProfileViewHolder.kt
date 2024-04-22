package nathan.timothy.prog39402finalproject

import android.view.View
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import nathan.timothy.prog39402finalproject.databinding.EventBinding

class EventProfileViewHolder(private val binding: EventBinding): RecyclerView.ViewHolder(binding.root){
    fun bind(event: EventEntity) {
        binding.event = event

        binding.eventCard.setOnClickListener {view : View ->
            val bundle = bundleOf("eventId" to event.id)
            view.findNavController().navigate(R.id.action_profileFragment_to_eventDetailsFragment, bundle)
        }
    }


}