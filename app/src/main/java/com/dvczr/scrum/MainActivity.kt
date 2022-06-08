package com.dvczr.scrum

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.dvczr.scrum.model.Task
import com.dvczr.scrum.viewmodel.TaskViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var mTaskViewModel: TaskViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mTaskViewModel = ViewModelProvider(this)[TaskViewModel::class.java]

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val navController = this.findNavController(R.id.f_main)
        val appBarConfiguration = AppBarConfiguration(setOf(R.id.listFragment,R.id.aboutFragment))
        setupActionBarWithNavController(navController, appBarConfiguration)
        bottomNavigationView.setupWithNavController(navController)
        bottomNavigationView.background = null
        bottomNavigationView.menu.getItem(1).isEnabled = false

        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            navController.navigate(R.id.action_listFragment_to_addFragment)
            val cBottomAppBar = findViewById<CoordinatorLayout>(R.id.cl_containerBottomAppBar)
            cBottomAppBar.visibility = View.GONE
        }
        fab.setOnLongClickListener {
            generateTasks()
            return@setOnLongClickListener true
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.f_main).apply {
            val cBottomAppBar = findViewById<CoordinatorLayout>(R.id.cl_containerBottomAppBar)
            cBottomAppBar.visibility = View.VISIBLE
        }
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    private fun generateTasks() {
        val taskList = arrayListOf(
            Task(0,"Aenean ex.", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Suspendisse dapibus dui vitae velit aenean."),
            Task(0,"Morbi quam leo, sagittis.", "Lorem ipsum dolor sit at."),
            Task(0,"Ut congue metus justo.", "Lorem non."),
            Task(0,"Nullam scelerisque.", "Maecenas aliquet dictum vehicula. Nunc id nisi molestie, rhoncus nunc odio."),
            Task(0,"Vivamus purus justo, pretium.", "Morbi semper volutpat est dui."),
            Task(0,"Sed iaculis.", "Suspendisse potenti. Sed ullamcorper nunc erat integer."),
            Task(0,"Donec laoreet est.", "Maecenas augue."),
            Task(0,"Donec nulla elit, ultricies quis.", "Vestibulum ultricies dolor nec leo fringilla, scelerisque suscipit ante vehicula molestie."),
            Task(0,"Fusce eu nulla maximus.", "Fusce tristique tincidunt nisi, vitae ultrices nunc congue ac. Aenean eget lectus sit amet lectus rhoncus hendrerit accumsan."),
            Task(0,"Curabitur convallis pellentesque.", "In at quam aliquet integer.")
        )
        for (tasks in taskList) {
            mTaskViewModel.addTask(tasks)
        }
    }
}