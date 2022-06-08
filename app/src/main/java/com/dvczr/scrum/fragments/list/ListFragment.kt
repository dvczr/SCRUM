package com.dvczr.scrum.fragments.list

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dvczr.scrum.R
import com.dvczr.scrum.viewmodel.TaskViewModel
import kotlinx.android.synthetic.main.fragment_list.view.*

class ListFragment : Fragment(), SearchView.OnQueryTextListener {

    private lateinit var mTaskViewModel: TaskViewModel
    private val listAdapter: ListAdapter by lazy { ListAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_list, container, false)

        // RecyclerView
        val adapter = ListAdapter()
        val recyclerView = view.rv_fList
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // TaskViewModel
        mTaskViewModel = ViewModelProvider(this)[TaskViewModel::class.java]
        mTaskViewModel.readAllData.observe(viewLifecycleOwner, Observer { task ->
            adapter.setData(task)
        })

        val swipeGesture = object : SwipeGesture(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                when(direction) {
                    ItemTouchHelper.LEFT -> {
                        mTaskViewModel.deleteTask(adapter.getTask(viewHolder.bindingAdapterPosition))
                        Toast.makeText(requireContext(), "Successfully deleted task!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        val touchHelper = ItemTouchHelper(swipeGesture)
        touchHelper.attachToRecyclerView(recyclerView)

        val searchView = view.sv_fList
        searchView.setOnQueryTextListener(this@ListFragment)

        // Add menu
        setHasOptionsMenu(true)

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.top_menu, menu)
        menu.getItem(0).isEnabled = false
        menu.getItem(0).isVisible = false
        menu.getItem(1).isEnabled = false
        menu.getItem(1).isVisible = false
        val searchView: SearchView = requireView().findViewById(R.id.sv_fList)
        mTaskViewModel.readAllData.observe(viewLifecycleOwner, Observer { task ->
            if (task.isNotEmpty()) {
                menu.getItem(2).isEnabled = true
                menu.getItem(2).isVisible = true
                searchView.visibility = View.VISIBLE
            } else {
                menu.getItem(2).isEnabled = false
                menu.getItem(2).isVisible = false
                searchView.visibility = View.GONE

            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_topDeleteAll) {
            deleteAllTasks()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return true
    }
    override fun onQueryTextChange(query: String?): Boolean {
        if (query != null) {
            searchDatabase(query)
        }
        return true
    }

    @SuppressLint("UseRequireInsteadOfGet")
    private fun searchDatabase(query: String) {
        val recyclerView: RecyclerView = requireView().rv_fList
        val searchQuery = "%$query%"
        recyclerView.adapter = this.listAdapter
        mTaskViewModel.searchDatabase(searchQuery).observe(this) { task ->
            task.let {
                listAdapter.setData(it)
            }
        }
    }

    private fun deleteAllTasks() {
            val builder = AlertDialog.Builder(requireContext())
            builder.setPositiveButton("Yes") {_, _ ->
                mTaskViewModel.deleteAllTasks()
                Toast.makeText(requireContext(), "Successfully deleted all task's", Toast.LENGTH_SHORT).show()
            }
            builder.setNegativeButton("No") {_, _-> }
            builder.setTitle("Delete all tasks?")
            builder.setMessage("Are you sure you want to DELETE all task's?")
            builder.create().show()
    }
}