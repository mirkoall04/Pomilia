package com.pomilia.pomilia.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.pomilia.pomilia.databinding.FragmentDetailNoteBinding
import com.pomilia.pomilia.viewmodel.NoteViewModel
import com.pomilia.pomilia.R

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}