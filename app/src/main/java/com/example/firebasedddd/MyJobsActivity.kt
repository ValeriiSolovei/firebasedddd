package com.example.firebasedddd

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.firebasedddd.adapters.JobAdapter
import com.example.firebasedddd.models.Job
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MyJobsActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    private lateinit var rvMyJobs: RecyclerView
    private lateinit var tvNoMyJobs: TextView
    private lateinit var myJobsAdapter: JobAdapter
    private val myJobsList = mutableListOf<Job>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_jobs)

        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "My Posted Jobs"
        toolbar.setNavigationOnClickListener {
            finish()
        }

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        rvMyJobs = findViewById(R.id.rvMyJobs)
        // Ensure this ID 'tvNoJobs' matches the one in your 'activity_my_jobs.xml' file.
        tvNoMyJobs = findViewById(R.id.tvNoJobs)

        // Initialize the correct JobAdapter. It takes the list and a lambda for clicks.
        myJobsAdapter = JobAdapter(myJobsList) { job ->
            val intent = Intent(this, ApplicantsActivity::class.java)
            // FIX: Removed the typo 'w' before 'jobID'
            intent.putExtra("JOB_ID", job.jobID)
            intent.putExtra("JOB_TITLE", job.title)
            startActivity(intent)
        }

        rvMyJobs.layoutManager = LinearLayoutManager(this)
        rvMyJobs.adapter = myJobsAdapter

        loadMyJobs()
    }

    private fun loadMyJobs() {
        val currentUser = auth.currentUser
        if (currentUser == null) {
            Toast.makeText(this, "You must be logged in.", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Use a 'where' clause to filter by the current user's ID.
        db.collection("jobs")
            .whereEqualTo("postedByUserId", currentUser.uid)
            .get()
            .addOnSuccessListener { querySnapshot ->
                // Handle the success case by parsing the documents.
                if (!querySnapshot.isEmpty) {
                    myJobsList.clear()
                    val jobs = querySnapshot.toObjects(Job::class.java)
                    myJobsList.addAll(jobs)
                    myJobsAdapter.notifyDataSetChanged()

                    rvMyJobs.visibility = View.VISIBLE
                    tvNoMyJobs.visibility = View.GONE
                } else {
                    rvMyJobs.visibility = View.GONE
                    tvNoMyJobs.visibility = View.VISIBLE
                }
            }
            .addOnFailureListener { e ->
                // Handle the failure case.
                Toast.makeText(this, "Error loading jobs: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
                rvMyJobs.visibility = View.GONE
                tvNoMyJobs.visibility = View.VISIBLE
            }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}
