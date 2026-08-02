package project.llmevaluation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import project.llmevaluation.R
import project.llmevaluation.databinding.FragmentResultBinding
import project.llmevaluation.utils.ChartManager
import project.llmevaluation.utils.ExportManager
import project.llmevaluation.viewmodel.LLMViewModel

class ResultFragment : Fragment() {

    private var _binding: FragmentResultBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LLMViewModel by activityViewModels()
    private var loadingDialog: AlertDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    private lateinit var adapter: ResponseAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ResponseAdapter()
        binding.recyclerResponses.layoutManager = LinearLayoutManager(context)
        binding.recyclerResponses.adapter = adapter

        viewModel.evaluationResults.observe(viewLifecycleOwner) { results ->
            if (results.isNotEmpty()) {
                val winner = results.maxByOrNull { it.overallScore }
                binding.txtWinner.text = "Winner: ${winner?.model}"
                adapter.submitList(results)
                
                ChartManager.setupBarChart(binding.barChart, results)
                ChartManager.setupRadarChart(binding.radarChart, results)
                ChartManager.setupPieChart(binding.pieChart, results)
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) showLoading() else hideLoading()
        }

        binding.btnExportPdf.setOnClickListener {
            viewModel.evaluationResults.value?.let { results ->
                ExportManager.exportToPdf(requireContext(), results, "LLM_Evaluation_${System.currentTimeMillis()}")
            }
        }

        binding.btnExportCsv.setOnClickListener {
            viewModel.evaluationResults.value?.let { results ->
                ExportManager.exportToCsv(requireContext(), results, "LLM_Evaluation_${System.currentTimeMillis()}")
            }
        }
    }

    private fun showLoading() {
        if (loadingDialog == null) {
            val builder = AlertDialog.Builder(requireContext())
            val view = layoutInflater.inflate(R.layout.dialog_loading, null)
            builder.setView(view)
            builder.setCancelable(false)
            loadingDialog = builder.create()
        }
        loadingDialog?.show()
    }

    private fun hideLoading() {
        loadingDialog?.dismiss()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
