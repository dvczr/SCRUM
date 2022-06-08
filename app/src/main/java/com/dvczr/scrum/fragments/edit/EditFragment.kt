package com.dvczr.scrum.fragments.edit

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.dvczr.scrum.R
import com.dvczr.scrum.model.Task
import com.dvczr.scrum.viewmodel.TaskViewModel
import kotlinx.android.synthetic.main.fragment_edit.*
import kotlinx.android.synthetic.main.fragment_edit.view.*

class EditFragment : Fragment() {

    private val args by navArgs<EditFragmentArgs>()

    private lateinit var mTaskViewModel: TaskViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_edit, container, false)

        mTaskViewModel = ViewModelProvider(this)[TaskViewModel::class.java]

        view.et_fEdit_title.setText(args.currentTask.title)
        view.et_fEdit_body.setText(args.currentTask.body)

        // Add menu
        setHasOptionsMenu(true)

        return view
    }

    @SuppressLint("UseRequireInsteadOfGet")
    private fun editTask(){
        val title = et_fEdit_title.text.toString()
        val body = et_fEdit_body.text.toString()
        val cBottomAppBar = activity!!.findViewById<CoordinatorLayout>(R.id.cl_containerBottomAppBar)
        if (args.currentTask.title != et_fEdit_title.text.toString() || args.currentTask.body !=  et_fEdit_body.text.toString()) {
            if (inputCheck(title, body)){
                // Create Task Object
                val editTask = Task(args.currentTask.id, title, body)
                mTaskViewModel.editTask(editTask)
                Toast.makeText(requireContext(), "Task SUCCESSFULLY edited", Toast.LENGTH_SHORT).show()
                // Navigate back
                findNavController().navigate(R.id.action_editFragment_to_listFragment)
                cBottomAppBar.visibility = View.VISIBLE
            } else {
                Toast.makeText(requireContext(), "Please fill out all fields!", Toast.LENGTH_SHORT).show()
            }
        } else {
            // Navigate back
            findNavController().navigate(R.id.action_editFragment_to_listFragment)
            cBottomAppBar.visibility = View.VISIBLE
        }
    }

    private fun inputCheck(title: String, body: String): Boolean{
        return (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(body))
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.top_menu, menu)
        menu.getItem(2).isEnabled = false
        menu.getItem(2).isVisible = false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_topDelete) {
            deleteTask()
        }
        if (item.itemId == R.id.menu_topSave) {
            editTask()
        }
        return super.onOptionsItemSelected(item)
    }

    @SuppressLint("UseRequireInsteadOfGet")
    private fun deleteTask() {
        val cBottomAppBar = activity!!.findViewById<CoordinatorLayout>(R.id.cl_containerBottomAppBar)
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes") {_, _ ->
            mTaskViewModel.deleteTask(args.currentTask)
            Toast.makeText(requireContext(), "Successfully deleted task id#: ${args.currentTask.id}!", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_editFragment_to_listFragment)
            cBottomAppBar.visibility = View.VISIBLE
        }
        builder.setNegativeButton("No") {_, _-> }
        builder.setTitle("Delete ${args.currentTask.title}?")
        builder.setMessage("Are you sure you want to DELETE task #${args.currentTask.id}?")
        builder.create().show()
    }
}