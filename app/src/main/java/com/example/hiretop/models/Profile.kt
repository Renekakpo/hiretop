package com.example.hiretop.models

data class Profile(
    val bannerUrl: String,
    val profilePictureUrl: String,
    val firstname: String,
    val lastname: String,
    val headline: String,
    val about: String,
    val experiences: List<Experience>,
    val education: List<Education>,
    val certifications: List<Certification>,
    val projects: List<Project>,
    val skills: List<String>,
    val createdAt: String,
    val updatedAt: String
)

data class Experience(
    val title: String,
    val employmentType: String,
    val companyName: String,
    val location: String,
    val locationType: String,
    val isCurrentWork: Boolean,
    val startMonth: String,
    val startYear: String,
    val endMonth: String,
    val endYear: String,
    val industry: String,
    val description: String,
    val profileHeadline: String,
    val skills: List<String>
)
data class Education(
    val school: String,
    val degree: String,
    val fieldOfStudy: String,
    val startMont: String,
    val startYear: String,
    val endMonth: String,
    val endYear: String,
    val activities: String,
    val description: String
)
data class Certification(
    val name: String,
    val issuingOrganization: String,
    val issueMonth: String,
    val issueYear: String,
    val expireMonth: String,
    val expireYear: String,
    val credentialID: String,
    val credentialURL: String,
    val skills: List<String>,
)
data class Project(
    val name: String,
    val description: String,
    val skills: List<String>,
    val isCurrentlyWorkingOn: Boolean,
    val startMonth: String,
    val startYear: String,
    val endMonth: String,
    val endYear: String,
)