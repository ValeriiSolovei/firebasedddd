package com.example.firebasedddd.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.firebasedddd.R
import com.example.firebasedddd.models.Job

class JobAdapter(
    private val jobList: List<Job>,
    private val onApplyClicked: (Job) -> Unit
) : RecyclerView.Adapter<JobAdapter.JobViewHolder>() {

    class JobViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // --- FIX THE IDS IN THESE LINES ---
        val titleTextView: TextView = itemView.findViewById(R.id.tvJobTitle) // Use tvJobTitle
        val companyTextView: TextView = itemView.findViewById(R.id.tvCompanyName) // Use tvCompanyName
        val locationTextView: TextView = itemView.findViewById(R.id.tvLocation)
        val applyButton: Button = itemView.findViewById(R.id.btnApply)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobViewHolder {
        // Make sure this is inflating the correct layout file name
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_job_post, parent, false) // Ensure filename is item_job_post.xml
        return JobViewHolder(view)
    }

    override fun onBindViewHolder(holder: JobViewHolder, position: Int) {
        val currentJob = jobList[position]
        holder.titleTextView.text = currentJob.title
        holder.companyTextView.text = currentJob.company
        holder.locationTextView.text = currentJob.location

        holder.applyButton.setOnClickListener {
            onApplyClicked(currentJob)
        }
    }

    override fun getItemCount() = jobList.size
}

