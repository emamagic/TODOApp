package com.codinginflow.mvvmtodo.ui.addedittask

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.codinginflow.mvvmtodo.base.BaseFragment
import com.codinginflow.mvvmtodo.databinding.FragmentAddEditTaskBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddEditTaskFragment: BaseFragment<FragmentAddEditTaskBinding>() {

    private val viewModel: AddEditTaskViewModel by viewModels()

    override fun getFragmentBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentAddEditTaskBinding {
        return FragmentAddEditTaskBinding.inflate(inflater ,container ,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            editTextTaskName.setText(viewModel.taskName)
            checkBoxImportant.isChecked = viewModel.taskImportance
            checkBoxImportant.jumpDrawablesToCurrentState()
            textViewDateCreated.isVisible = viewModel.task != null
            textViewDateCreated.text = "Created: ${viewModel.task?.createdDateFormatted}"
        }


    }
}