package project.llmevaluation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.Chip
import project.llmevaluation.BuildConfig
import project.llmevaluation.R
import project.llmevaluation.api.RetrofitClient
import project.llmevaluation.data.LLMEvaluationDatabase
import project.llmevaluation.databinding.FragmentPromptBinding
import project.llmevaluation.python.PythonBridge
import project.llmevaluation.repository.LLMRepository
import project.llmevaluation.utils.PreferenceManager
import project.llmevaluation.viewmodel.LLMViewModel
import project.llmevaluation.viewmodel.LLMViewModelFactory

class PromptFragment : Fragment() {

    private var _binding: FragmentPromptBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LLMViewModel by activityViewModels {
        val database = LLMEvaluationDatabase.getDatabase(requireContext())
        val repository = LLMRepository(RetrofitClient.apiService, database.historyDao())
        LLMViewModelFactory(repository, PythonBridge(requireContext()))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPromptBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnGenerate.setOnClickListener {
            val prompt = binding.editPrompt.text.toString()
            val selectedModels = getSelectedModels()

            if (prompt.isBlank()) {
                Toast.makeText(context, "Please enter a prompt", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (selectedModels.isEmpty()) {
                Toast.makeText(context, "Please select at least one model", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val prefManager = PreferenceManager(requireContext())
            val apiKey = if (BuildConfig.OPENROUTER_API_KEY.isNotBlank()) {
                BuildConfig.OPENROUTER_API_KEY
            } else {
                prefManager.getApiKey() ?: ""
            }

            if (apiKey.isBlank()) {
                Toast.makeText(context, "API Key missing! Add to local.properties or Settings.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            viewModel.compareModels(prompt, selectedModels, apiKey)
            findNavController().navigate(R.id.action_promptFragment_to_resultFragment)
        }
    }

    private fun getSelectedModels(): List<String> {
        val models = mutableListOf<String>()
        for (i in 0 until binding.chipGroupModels.childCount) {
            val chip = binding.chipGroupModels.getChildAt(i) as Chip
            if (chip.isChecked) {
                models.add(getModelId(chip.text.toString()))
            }
        }
        return models
    }

    private fun getModelId(displayName: String): String {
        return when (displayName) {
            "GPT-4o-mini" -> "openai/gpt-4o-mini"
            "Claude" -> "anthropic/claude-3-haiku"
            "Gemini" -> "google/gemini-pro-1.5"
            "Llama 3" -> "meta-llama/llama-3-8b-instruct"
            "Mistral" -> "mistralai/mistral-7b-instruct"
            "DeepSeek" -> "deepseek/deepseek-chat"
            else -> "openai/gpt-3.5-turbo"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
