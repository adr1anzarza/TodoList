package com.example.adrian.todolist.taskDetail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.adrian.todolist.R
import com.example.adrian.todolist.database.TaskDatabase
import com.example.adrian.todolist.databinding.FragmentDetailTaskBinding

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class TaskDetailFragment : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        // Get a reference to the binding object and inflate the fragment views.
        val binding: FragmentDetailTaskBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_detail_task, container, false)

        val application = requireNotNull(this.activity).application
        val arguments = TaskDetailFragmentArgs.fromBundle(requireArguments())

        // Create an instance of the ViewModel Factory.
        val dataSource = TaskDatabase.getInstance(application).taskDatabaseDao
        val viewModelFactory = TaskDetailViewModelFactory(arguments.taskKey, dataSource)

        // Get a reference to the ViewModel associated with this fragment.
        val sleepDetailViewModel = ViewModelProviders.of(this, viewModelFactory).get(TaskDetailViewModel::class.java)

        // To use the View Model with data binding, you have to explicitly
        // give the binding object a reference to it.
        binding.viewModel = sleepDetailViewModel

        binding.lifecycleOwner = this

        // Add an Observer to the state variable for Navigating when a Quality icon is tapped.
        sleepDetailViewModel.navigateToTaskFragment.observe(viewLifecycleOwner, Observer {
            if (it == true) { // Observed state is true.
                this.findNavController().navigate(
                    TaskDetailFragmentDirections.actionTaskDetailToTaskFragment())
                // Reset state to make sure we only navigate once, even if the device
                // has a configuration change.
                sleepDetailViewModel.doneNavigating()
            }
        })

        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }
}