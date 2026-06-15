package com.pomilia.pomilia.ui.add

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
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

    private var selectedFileUri: String? = null
    private var selectedFileName: String? = null

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

    private val filePickerLauncher = registerForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        if (uri != null) {
            requireContext().contentResolver.takePersistableUriPermission(
                uri,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )

            selectedFileUri = uri.toString()
            selectedFileName = getFileName(uri)

            binding.textSelectedFile.text =
                selectedFileName ?: "File selezionato"
        }
    }

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

        binding.buttonAttachFile.setOnClickListener {
            filePickerLauncher.launch(arrayOf("*/*"))
        }

        binding.buttonSave.setOnClickListener {
            saveNote()
        }
    }

    private fun setupEditModeIfNeeded() {
        if (noteId == -1) {
            binding.textTitleScreen.text = "Nuova Nota"
            binding.buttonSave.text = "Salva"
            binding.textSelectedFile.text = "Nessun file selezionato"
            return
        }

        binding.textTitleScreen.text = "Modifica Nota"
        binding.buttonSave.text = "Aggiorna"

        viewModel.getNoteById(noteId).observe(viewLifecycleOwner) { note ->
            if (note == null) {
                return@observe
            }

            currentNote = note

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

            selectedFileUri = note.fileUri
            selectedFileName = note.fileName

            binding.textSelectedFile.text =
                note.fileName ?: "Nessun file selezionato"
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
                subject = subject,
                fileUri = selectedFileUri,
                fileName = selectedFileName
            )
        } else {
            val updatedNote = NoteEntity(
                id = noteId,
                title = title,
                content = content,
                category = category,
                subject = subject,
                ownerUsername = currentNote?.ownerUsername ?: "unknown_user",
                fileUri = selectedFileUri ?: currentNote?.fileUri,
                fileName = selectedFileName ?: currentNote?.fileName
            )

            viewModel.updateNote(updatedNote)
        }

        findNavController().popBackStack()
    }

    private fun getFileName(uri: Uri): String? {
        val cursor = requireContext().contentResolver.query(
            uri,
            null,
            null,
            null,
            null
        )

        cursor?.use {
            val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)

            if (it.moveToFirst() && nameIndex >= 0) {
                return it.getString(nameIndex)
            }
        }

        return null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}