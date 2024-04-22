package nathan.timothy.prog39402finalproject

import android.view.View
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import nathan.timothy.prog39402finalproject.EventEntity
import nathan.timothy.prog39402finalproject.R
import nathan.timothy.prog39402finalproject.databinding.EventBinding

class EventViewHolder(private val binding: EventBinding, private val navController: NavController) : RecyclerView.ViewHolder(binding.root) {

    fun bind(event: EventEntity) {
        binding.event = event

        binding.eventCard.setOnClickListener { view: View ->
            val bundle = bundleOf("eventId" to event.id)

            // Check if the current fragment is HomeFragment
            if (navController.currentDestination?.id == R.id.homeFragment) {
                // Navigate to EventDetailsFragment from HomeFragment
                navController.navigate(R.id.action_homeFragment_to_eventDetailsFragment, bundle)
            } else {
                // Navigate to EventDetailsFragment using the default action
                navController.navigate(R.id.action_eventFragment_to_eventDetailsFragment, bundle)
            }
        }
    }
}
