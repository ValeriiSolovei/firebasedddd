package com.example.firebasedddd.models

data class Application(
    val applicationId: String? = null,
    val jobId: String? = null,
    val applicantId: String? = null,
    val jobTitle: String? = null, // Store for easy display
    val applicantEmail: String? = null, // Store for easy display
    val applicationDate: Long = System.currentTimeMillis()
)
