package com.nalepa.projectnr2.fragments

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.nalepa.projectnr2.NotebookApplication
import com.nalepa.projectnr2.databinding.FragmentNoteDetailsBinding
import com.nalepa.projectnr2.room.Note
import com.nalepa.projectnr2.viewmodel.NotebookViewModel


class NoteDetailsFragment : Fragment() {
    lateinit var note: Note

    private val navigationArgs: NoteDetailsFragmentArgs by navArgs()

    private var _binding: FragmentNoteDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: NotebookViewModel by activityViewModels {
        NotebookViewModel.NotebookViewModelFactory(
            (activity?.application as NotebookApplication).database.noteDao()
        )
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.detailDesc.movementMethod = ScrollingMovementMethod() // setting desc to be scrollable
        val id = navigationArgs.itemId                                  // if there were too many letters
        viewModel.retrieveNoteDetails(id).observe(this.viewLifecycleOwner) { selectedItem ->
            note = selectedItem
            bind(note)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentNoteDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showConfirmationDialogue() {
        MaterialAlertDialogBuilder(requireContext()).setTitle("Do You want to delete?")
            .setMessage("This action cannot be undone.")
            .setNegativeButton("No") {_,_ -> }
            .setPositiveButton("Yes") {_, _ -> deleteNote() }
            .show()
    }

    private fun deleteNote() {
            viewModel.deleteNote(note)
            findNavController().navigateUp()
    }

    private fun editNote() {
        val action = NoteDetailsFragmentDirections.actionNoteDetailsFragmentToAddNewNoteFragment(
            "Edit your note", note.id
        )
        this.findNavController().navigate(action)
    }

    private fun bind(note: Note) {
        binding.apply {
            detailTitle.text = note.title
            detailDesc.text = note.description
            deleteBtn.setOnClickListener { showConfirmationDialogue() }
            editBtn.setOnClickListener { editNote() }
        }
    }
}