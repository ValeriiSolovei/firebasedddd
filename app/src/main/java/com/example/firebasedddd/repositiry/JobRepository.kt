package com.example.firebasedddd.repository

import com.example.firebasedddd.models.Application
import com.example.firebasedddd.models.Job
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

object JobRepository {

    private val db = FirebaseFirestore.getInstance()
    private val jobsCollection = db.collection("jobs")
    // Add a new collection for applications
    private val applicationsCollection = db.collection("applications")

    fun postJob(job: Job, onComplete: (Boolean, String?) -> Unit) {
        jobsCollection.document(job.jobID!!)
            .set(job)
            .addOnSuccessListener { onComplete(true, null) }
            .addOnFailureListener { e -> onComplete(false, e.localizedMessage) }
    }

    fun fetchJobs(onComplete: (List<Job>?, String?) -> Unit) {
        jobsCollection
            .orderBy("datePosted", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { result ->
                onComplete(result?.toObjects(Job::class.java), null)
            }
            .addOnFailureListener { e -> onComplete(null, e.localizedMessage) }
    }

    // New function to apply for a job
    fun applyForJob(application: Application, onComplete: (Boolean, String?) -> Unit) {
        // Use a combination of jobId and applicantId as the document ID to prevent duplicate applications
        val documentId = "${application.jobId}_${application.applicantId}"
        applicationsCollection.document(documentId)
            .set(application)
            .addOnSuccessListener { onComplete(true, null) }
            .addOnFailureListener { e -> onComplete(false, e.localizedMessage) }
    }

    fun fetchApplicantsForJob(jobId: String, onComplete: (List<Application>?, String?) -> Unit) {
        applicationsCollection
            .whereEqualTo("jobId", jobId)
            .orderBy("applicationDate", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { result ->
                onComplete(result?.toObjects(Application::class.java), null)
            }
            .addOnFailureListener { e ->
                onComplete(null, e.localizedMessage)
            }
    }
}
