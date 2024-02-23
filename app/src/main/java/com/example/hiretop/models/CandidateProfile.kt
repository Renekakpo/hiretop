package com.example.hiretop.models

import com.google.firebase.firestore.DocumentId
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

data class CandidateProfile(
    @DocumentId
    val profileId: String? = null,
    val bannerUrl: String? = null,
    val pictureUrl: String? = null,
    val firstname: String? = null,
    val lastname: String? = null,
    val name: String = "$firstname $lastname",
    val headline: String? = null,
    val about: String? = null,
    val experiences: Set<Experience>? = null,
    val educations: Set<Education>? = null,
    val certifications: Set<Certification>? = null,
    val projects: Set<Project>? = null,
    val skills: Set<String>? = null,
    val createdAt: Long? = null,
    val updatedAt: Long? = null
)

data class Experience(
    val title: String,
    val employmentType: String? = null,
    val companyName: String,
    val location: String,
    val locationType: String,
    val isCurrentWork: Boolean,
    val startMonth: String,
    val startYear: String,
    val endMonth: String,
    val endYear: String,
    val industry: String,
    val description: String? = null,
    val skills: Set<String>? = null
) {
    private val dateFormatter: SimpleDateFormat = SimpleDateFormat("MMMM yyyy", Locale.FRENCH)

    fun getStartDate(): Date? {
        val dateString = "$startMonth $startYear"
        return dateFormatter.parse(dateString)
    }

    fun getEndDate(): Date? {
        if (isCurrentWork) {
            return null
        }
        val dateString = "$endMonth $endYear"
        return dateFormatter.parse(dateString)
    }
}

data class Education(
    val school: String,
    val degree: String? = null,
    val fieldOfStudy: String? = null,
    val grade: String? = null,
    val startMonth: String? = null,
    val startYear: String? = null,
    val endMonth: String? = null,
    val endYear: String? = null,
    val activities: String? = null,
    val description: String? = null
) {
    private val dateFormatter: SimpleDateFormat = SimpleDateFormat("MMMM yyyy", Locale.FRENCH)

    fun getStartDate(): Date? {
        val dateString = "$startMonth $startYear"
        return dateFormatter.parse(dateString)
    }

    fun getEndDate(): Date? {
        val dateString = "$endMonth $endYear"
        return dateFormatter.parse(dateString)
    }
}
data class Certification(
    val name: String,
    val issuingOrganization: String,
    val issueMonth: String? = null,
    val issueYear: String? = null,
    val expireMonth: String? = null,
    val expireYear: String? = null,
    val credentialID: String? = null,
    val credentialURL: String? = null,
    val skills: Set<String>? = null,
) {
    private val dateFormatter: SimpleDateFormat = SimpleDateFormat("MMMM yyyy", Locale.FRENCH)

    fun getStartDate(): Date? {
        val dateString = "$expireMonth $expireYear"
        return dateFormatter.parse(dateString)
    }
}
data class Project(
    val name: String,
    val description: String? = null,
    val skills: List<String>? = null
)

val mockCandidateProfile = CandidateProfile(
    bannerUrl = "https://example.com/banner.jpg",
    pictureUrl = "https://example.com/profile.jpg",
    firstname = "Jean",
    lastname = "Dupont",
    headline = "Développeur Full Stack",
    about = "Passionné par la programmation et les nouvelles technologies.",
    experiences = setOf(
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
            skills = setOf("Kotlin", "Java", "HTML", "CSS", "JavaScript", "React", "Node.js")
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
            skills = setOf("HTML", "CSS", "JavaScript", "Angular")
        )
    ),
    educations = setOf(
        Education(
            school = "Université Paris-Saclay",
            degree = "Master en Informatique",
            fieldOfStudy = "Ingénierie Logicielle",
            startMonth = "Septembre",
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
            startMonth = "Septembre",
            startYear = "2013",
            endMonth = "Juin",
            endYear = "2016",
            activities = "",
            description = ""
        )
    ),
    certifications = setOf(
        Certification(
            name = "Certification Kotlin Developer",
            issuingOrganization = "JetBrains",
            issueMonth = "Mars",
            issueYear = "2020",
            expireMonth = "",
            expireYear = "",
            credentialID = "ABC123",
            credentialURL = "https://example.com/certification",
            skills = setOf("Kotlin")
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
            skills = setOf("React", "JavaScript")
        )
    ),
    projects = setOf(
        Project(
            name = "Application de Gestion de Projet",
            description = "Application web permettant de gérer les projets et les tâches.",
            skills = listOf("Java", "Spring Boot", "React", "MySQL"),
        ),
        Project(
            name = "Application de Réservation de Vols",
            description = "Application mobile permettant de rechercher et de réserver des vols.",
            skills = listOf("Kotlin", "Android", "Firebase"),
        )
    ),
    skills = setOf("Kotlin", "Java", "HTML", "CSS", "JavaScript", "React", "Node.js", "Angular"),
    createdAt = System.currentTimeMillis(),
    updatedAt = System.currentTimeMillis()
)