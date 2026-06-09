package com.pomilia.pomilia.ui.add

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.pomilia.pomilia.databinding.FragmentAddNoteBinding
import com.pomilia.pomilia.R
import com.pomilia.pomilia.viewmodel.NoteViewModel

/**
 * A simple [Fragment] subclass.
 * Use the [AddNoteFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddNoteFragment : Fragment() {

    private var _binding: FragmentAddNoteBinding? = null

    private val binding get() = _binding!!

    private val viewModel: NoteViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentAddNoteBinding.inflate(
            inflater,
            container,
            false
        )

        return binding.root
    }


    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        val categories = listOf(
            "Seleziona categoria",
            "Appunti",
            "Riassunti",
            "Quiz"
        )

        val subjects = listOf(
            "Seleziona materia",
            "Matematica",
            "Fisica",
            "Informatica",
            "Chimica"
        )

        // Spinner Categoria
        val categoryAdapter = ArrayAdapter(
            requireContext(),
            R.layout.spinner_item,
            categories
        )

        categoryAdapter.setDropDownViewResource(
            R.layout.spinner_dropdown_item
        )

        binding.spinnerCategory.adapter = categoryAdapter

        // Spinner Materia
        val subjectAdapter = ArrayAdapter(
            requireContext(),
            R.layout.spinner_item,
            subjects
        )

        subjectAdapter.setDropDownViewResource(
            R.layout.spinner_dropdown_item
        )

        binding.spinnerSubject.adapter = subjectAdapter

        // Pulsante Annulla
        binding.buttonCancel.setOnClickListener {
            findNavController().popBackStack()
        }

        // Pulsante Salva (per ora mostra solo i dati)
        binding.buttonSave.setOnClickListener {

            val title = binding.editTitle.text.toString().trim()

            val content = binding.editContent.text.toString().trim()

            val category =
                binding.spinnerCategory.selectedItem.toString()

            val subject =
                binding.spinnerSubject.selectedItem.toString()

            if (
                title.isEmpty() ||
                content.isEmpty() ||
                category == "Seleziona categoria" ||
                subject == "Seleziona materia"
            ) {
                return@setOnClickListener
            }

            viewModel.addNote(
                title,
                content,
                category,
                subject
            )

            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}