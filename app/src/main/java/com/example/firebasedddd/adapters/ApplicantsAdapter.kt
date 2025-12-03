package com.example.firebasedddd.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.firebasedddd.models.Application

class ApplicantsAdapter(private val applicants: List<Application>) :
    RecyclerView.Adapter<ApplicantsAdapter.ApplicantViewHolder>() {

    class ApplicantViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val emailTextView: TextView = itemView.findViewById(android.R.id.text1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ApplicantViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_1, parent, false)
        return ApplicantViewHolder(view)
    }

    override fun onBindViewHolder(holder: ApplicantViewHolder, position: Int) {
        holder.emailTextView.text = applicants[position].applicantEmail
    }

    override fun getItemCount() = applicants.size
}
