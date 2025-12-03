package com.example.firebasedddd

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.firebasedddd.adapters.ApplicantsAdapter
import com.example.firebasedddd.models.Application
import com.example.firebasedddd.repository.JobRepository

class ApplicantsActivity : AppCompatActivity() {

    private lateinit var applicantsRecyclerView: RecyclerView
    private lateinit var tvNoApplicants: TextView
    private lateinit var applicantsAdapter: ApplicantsAdapter
    private val applicantsList = mutableListOf<Application>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_applicants)

        val jobId = intent.getStringExtra("JOB_ID")
        val jobTitle = intent.getStringExtra("JOB_TITLE")

        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Applicants for: $jobTitle"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            finish()
        }

        applicantsRecyclerView = findViewById(R.id.rvApplicants)
        tvNoApplicants = findViewById(R.id.tvNoApplicants)

        applicantsAdapter = ApplicantsAdapter(applicantsList)
        applicantsRecyclerView.adapter = applicantsAdapter

        if (jobId == null) {
            Toast.makeText(this, "Job ID not found.", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        loadApplicants(jobId)
    }

    private fun loadApplicants(jobId: String) {
        JobRepository.fetchApplicantsForJob(jobId) { applicants, error ->
            if (error != null) {
                Toast.makeText(this, "Error: $error", Toast.LENGTH_LONG).show()
                return@fetchApplicantsForJob
            }

            if (applicants != null && applicants.isNotEmpty()) {
                applicantsRecyclerView.visibility = View.VISIBLE
                tvNoApplicants.visibility = View.GONE
                applicantsList.clear()
                applicantsList.addAll(applicants)
                applicantsAdapter.notifyDataSetChanged()
            } else {
                applicantsRecyclerView.visibility = View.GONE
                tvNoApplicants.visibility = View.VISIBLE
            }
        }
    }
}
