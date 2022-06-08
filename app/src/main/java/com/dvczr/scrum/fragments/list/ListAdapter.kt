package com.dvczr.scrum.fragments.list

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.dvczr.scrum.R
import com.dvczr.scrum.model.Task
import kotlinx.android.synthetic.main.custom_task.view.*

class ListAdapter: RecyclerView.Adapter<ListAdapter.MyViewHolder>() {

    private var taskList = emptyList<Task>()

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.custom_task, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentTask = taskList[position]
        holder.itemView.et_customTask_title.text = currentTask.title
        holder.itemView.et_customTask_body.text = currentTask.body

        holder.itemView.rootView.setOnLongClickListener {
            val action = ListFragmentDirections.actionListFragmentToEditFragment(currentTask)
            holder.itemView.findNavController().navigate(action).apply {
                val cBottomAppBar: CoordinatorLayout =
                    holder.itemView.rootView.findViewById(R.id.cl_containerBottomAppBar)
                cBottomAppBar.visibility = View.GONE
            }
            return@setOnLongClickListener true
        }
    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(task: List<Task>) {
        this.taskList = task
        notifyDataSetChanged()
    }

    fun getTask(position: Int): Task {
        return taskList[position]
    }
}