package com.example.adrian.todolist.taskDetail

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.adrian.todolist.R
import com.example.adrian.todolist.database.TaskDatabase
import com.example.adrian.todolist.databinding.FragmentDetailTaskBinding
import com.google.android.material.snackbar.Snackbar

class TaskDetailFragment : Fragment() {

    lateinit var taskDetailViewModel: TaskDetailViewModel

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentDetailTaskBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_detail_task, container, false)

        val application = requireNotNull(this.activity).application
        val arguments = TaskDetailFragmentArgs.fromBundle(requireArguments())

        val dataSource = TaskDatabase.getInstance(application).taskDatabaseDao
        val viewModelFactory = TaskDetailViewModelFactory(arguments.taskKey, dataSource)

        taskDetailViewModel = ViewModelProviders.of(this, viewModelFactory).get(TaskDetailViewModel::class.java)

        binding.viewModel = taskDetailViewModel

        binding.lifecycleOwner = this

        setHasOptionsMenu(true)

        taskDetailViewModel.navigateToTaskFragment.observe(viewLifecycleOwner, Observer {
            if (it == true) { // Observed state is true.
                this.findNavController().navigate(
                    TaskDetailFragmentDirections.actionTaskDetailToTaskFragment())
                taskDetailViewModel.doneNavigating()
            }
        })

        taskDetailViewModel.showSnackBarEvent.observe(viewLifecycleOwner, Observer {
            if (it == true) { // Observed state is true.
                Snackbar.make(
                    requireActivity().findViewById(android.R.id.content),
                    getString(R.string.warning_empty),
                    Snackbar.LENGTH_SHORT // How long to display the message.
                ).show()
                taskDetailViewModel.doneShowingSnackbar()
            }
        })

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_edit, menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_settings -> taskDetailViewModel.onClearItem()
            else -> super.onOptionsItemSelected(item)
        }
        return true
    }

}