package project.llmevaluation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import project.llmevaluation.R
import project.llmevaluation.databinding.FragmentSettingsBinding
import project.llmevaluation.utils.PreferenceManager
import project.llmevaluation.viewmodel.LLMViewModel

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LLMViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val prefManager = PreferenceManager(requireContext())
        binding.editApiKey.setText(prefManager.getApiKey())

        binding.btnSaveKey.setOnClickListener {
            val key = binding.editApiKey.text.toString()
            if (key.isNotBlank()) {
                prefManager.saveApiKey(key)
                Toast.makeText(context, "API Key Saved", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnClearHistory.setOnClickListener {
            viewModel.clearAllHistory()
            Toast.makeText(context, "History Cleared", Toast.LENGTH_SHORT).show()
        }

        binding.btnAbout.setOnClickListener {
            findNavController().navigate(R.id.action_settingsFragment_to_aboutFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
