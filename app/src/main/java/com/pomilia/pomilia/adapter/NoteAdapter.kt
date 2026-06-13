package com.pomilia.pomilia.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pomilia.pomilia.data.local.NoteEntity
import com.pomilia.pomilia.databinding.ItemNoteBinding

class NoteAdapter(
    private var notesList: List<NoteEntity>,
    private val onNoteClick: (NoteEntity) -> Unit,
    private val onDeleteClick: (NoteEntity) -> Unit
) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    class NoteViewHolder(
        val binding: ItemNoteBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NoteViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemNoteBinding.inflate(
            layoutInflater,
            parent,
            false
        )
        return NoteViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: NoteViewHolder,
        position: Int
    ) {
        val currentNote = notesList[position]

        holder.binding.textTitle.text = currentNote.title
        holder.binding.textSubject.text = currentNote.subject

        holder.binding.root.setOnClickListener {
            onNoteClick(currentNote)
        }

        holder.binding.buttonDelete.setOnClickListener {
            onDeleteClick(currentNote)
        }
    }

    override fun getItemCount(): Int {
        return notesList.size
    }

    fun updateData(newNotes: List<NoteEntity>) {
        notesList = newNotes
        notifyDataSetChanged()
    }
}