package com.example.firebasedddd

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.firebasedddd.models.Job
import com.example.firebasedddd.repository.JobRepository

class PostJobActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_job)

        val etTitle = findViewById<EditText>(R.id.etTitle)
        val etCompany = findViewById<EditText>(R.id.etCompany)
        val etLocation = findViewById<EditText>(R.id.etLocation)
        val etSalary = findViewById<EditText>(R.id.etSalary)
        val etDescription = findViewById<EditText>(R.id.etDescription)
        val btnSubmit = findViewById<Button>(R.id.btnSubmit)

        btnSubmit.setOnClickListener {
            val title = etTitle.text.toString().trim()
            val company = etCompany.text.toString().trim()
            val location = etLocation.text.toString().trim()
            val salary = etSalary.text.toString().trim()
            val desc = etDescription.text.toString().trim()
            if (title.isEmpty() || company.isEmpty() || location.isEmpty()) {
                Toast.makeText(this, "Title, company and location required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val job = Job(
                title = title,
                company = company,
                location = location,
                salaryRange = if (salary.isEmpty()) null else salary,
                description = desc,
                postedByUserId = AuthUtils.getCurrentUserId()
            )

            JobRepository.postJob(job) { success, error ->
                if (success) {
                    Toast.makeText(this, "Job posted", Toast.LENGTH_SHORT).show()
                    finish()
                } else Toast.makeText(this, "Error posting job: $error", Toast.LENGTH_LONG).show()
            }
        }
    }
}
