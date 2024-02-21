package com.example.hiretop.models

data class JobApplication(
    val candidateID: String = "",
    val jobID: String = "",
    val jobTitle: String,
    val companyName: String,
    val location: String,
    val locationType: String,
    val status: String,
    val stages: String,
    val appliedAt: Long,
    val interviewDate: Long? = null,
    val offerReceived: Boolean = false
)

val jobApplicationsList = listOf(
    JobApplication(
        jobTitle = "Ingénieur Logiciel",
        companyName = "Tech Corp",
        location = "San Francisco",
        locationType = "À distance",
        status = "En attente",
        stages = "Entretien",
        appliedAt = System.currentTimeMillis() - 86400000, // Il y a 1 jour
        interviewDate = System.currentTimeMillis() - 172800000,
        offerReceived = true
    ),
    JobApplication(
        jobTitle = "Analyste de Données",
        companyName = "Data Insights Inc.",
        location = "New York",
        locationType = "Sur site",
        status = "En cours",
        stages = "Test Technique",
        appliedAt = System.currentTimeMillis() - 172800000 // Il y a 2 jours
    ),
    JobApplication(
        jobTitle = "Chef de Produit",
        companyName = "Product Innovations",
        location = "Seattle",
        locationType = "À distance",
        status = "Approuvé",
        stages = "Entretien",
        appliedAt = System.currentTimeMillis() - 259200000, // Il y a 3 jours
        interviewDate = System.currentTimeMillis() - 172800000,
        offerReceived = true
    ),
    JobApplication(
        jobTitle = "Designer UI/UX",
        companyName = "Design Studios",
        location = "Los Angeles",
        locationType = "Sur site",
        status = "Refusé",
        stages = "Terminer",
        appliedAt = System.currentTimeMillis() - 345600000 // Il y a 4 jours
    ),
    JobApplication(
        jobTitle = "Spécialiste en Marketing",
        companyName = "Marketing Masters",
        location = "Chicago",
        locationType = "À distance",
        status = "En attente",
        stages = "Entretien",
        appliedAt = System.currentTimeMillis() - 432000000 // Il y a 5 jours
    ),
    JobApplication(
        jobTitle = "Ingénieur Logiciel",
        companyName = "Tech Corp",
        location = "San Francisco",
        locationType = "À distance",
        status = "En attente",
        stages = "Entretien",
        appliedAt = System.currentTimeMillis() - 518400000, // Il y a 6 jours
        interviewDate = System.currentTimeMillis() - 172800000,
        offerReceived = true
    ),
    JobApplication(
        jobTitle = "Analyste de Données",
        companyName = "Data Insights Inc.",
        location = "New York",
        locationType = "Sur site",
        status = "En cours",
        stages = "Test Technique",
        appliedAt = System.currentTimeMillis() - 604800000 // Il y a 7 jours
    ),
    JobApplication(
        jobTitle = "Chef de Produit",
        companyName = "Product Innovations",
        location = "Seattle",
        locationType = "À distance",
        status = "Approuvé",
        stages = "Entretien RH",
        appliedAt = System.currentTimeMillis() - 691200000 // Il y a 8 jours
    ),
    JobApplication(
        jobTitle = "Designer UI/UX",
        companyName = "Design Studios",
        location = "Los Angeles",
        locationType = "Sur site",
        status = "Refusé",
        stages = "Terminer",
        appliedAt = System.currentTimeMillis() - 777600000, // Il y a 9 jours
        interviewDate = System.currentTimeMillis() - 172800000,
        offerReceived = true
    ),
    JobApplication(
        jobTitle = "Spécialiste en Marketing",
        companyName = "Marketing Masters",
        location = "Chicago",
        locationType = "À distance",
        status = "En attente",
        stages = "Entretien",
        appliedAt = System.currentTimeMillis() - 864000000 // Il y a 10 jours
    ),
    JobApplication(
        jobTitle = "Ingénieur Logiciel",
        companyName = "Tech Corp",
        location = "San Francisco",
        locationType = "À distance",
        status = "En attente",
        stages = "Entretien",
        appliedAt = System.currentTimeMillis() - 950400000 // Il y a 11 jours
    ),
    JobApplication(
        jobTitle = "Analyste de Données",
        companyName = "Data Insights Inc.",
        location = "New York",
        locationType = "Sur site",
        status = "En cours",
        stages = "Test d'évaluation",
        appliedAt = System.currentTimeMillis() - 1036800000 // Il y a 12 jours
    ),
    JobApplication(
        jobTitle = "Chef de Produit",
        companyName = "Product Innovations",
        location = "Seattle",
        locationType = "À distance",
        status = "Approuvé",
        stages = "Terimner",
        appliedAt = System.currentTimeMillis() - 1123200000, // Il y a 13 jours
        interviewDate = System.currentTimeMillis() - 172800000,
        offerReceived = true
    ),
    JobApplication(
        jobTitle = "Designer UI/UX",
        companyName = "Design Studios",
        location = "Los Angeles",
        locationType = "Sur site",
        status = "Refusé",
        stages = "Terminer",
        appliedAt = System.currentTimeMillis() - 1209600000 // Il y a 14 jours
    ),
    JobApplication(
        jobTitle = "Spécialiste en Marketing",
        companyName = "Marketing Masters",
        location = "Chicago",
        locationType = "À distance",
        status = "En attente",
        stages = "Entretien",
        appliedAt = System.currentTimeMillis() - 1296000000 // Il y a 15 jours
    )
)
