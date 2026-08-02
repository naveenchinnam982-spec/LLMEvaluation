package project.llmevaluation.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import project.llmevaluation.data.HistoryEntity
import project.llmevaluation.databinding.ItemHistoryBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HistoryAdapter(private val onDelete: (Long) -> Unit) :
    ListAdapter<HistoryEntity, HistoryAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: ItemHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        
        fun bind(item: HistoryEntity) {
            binding.txtHistoryModel.text = item.modelName
            binding.txtHistoryPrompt.text = item.prompt
            binding.txtHistoryScore.text = "Overall Score: %.2f".format(item.overallScore)
            
            val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
            binding.txtHistoryDate.text = sdf.format(Date(item.timestamp))

            binding.root.setOnLongClickListener {
                onDelete(item.id)
                true
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<HistoryEntity>() {
        override fun areItemsTheSame(oldItem: HistoryEntity, newItem: HistoryEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: HistoryEntity, newItem: HistoryEntity): Boolean {
            return oldItem == newItem
        }
    }
}
