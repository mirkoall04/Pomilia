package com.pomilia.pomilia.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.pomilia.pomilia.adapter.NoteAdapter
import com.pomilia.pomilia.databinding.FragmentHomeBinding
import com.pomilia.pomilia.viewmodel.NoteViewModel
import com.pomilia.pomilia.R
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    // Colleghiamo il ViewModel esistente usando la delega "by viewModels()"
    private val viewModel: NoteViewModel by activityViewModels()

    // Dichiariamo l'adapter (inizialmente vuoto)
    private lateinit var noteAdapter: NoteAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inizializziamo l'Adapter con una lista vuota e la logica di cancellazione
        noteAdapter = NoteAdapter(emptyList()) { note ->
            // Questo blocco viene eseguito quando viene cliccato il cestino nell'adapter
            viewModel.deleteNote(note)
        }

        // Colleghiamo l'adapter al RecyclerView presente nel layout XML
        binding.recyclerViewNotes.adapter = noteAdapter

        binding.buttonAddNote.setOnClickListener {
            findNavController().navigate(
                R.id.action_homeFragment_to_addNoteFragment
            )
        }

        // Osserviamo la "bacheca" (LiveData) delle note nel ViewModel.
        // Ogni volta che la lista delle note cambia (o viene caricata all'inizio),
        // questo blocco di codice viene eseguito automaticamente.
        viewModel.notes.observe(viewLifecycleOwner) { listaDiNote ->
            // Passiamo la nuova lista all'adapter per aggiornare lo schermo
            noteAdapter.updateData(listaDiNote)
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}