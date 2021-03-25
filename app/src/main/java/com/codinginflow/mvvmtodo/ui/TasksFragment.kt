package com.codinginflow.mvvmtodo.ui

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.codinginflow.mvvmtodo.R
import com.codinginflow.mvvmtodo.base.BaseFragment
import com.codinginflow.mvvmtodo.databinding.FragmentTasksBinding
import com.codinginflow.mvvmtodo.safe.ResultWrapper
import com.codinginflow.mvvmtodo.util.Constants.MODE_TOAST_ERROR
import com.codinginflow.mvvmtodo.util.onQueryTextListener
import com.codinginflow.mvvmtodo.util.toasty
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TasksFragment: BaseFragment<FragmentTasksBinding>() {

    private val viewModel: TasksViewModel by viewModels()

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentTasksBinding.inflate(inflater ,container ,false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val taskAdapter = TasksAdapter()

        binding?.apply {
            recyclerViewTasks.apply {
                adapter = taskAdapter
                setHasFixedSize(true)
            }
        }

        viewModel.tasks.observe(viewLifecycleOwner) { result ->
            when(result){
                is ResultWrapper.Success -> taskAdapter.submitList(result.data)
                is ResultWrapper.Error -> toasty("there is a problem when getting data from database" ,MODE_TOAST_ERROR)
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

}