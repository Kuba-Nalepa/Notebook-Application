package com.nalepa.projectnr2.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nalepa.projectnr2.databinding.ListItemBinding
import com.nalepa.projectnr2.room.Note

class NoteAdapter(private val onNoteClicked: (Note) -> Unit):
        ListAdapter<Note, NoteAdapter.NoteViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder(
            ListItemBinding.inflate(
                LayoutInflater.from(
                    parent.context
                )
            )
        )
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val currentNote = getItem(position)
        holder.itemView.setOnClickListener {
            onNoteClicked(currentNote)
        }
        holder.bind(currentNote)
    }

    class NoteViewHolder(private var binding: ListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(note: Note) {
            binding.apply {
                titleTextView.text = note.title
            }
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Note>() {
            override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
                return oldItem.title == newItem.title
            }

        }
    }

}