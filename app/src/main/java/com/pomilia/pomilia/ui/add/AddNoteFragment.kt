package com.pomilia.pomilia.ui.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.pomilia.pomilia.R
import com.pomilia.pomilia.data.local.NoteEntity
import com.pomilia.pomilia.databinding.FragmentAddNoteBinding
import com.pomilia.pomilia.viewmodel.NoteViewModel

class AddNoteFragment : Fragment() {

    private var _binding: FragmentAddNoteBinding? = null
    private val binding get() = _binding!!

    private val viewModel: NoteViewModel by activityViewModels()

    private var noteId: Int = -1

    private var currentNote: NoteEntity? = null

    private val categories = listOf(
        "Seleziona categoria",
        "Appunti",
        "Riassunti",
        "Quiz"
    )

    private val subjects = listOf(
        "Seleziona materia",
        "Matematica",
        "Fisica",
        "Informatica",
        "Chimica"
    )

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

        noteId = arguments?.getInt("noteId", -1) ?: -1

        setupSpinners()
        setupButtons()
        setupEditModeIfNeeded()
    }

    private fun setupSpinners() {
        val categoryAdapter = ArrayAdapter(
            requireContext(),
            R.layout.spinner_item,
            categories
        )

        categoryAdapter.setDropDownViewResource(
            R.layout.spinner_dropdown_item
        )

        binding.spinnerCategory.adapter = categoryAdapter

        val subjectAdapter = ArrayAdapter(
            requireContext(),
            R.layout.spinner_item,
            subjects
        )

        subjectAdapter.setDropDownViewResource(
            R.layout.spinner_dropdown_item
        )

        binding.spinnerSubject.adapter = subjectAdapter
    }

    private fun setupButtons() {
        binding.buttonCancel.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.buttonSave.setOnClickListener {
            saveNote()
        }
    }

    private fun setupEditModeIfNeeded() {
        if (noteId == -1) {
            binding.textTitleScreen.text = "Nuova Nota"
            binding.buttonSave.text = "Salva"
            return
        }

        binding.textTitleScreen.text = "Modifica Nota"
        binding.buttonSave.text = "Aggiorna"

        viewModel.getNoteById(noteId).observe(viewLifecycleOwner) { note ->
            if (note == null) {
                return@observe
            }

            binding.editTitle.setText(note.title)
            binding.editContent.setText(note.content)

            val categoryPosition = categories.indexOf(note.category)
            if (categoryPosition >= 0) {
                binding.spinnerCategory.setSelection(categoryPosition)
            }

            val subjectPosition = subjects.indexOf(note.subject)
            if (subjectPosition >= 0) {
                binding.spinnerSubject.setSelection(subjectPosition)
            }
        }
    }

    private fun saveNote() {
        val title = binding.editTitle.text.toString().trim()
        val content = binding.editContent.text.toString().trim()
        val category = binding.spinnerCategory.selectedItem.toString()
        val subject = binding.spinnerSubject.selectedItem.toString()

        if (
            title.isEmpty() ||
            content.isEmpty() ||
            category == "Seleziona categoria" ||
            subject == "Seleziona materia"
        ) {
            return
        }

        if (noteId == -1) {
            viewModel.addNote(
                title = title,
                content = content,
                category = category,
                subject = subject
            )
        } else {
            val updatedNote = NoteEntity(
                id = noteId,
                title = title,
                content = content,
                category = category,
                subject = subject,
                ownerUsername = currentNote?.ownerUsername ?: "unknown_user"

            )

            viewModel.updateNote(updatedNote)
        }

        findNavController().popBackStack()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}