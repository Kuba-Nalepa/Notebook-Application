package com.nalepa.projectnr2.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.nalepa.projectnr2.NotebookApplication
import com.nalepa.projectnr2.adapter.NoteAdapter
import com.nalepa.projectnr2.databinding.FragmentMainMenuBinding
import com.nalepa.projectnr2.viewmodel.NotebookViewModel

class MainMenuFragment : Fragment() {
    private var _binding: FragmentMainMenuBinding? = null
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
        _binding = FragmentMainMenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = NoteAdapter{
            val action = MainMenuFragmentDirections.actionMainMenuFragmentToNoteDetailsFragment(it.id)
            this.findNavController().navigate(action)
        }
        binding.recyclerView.adapter = adapter
        viewModel.allNotes.observe(this.viewLifecycleOwner) { notes ->
            notes.let {
                adapter.submitList(it)
            }
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(this.context)
        binding.floatingActionButton.setOnClickListener{
            val action = MainMenuFragmentDirections.actionMainMenuFragmentToAddNewNoteFragment("Add New Note")
            this.findNavController().navigate(action)
        }
    }
}