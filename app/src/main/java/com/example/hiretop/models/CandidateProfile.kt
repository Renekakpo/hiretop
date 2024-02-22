package com.example.hiretop.models

import com.google.firebase.firestore.DocumentId

data class CandidateProfile(
    @DocumentId
    val profileId: String? = null,
    val bannerUrl: String?,
    val pictureUrl: String?,
    val firstname: String?,
    val lastname: String?,
    val name: String = "$firstname $lastname",
    val headline: String?,
    val about: String?,
    val experiences: List<Experience>?,
    val education: List<Education>?,
    val certifications: List<Certification>?,
    val projects: List<Project>?,
    val skills: List<String>?,
    val createdAt: Long?,
    val updatedAt: Long?
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

val mockCandidateProfile = CandidateProfile(
    bannerUrl = "https://example.com/banner.jpg",
    pictureUrl = "https://example.com/profile.jpg",
    firstname = "Jean",
    lastname = "Dupont",
    headline = "Développeur Full Stack",
    about = "Passionné par la programmation et les nouvelles technologies.",
    experiences = listOf(
        Experience(
            title = "Développeur Full Stack",
            employmentType = "Temps plein",
            companyName = "Tech Solutions SARL",
            location = "Paris",
            locationType = "Ville",
            isCurrentWork = true,
            startMonth = "Janvier",
            startYear = "2018",
            endMonth = "",
            endYear = "",
            industry = "Technologie",
            description = "Développement et maintenance d'applications web et mobiles.",
            skills = listOf("Kotlin", "Java", "HTML", "CSS", "JavaScript", "React", "Node.js")
        ),
        Experience(
            title = "Stagiaire Développeur Web",
            employmentType = "Stage",
            companyName = "Startup Innovante SAS",
            location = "Lyon",
            locationType = "Ville",
            isCurrentWork = false,
            startMonth = "Mai",
            startYear = "2017",
            endMonth = "Août",
            endYear = "2017",
            industry = "Technologie",
            description = "Développement d'une application web de gestion des tâches.",
            skills = listOf("HTML", "CSS", "JavaScript", "Angular")
        )
    ),
    education = listOf(
        Education(
            school = "Université Paris-Saclay",
            degree = "Master en Informatique",
            fieldOfStudy = "Ingénierie Logicielle",
            startMont = "Septembre",
            startYear = "2016",
            endMonth = "Juin",
            endYear = "2018",
            activities = "Membre du club d'informatique.",
            description = ""
        ),
        Education(
            school = "Lycée Louis-le-Grand",
            degree = "Baccalauréat Scientifique",
            fieldOfStudy = "",
            startMont = "Septembre",
            startYear = "2013",
            endMonth = "Juin",
            endYear = "2016",
            activities = "",
            description = ""
        )
    ),
    certifications = listOf(
        Certification(
            name = "Certification Kotlin Developer",
            issuingOrganization = "JetBrains",
            issueMonth = "Mars",
            issueYear = "2020",
            expireMonth = "",
            expireYear = "",
            credentialID = "ABC123",
            credentialURL = "https://example.com/certification",
            skills = listOf("Kotlin")
        ),
        Certification(
            name = "Certification React Developer",
            issuingOrganization = "FreeCodeCamp",
            issueMonth = "Février",
            issueYear = "2019",
            expireMonth = "",
            expireYear = "",
            credentialID = "DEF456",
            credentialURL = "https://example.com/certification",
            skills = listOf("React", "JavaScript")
        )
    ),
    projects = listOf(
        Project(
            name = "Application de Gestion de Projet",
            description = "Application web permettant de gérer les projets et les tâches.",
            skills = listOf("Java", "Spring Boot", "React", "MySQL"),
            isCurrentlyWorkingOn = false,
            startMonth = "Janvier",
            startYear = "2020",
            endMonth = "Décembre",
            endYear = "2020"
        ),
        Project(
            name = "Application de Réservation de Vols",
            description = "Application mobile permettant de rechercher et de réserver des vols.",
            skills = listOf("Kotlin", "Android", "Firebase"),
            isCurrentlyWorkingOn = true,
            startMonth = "Mai",
            startYear = "2021",
            endMonth = "",
            endYear = ""
        )
    ),
    skills = listOf("Kotlin", "Java", "HTML", "CSS", "JavaScript", "React", "Node.js", "Angular"),
    createdAt = System.currentTimeMillis(),
    updatedAt = System.currentTimeMillis()
)