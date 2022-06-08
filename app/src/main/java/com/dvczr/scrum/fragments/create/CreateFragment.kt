package com.dvczr.scrum.fragments.create

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import androidx.fragment.app.Fragment
import android.view.animation.DecelerateInterpolator
import android.widget.EditText
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.graphics.ColorUtils
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.dvczr.scrum.R
import com.dvczr.scrum.model.Task
import com.dvczr.scrum.viewmodel.TaskViewModel
import kotlinx.android.synthetic.main.fragment_create.*

class CreateFragment : Fragment() {

    private lateinit var mTaskViewModel: TaskViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_create, container, false)

        mTaskViewModel = ViewModelProvider(this)[TaskViewModel::class.java]

        val title = view.findViewById<EditText>(R.id.et_fCreate_title)
        val body = view.findViewById<EditText>(R.id.et_fCreate_body)

        val containerBackground = view.findViewById<ConstraintLayout>(R.id.cl_fCreate_containerBackground)
        val containerBorder = view.findViewById<CardView>(R.id.cv_fCreate_containerBorder)

        // Fade animation for the background of Popup Window
        val alpha = 5 //between 0-255
        val alphaColor = ColorUtils.setAlphaComponent(Color.parseColor("#6a0dad"), alpha)
        val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), Color.BLACK, alphaColor)

        colorAnimation.duration = 1000 // milliseconds
        colorAnimation.addUpdateListener { animator ->
            containerBackground.setBackgroundColor(animator.animatedValue as Int)
        }
        colorAnimation.start()

        // Fade animation for the Popup Window
        containerBorder.alpha = 0f
        containerBorder
            .animate()
            .alpha(1f)
            .setDuration(250)
            .setInterpolator(DecelerateInterpolator())
            .start()

        // Add menu
        setHasOptionsMenu(true)

        return view
    }

    @SuppressLint("UseRequireInsteadOfGet")
    private fun insertDataToDatabase() {
        val title = et_fCreate_title.text.toString()
        val body = et_fCreate_body.text.toString()
        val cBottomAppBar = activity!!.findViewById<CoordinatorLayout>(R.id.cl_containerBottomAppBar)

        if (inputCheck(title, body)) {
            // Create Task
            val task = Task(0, title, body)
            // Add data to database
            mTaskViewModel.addTask(task)
            Toast.makeText(requireContext(), "Task was SUCCESSFULLY added!", Toast.LENGTH_SHORT).show()
            // Navigate back
            findNavController().navigate(R.id.action_addFragment_to_listFragment).apply {
                cBottomAppBar.visibility = View.VISIBLE
            }
        } else {
            Toast.makeText(requireContext(), "Please fill in all fields!", Toast.LENGTH_SHORT).show()

        }
    }

    private fun inputCheck(title: String, body: String): Boolean{
        return (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(body))
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.top_menu, menu)
        menu.getItem(1).isEnabled = false
        menu.getItem(1).isVisible = false
        menu.getItem(2).isEnabled = false
        menu.getItem(2).isVisible = false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_topSave) {
            insertDataToDatabase()
        }
        return super.onOptionsItemSelected(item)
    }
}