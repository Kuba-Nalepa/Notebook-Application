package com.nalepa.projectnr2.viewmodel

import androidx.lifecycle.*
import com.nalepa.projectnr2.room.Note
import com.nalepa.projectnr2.room.NoteDao
import kotlinx.coroutines.launch

class NotebookViewModel(private val noteDao: NoteDao): ViewModel() {
    val allNotes: LiveData<List<Note>> = noteDao.getNotes().asLiveData()

    private fun insertNote(note: Note) {
        viewModelScope.launch {
            noteDao.insert(note)
        }
    }

     private fun getNewNote(noteTitle: String, noteDesc: String): Note {
        return Note(
            title = noteTitle,
            description = noteDesc
        )
    }

    private fun getUpdatedNote(noteId: Int, noteTitle: String, noteDesc: String): Note {
        return Note(
            id = noteId,
            title = noteTitle,
            description = noteDesc
        )
    }

    fun addNewNote(noteTitle: String, noteDesc: String) {
        val newNote = getNewNote(noteTitle, noteDesc)
        insertNote(newNote)
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            noteDao.delete(note)
        }
    }

    fun updateNote(
        noteId: Int,
        noteTitle: String,
        noteDesc: String
    ) {
        val updatedNote = getUpdatedNote(noteId, noteTitle, noteDesc)
        viewModelScope.launch {
            noteDao.update(updatedNote)
        }
    }

    fun retrieveNoteDetails(noteId: Int) : LiveData<Note> {
        return noteDao.getNote(noteId).asLiveData()
    }

    class NotebookViewModelFactory(private val noteDao: NoteDao): ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(NotebookViewModel::class.java)) {
                @Suppress
                return NotebookViewModel(noteDao) as T
            }
            throw IllegalArgumentException("Unknown ViewModel")
        }
    }
}