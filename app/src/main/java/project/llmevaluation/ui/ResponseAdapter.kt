package project.llmevaluation.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import project.llmevaluation.databinding.ItemResponseBinding
import project.llmevaluation.models.EvaluationResult

class ResponseAdapter : ListAdapter<EvaluationResult, ResponseAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemResponseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(private val binding: ItemResponseBinding) :
        RecyclerView.ViewHolder(binding.root) {
        
        fun bind(item: EvaluationResult) {
            binding.txtModelName.text = item.model
            // In a real app we'd get the actual text from the response map
            // For now just show the score
            binding.txtModelScore.text = "Score: %.2f".format(item.overallScore)
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<EvaluationResult>() {
        override fun areItemsTheSame(oldItem: EvaluationResult, newItem: EvaluationResult): Boolean {
            return oldItem.model == newItem.model
        }

        override fun areContentsTheSame(oldItem: EvaluationResult, newItem: EvaluationResult): Boolean {
            return oldItem == newItem
        }
    }
}
