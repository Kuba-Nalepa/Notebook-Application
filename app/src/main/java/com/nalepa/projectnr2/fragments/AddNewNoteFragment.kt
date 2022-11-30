package com.nalepa.projectnr2.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.nalepa.projectnr2.NotebookApplication
import com.nalepa.projectnr2.databinding.FragmentAddNewNoteBinding
import com.nalepa.projectnr2.room.Note
import com.nalepa.projectnr2.viewmodel.NotebookViewModel

class AddNewNoteFragment : Fragment() {
    lateinit var note: Note

    private val navigationArgs: NoteDetailsFragmentArgs by navArgs()

    private var _binding: FragmentAddNewNoteBinding? = null
    private val binding get() = _binding!!

    private val viewModel: NotebookViewModel by activityViewModels {
        NotebookViewModel.NotebookViewModelFactory(
            (activity?.application as NotebookApplication).database.noteDao()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAddNewNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = navigationArgs.itemId
        if (id > 0) {
            viewModel.retrieveNoteDetails(id).observe(this.viewLifecycleOwner) {   selectedNote ->
                note = selectedNote
                bind(note)

            }
        } else {
            binding.btnAdd.setOnClickListener{
                if(binding.etTitle.text.toString().isEmpty()) {
                    val alert = Toast.makeText(context,"Title field is required",Toast.LENGTH_SHORT)
                    alert.show()
                } else {
                    addNote()
                }

            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun addNote() {
        viewModel.addNewNote(
            binding.etTitle.text.toString(),
            binding.etDesc.text.toString()
        )

        val action = AddNewNoteFragmentDirections.actionAddNewNoteFragmentToMainMenuFragment()
        findNavController().navigate(action)
    }

    private fun updateNote() {
        viewModel.updateNote(
            this.navigationArgs.itemId,
            this.binding.etTitle.text.toString(),
            this.binding.etDesc.text.toString()
        )
        val action = AddNewNoteFragmentDirections.actionAddNewNoteFragmentToMainMenuFragment()
        this.findNavController().navigate(action)
    }

    private fun bind(note: Note) {
        binding.apply {
            etTitle.setText(note.title)
            etDesc.setText(note.description)
            btnAdd.setOnClickListener { updateNote() }
        }
    }
}