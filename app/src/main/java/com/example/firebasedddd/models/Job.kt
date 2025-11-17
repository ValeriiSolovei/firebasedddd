package com.example.firebasedddd.models

import java.io.Serializable

data class Job(
    val jobId: String = "",
    val title: String = "",
    val company: String = "",
    val location: String = "",
    val salaryRange: String? = null,
    val category: String? = null,
    val description: String? = null,
    val postedByUserId: String? = null,
    val datePosted: Long = System.currentTimeMillis()
) : Serializable