package com.pomilia.pomilia.ui.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.pomilia.pomilia.R
import com.pomilia.pomilia.databinding.FragmentDetailNoteBinding
import com.pomilia.pomilia.security.SessionManager
import com.pomilia.pomilia.viewmodel.NoteViewModel

class DetailNoteFragment : Fragment() {

    private var _binding: FragmentDetailNoteBinding? = null
    private val binding get() = _binding!!

    private val viewModel: NoteViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailNoteBinding.inflate(
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

        val noteId = arguments?.getInt("noteId") ?: return

        viewModel.getNoteById(noteId).observe(viewLifecycleOwner) { note ->
            if (note == null) {
                findNavController().popBackStack()
                return@observe
            }

            binding.textDetailTitle.text = note.title
            binding.textDetailSubject.text = note.subject
            binding.textDetailCategory.text = note.category
            binding.textDetailContent.text = note.content

            val sessionManager = SessionManager(requireContext())
            val currentUsername = sessionManager.getUsername()

            if (note.ownerUsername == currentUsername) {
                binding.buttonEdit.visibility = View.VISIBLE
            } else {
                binding.buttonEdit.visibility = View.GONE
            }

            if (note.fileUri != null && note.fileName != null) {
                binding.textAttachedFileLabel.visibility = View.VISIBLE
                binding.buttonOpenFile.visibility = View.VISIBLE
                binding.buttonOpenFile.text = "Apri: ${note.fileName}"

                binding.buttonOpenFile.setOnClickListener {
                    openAttachedFile(note.fileUri)
                }
            } else {
                binding.textAttachedFileLabel.visibility = View.GONE
                binding.buttonOpenFile.visibility = View.GONE
            }
        }

        binding.buttonBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.buttonEdit.setOnClickListener {
            val bundle = Bundle().apply {
                putInt("noteId", noteId)
            }

            findNavController().navigate(
                R.id.action_detailNoteFragment_to_addNoteFragment,
                bundle
            )
        }
    }

    private fun openAttachedFile(fileUri: String) {
        val uri = Uri.parse(fileUri)

        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, "*/*")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        try {
            startActivity(Intent.createChooser(intent, "Apri file con"))
        } catch (exception: Exception) {
            Toast.makeText(
                requireContext(),
                "Nessuna app disponibile per aprire questo file",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}