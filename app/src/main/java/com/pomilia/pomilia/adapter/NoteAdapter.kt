package com.pomilia.pomilia.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pomilia.pomilia.databinding.ItemNoteBinding
import com.pomilia.pomilia.model.Note

// L'Adapter riceve una lista di Note e le mostra a schermo
class NoteAdapter(private var notesList: List<Note>,
                  private val onDeleteClick: (Note) -> Unit ) :
    RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    // 1. Questa classe "ViewHolder" tiene i riferimenti ai componenti visivi di ogni singola riga
    class NoteViewHolder(val binding: ItemNoteBinding) : RecyclerView.ViewHolder(binding.root)

    // 2. Crea visivamente la riga (item_note) quando il RecyclerView ne ha bisogno
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemNoteBinding.inflate(layoutInflater, parent, false)
        return NoteViewHolder(binding)
    }

    // 3. Associa i dati di una specifica nota ai testi della riga visibile
    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val currentNote = notesList[position]

        // Assegniamo i valori della nota ai TextView grazie al ViewBinding
        holder.binding.textTitle.text = currentNote.title
        holder.binding.textSubject.text = currentNote.subject

        holder.binding.buttonDelete.setOnClickListener {
            onDeleteClick(currentNote) // Passiamo la nota corrente alla callback
        }
    }

    // 4. Dice ad Android quante note ci sono in totale nella lista
    override fun getItemCount(): Int {
        return notesList.size
    }

    // Funzione per aggiornare la lista quando i dati cambiano nel ViewModel
    fun updateData(newNotes: List<Note>) {
        this.notesList = newNotes
        notifyDataSetChanged() // Dice al RecyclerView di ridisegnare la lista
    }
}