package com.example.firebasedddd

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.firebasedddd.models.Job

class JobDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_job_details)

        val job = intent.getSerializableExtra("job") as? Job
        job?.let {
            findViewById<TextView>(R.id.tvTitle).text = it.title
            findViewById<TextView>(R.id.tvCompany).text = it.company
            findViewById<TextView>(R.id.tvLocation).text = it.location
            findViewById<TextView>(R.id.tvSalary).text = it.salaryRange ?: "N/A"
            findViewById<TextView>(R.id.tvDescription).text = it.description ?: ""
        }
    }
}
