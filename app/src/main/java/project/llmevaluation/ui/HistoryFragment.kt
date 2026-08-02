package project.llmevaluation.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import project.llmevaluation.databinding.FragmentHistoryBinding
import project.llmevaluation.viewmodel.LLMViewModel

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LLMViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    private lateinit var adapter: HistoryAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = HistoryAdapter { id ->
            viewModel.deleteHistory(id)
        }
        binding.recyclerHistory.layoutManager = LinearLayoutManager(context)
        binding.recyclerHistory.adapter = adapter
        
        // Initial load
        viewModel.getAllHistory().asLiveData().observe(viewLifecycleOwner) { history ->
            updateUI(history)
        }

        binding.editSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString()
                if (query.isEmpty()) {
                    viewModel.getAllHistory().asLiveData().observe(viewLifecycleOwner) { updateUI(it) }
                } else {
                    viewModel.searchHistory(query).asLiveData().observe(viewLifecycleOwner) { updateUI(it) }
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun updateUI(history: List<project.llmevaluation.data.HistoryEntity>) {
        if (history.isEmpty()) {
            binding.txtEmptyHistory.visibility = View.VISIBLE
            binding.recyclerHistory.visibility = View.GONE
        } else {
            binding.txtEmptyHistory.visibility = View.GONE
            binding.recyclerHistory.visibility = View.VISIBLE
            adapter.submitList(history)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
