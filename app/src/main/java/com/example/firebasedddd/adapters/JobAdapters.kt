package com.example.firebasedddd.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.firebasedddd.R
import com.example.firebasedddd.models.Job

class JobAdapter(private var jobs: List<Job>, private val onClick: (Job) -> Unit) :
    RecyclerView.Adapter<JobAdapter.JobViewHolder>() {

    inner class JobViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        private val tvTitle: TextView = item.findViewById(R.id.tvTitle)
        private val tvCompany: TextView = item.findViewById(R.id.tvCompany)
        private val tvLocation: TextView = item.findViewById(R.id.tvLocation)
        fun bind(job: Job) {
            tvTitle.text = job.title
            tvCompany.text = job.company
            tvLocation.text = job.location
            itemView.setOnClickListener { onClick(job) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_job, parent, false)
        return JobViewHolder(v)
    }

    override fun getItemCount(): Int = jobs.size

    override fun onBindViewHolder(holder: JobViewHolder, position: Int) = holder.bind(jobs[position])

    fun updateJobs(newJobs: List<Job>) {
        this.jobs = newJobs
        notifyDataSetChanged()
    }
}
