package com.codinginflow.mvvmtodo.ui.task

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.codinginflow.mvvmtodo.R
import com.codinginflow.mvvmtodo.base.BaseFragment
import com.codinginflow.mvvmtodo.databinding.FragmentTasksBinding
import com.codinginflow.mvvmtodo.db.entity.Task
import com.codinginflow.mvvmtodo.safe.ResultWrapper
import com.codinginflow.mvvmtodo.util.Constants.MODE_TOAST_ERROR
import com.codinginflow.mvvmtodo.util.exhaustive
import com.codinginflow.mvvmtodo.util.onQueryTextListener
import com.codinginflow.mvvmtodo.util.toasty
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TasksFragment: BaseFragment<FragmentTasksBinding>() , TasksAdapter.OnItemClickListener {

    private val viewModel: TasksViewModel by viewModels()

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentTasksBinding.inflate(inflater ,container ,false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val taskAdapter = TasksAdapter(this)

        binding?.apply {
            recyclerViewTasks.apply {
                adapter = taskAdapter
                setHasFixedSize(true)
            }
            ItemTouchHelper(object: ItemTouchHelper.SimpleCallback(0 ,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT){

                override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val task = taskAdapter.currentList[viewHolder.adapterPosition]
                    viewModel.onTaskSwiped(task)
                }


            }).attachToRecyclerView(recyclerViewTasks)

            fabAddTask.setOnClickListener {
                viewModel.onAddNewTaskClick()
            }
        }

        viewModel.tasks.observe(viewLifecycleOwner) { result ->
            when(result){
                is ResultWrapper.Success -> taskAdapter.submitList(result.data)
                is ResultWrapper.Error -> toasty("there is a problem when getting data from database" ,MODE_TOAST_ERROR)
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.tasksEvent.collect { event ->
                when(event){
                    is TasksViewModel.TasksEvent.ShowUndoDeleteTaskMessage -> {
                        Snackbar.make(requireView() ,"Task deleted" ,Snackbar.LENGTH_LONG)
                                .setAction("UNDO"){
                                    viewModel.onUndoDeleteClick(event.task)
                                }.show()
                    }
                    is TasksViewModel.TasksEvent.NavigateToAddTaskScreen -> {
                        val action = TasksFragmentDirections.actionTasksFragmentToAddEditTaskFragment(null ,"New Task")
                        findNavController().navigate(action)
                    }
                    is TasksViewModel.TasksEvent.NavigateToEditTaskScreen -> {
                        val action = TasksFragmentDirections.actionTasksFragmentToAddEditTaskFragment(event.task ,"Edit Task")
                        findNavController().navigate(action)
                    }
                }.exhaustive
            }
        }

        setHasOptionsMenu(true)

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_fragment_tasks ,menu)

        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView

        searchView.onQueryTextListener {
            viewModel.searchQuery.value = it
        }

        viewLifecycleOwner.lifecycleScope.launch {
            menu.findItem(R.id.action_hide_completed_tasks).isChecked =
                    viewModel.preferencesFlow.first().hideCompleted
        }

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.action_sort_by_name -> {
                viewModel.onSortOrderSelected(SortOrder.BY_NAME)
                true
            }
            R.id.action_sort_by_date_created -> {
                viewModel.onSortOrderSelected(SortOrder.BY_DATE)
                true
            }
            R.id.action_hide_completed_tasks -> {
                item.isChecked = !item.isChecked
                viewModel.onHildeCompletedClick(item.isChecked)
                true
            }
            R.id.action_delete_all_completed_tasks -> {

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onItemClick(task: Task) {
        viewModel.onTaskSelected(task)
    }

    override fun onCheckBoxClick(task: Task, isChecked: Boolean) {
        viewModel.onTaskCheckedChanged(task ,isChecked)
    }

}