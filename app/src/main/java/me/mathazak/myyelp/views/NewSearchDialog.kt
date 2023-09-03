package me.mathazak.myyelp.views

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.transition.MaterialContainerTransform
import me.mathazak.myyelp.R
import me.mathazak.myyelp.YelpApplication
import me.mathazak.myyelp.databinding.DialogNewSearchBinding
import me.mathazak.myyelp.viewmodels.BusinessViewModel
import me.mathazak.myyelp.viewmodels.BusinessViewModelFactory

class NewSearchDialog : DialogFragment() {

    private var _binding: DialogNewSearchBinding? = null
    private val binding get() = _binding!!

    private val viewModel: BusinessViewModel by activityViewModels {
        BusinessViewModelFactory(
            (activity?.application as YelpApplication).repository
        )
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogNewSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.etTerm.editText?.setText(viewModel.searchTerm)

        binding.searchButton.setOnClickListener {
            viewModel.searchTerm = binding.etTerm.editText?.text.toString()
            viewModel.fetchNewSearch()
            dismissDialog()
        }
        binding.cancelButton.setOnClickListener {
            dismissDialog()
        }
    }

    private fun dismissDialog() {
        val inputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as
                InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(binding.root.windowToken, 0)
        dismiss()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
