package com.example.adrian.todolist.tasks

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adrian.todolist.R
import com.example.adrian.todolist.database.TaskDatabase
import com.example.adrian.todolist.databinding.FragmentTaskBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class TaskFragment : Fragment() {

    lateinit var taskViewModel: TaskViewModel

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        // Get a reference to the binding object and inflate the fragment views.
        val binding: FragmentTaskBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_task, container, false)

        // Reference to the application context
        val application = requireNotNull(this.activity).application

        //Reference to DAO database
        val dataSource = TaskDatabase.getInstance(application).taskDatabaseDao

        //Instance of the VMF
        val viewModelFactory = TaskViewModelFactory(dataSource, application)

        //Reference to the VM
        taskViewModel = ViewModelProviders.of(this, viewModelFactory).get(TaskViewModel::class.java)

        val adapter = TaskAdapter(TaskAdapter.TaskListener { taskId ->
            taskViewModel.onTaskClicked(taskId)
        })

        binding.sleepList.adapter = adapter

        binding.lifecycleOwner = this

        binding.viewModel = taskViewModel

        setHasOptionsMenu(true)

        val manager = LinearLayoutManager(activity)
        binding.sleepList.layoutManager = manager

        taskViewModel.tasks.observe(viewLifecycleOwner, Observer { taskList ->
            taskList?.let {
                adapter.submitList(taskList)
            }
        })

        taskViewModel.navigateToTaskDetail.observe(viewLifecycleOwner, Observer { night ->
            night?.let {
                this.findNavController().navigate(TaskFragmentDirections
                    .actionTaskFragmentToTaskDetailFragment(night))
                taskViewModel.onSleepDataQualityNavigated()
            }
        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_main, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_settings -> taskViewModel.onClear()
            else -> super.onOptionsItemSelected(item)
        }
        return true
    }
}