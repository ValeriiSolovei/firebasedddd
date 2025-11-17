package com.example.firebasedddd.repository

import com.example.firebasedddd.models.Job
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

object JobRepository {
    private val db = FirebaseFirestore.getInstance()
    private val jobsRef = db.collection("jobs")

    fun postJob(job: Job, onComplete: (Boolean, String?) -> Unit) {
        val doc = jobsRef.document()
        val jobWithId = job.copy(jobId = doc.id)
        doc.set(jobWithId)
            .addOnSuccessListener { onComplete(true, null) }
            .addOnFailureListener { e -> onComplete(false, e.localizedMessage) }
    }

    fun fetchJobs(onComplete: (List<Job>?, String?) -> Unit) {
        jobsRef.orderBy("datePosted", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { snap ->
                val jobs = snap.documents.mapNotNull { it.toObject(Job::class.java) }
                onComplete(jobs, null)
            }
            .addOnFailureListener { e -> onComplete(null, e.localizedMessage) }
    }

    fun getJobById(jobId: String, onComplete: (Job?, String?) -> Unit) {
        jobsRef.document(jobId).get()
            .addOnSuccessListener { doc -> onComplete(doc.toObject(Job::class.java), null) }
            .addOnFailureListener { e -> onComplete(null, e.localizedMessage) }
    }
}
