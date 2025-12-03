package com.example.firebasedddd

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.firebasedddd.adapters.JobAdapter // This import will now work correctly
import com.example.firebasedddd.models.Application
import com.example.firebasedddd.models.Job
import com.example.firebasedddd.repository.JobRepository
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class HomeActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var jobsRecyclerView: RecyclerView
    private lateinit var tvNoJobs: TextView
    private lateinit var jobAdapter: JobAdapter
    private val jobList = mutableListOf<Job>() // This line is now correctly included

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // --- Setup the Toolbar ---
        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        // -------------------------

        // Initialize Firebase
        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        // Initialize Views
        jobsRecyclerView = findViewById(R.id.rvJobs)
        tvNoJobs = findViewById(R.id.tvNoJobs)
        val fabAddJob = findViewById<FloatingActionButton>(R.id.fabAddJob)

        // The adapter now needs two arguments: the list and the lambda for the apply button
        jobAdapter = JobAdapter(jobList) { job ->
            applyToJob(job) // This function will be called when the "Apply" button is clicked
        }

        jobsRecyclerView.layoutManager = LinearLayoutManager(this)
        jobsRecyclerView.adapter = jobAdapter

        fabAddJob.setOnClickListener {
            startActivity(Intent(this, PostJobActivity::class.java))
        }

        listenForJobs()
    }

    private fun applyToJob(job: Job) {
        val currentUser = auth.currentUser
        if (currentUser == null) {
            Toast.makeText(this, "Please log in to apply.", Toast.LENGTH_SHORT).show()
            return
        }

        // Prevent a user from applying to their own job post
        if (currentUser.uid == job.postedByUserId) {
            Toast.makeText(this, "You cannot apply to your own job.", Toast.LENGTH_SHORT).show()
            return
        }

        val application = Application(
            jobId = job.jobID,
            jobTitle = job.title,
            applicantId = currentUser.uid,
            applicantEmail = currentUser.email
        )

        JobRepository.applyForJob(application) { success, error ->
            if (success) {
                Toast.makeText(this, "Successfully applied for ${job.title}", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Application failed: $error", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun listenForJobs() {
        db.collection("jobs")
            .orderBy("datePosted", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshots, error ->
                if (error != null) {
                    Toast.makeText(this, "Error loading jobs: ${error.message}", Toast.LENGTH_LONG).show()
                    return@addSnapshotListener
                }

                if (snapshots != null && !snapshots.isEmpty) {
                    jobsRecyclerView.visibility = View.VISIBLE
                    tvNoJobs.visibility = View.GONE

                    jobList.clear()
                    jobList.addAll(snapshots.toObjects(Job::class.java))
                    jobAdapter.notifyDataSetChanged()
                } else {
                    jobsRecyclerView.visibility = View.GONE
                    tvNoJobs.visibility = View.VISIBLE
                }
            }
    }

    // --- Functions to handle the menu ---
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.home_activity_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here.
        return when (item.itemId) {
            R.id.menu_my_posted_jobs -> {
                // Navigate to the new MyJobsActivity
                val intent = Intent(this, MyJobsActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
