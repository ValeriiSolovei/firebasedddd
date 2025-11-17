package com.example.firebasedddd

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.firebasedddd.adapters.JobAdapter
import com.example.firebasedddd.models.Job
import com.example.firebasedddd.repository.JobRepository
import com.google.android.material.floatingactionbutton.FloatingActionButton

class HomeActivity : AppCompatActivity() {
    private lateinit var adapter: JobAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val rvJobs = findViewById<RecyclerView>(R.id.rvJobs)
        rvJobs.layoutManager = LinearLayoutManager(this)
        adapter = JobAdapter(listOf()) { job -> openDetails(job) }
        rvJobs.adapter = adapter

        findViewById<FloatingActionButton>(R.id.fabPost).setOnClickListener {
            startActivity(Intent(this, PostJobActivity::class.java))
        }

        loadJobs()
    }

    private fun loadJobs() {
        JobRepository.fetchJobs { jobs, error ->
            if (jobs != null) adapter.updateJobs(jobs)
            else Toast.makeText(this, "Error loading jobs: $error", Toast.LENGTH_LONG).show()
        }
    }

    private fun openDetails(job: Job) {
        val i = Intent(this, JobDetailsActivity::class.java)
        i.putExtra("job", job)
        startActivity(i)
    }
}
