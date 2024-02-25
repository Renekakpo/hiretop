package com.example.hiretop.models

import com.google.firebase.firestore.DocumentId

data class JobApplication(
    @DocumentId
    val jobApplicationId: String? = null,
    val candidateProfileId: String,
    val enterpriseProfileId: String,
    val jobOfferId: String,
    val jobOfferTitle: String,
    val candidateFullName: String,
    val candidatePictureUrl: String? = null,
    val companyName: String,
    val location: String,
    val locationType: String,
    val status: String?,
    val stages: String?,
    val appliedAt: Long,
    val interviewDate: Long? = null,
    val offerReceived: Boolean = false,
    val offerContent: String? = null,
    val withdraw: Boolean = false,
)

val jobApplicationsLists = listOf(
    JobApplication(
        jobOfferTitle = "Ingénieur Logiciel",
        candidateProfileId = "profile_id",
        enterpriseProfileId = "enterprise_profile_id",
        jobOfferId = "job_offer_id",
        companyName = "Tech Corp",
        candidateFullName = "Techno",
        location = "San Francisco",
        locationType = "À distance",
        status = "En attente",
        stages = "Entretien",
        appliedAt = System.currentTimeMillis() - 86400000, // Il y a 1 jour
        interviewDate = System.currentTimeMillis() - 172800000,
        offerReceived = true,
        offerContent = "Contenu de l'offre reçu du recruiteur..."
    ),
    JobApplication(
        jobOfferTitle = "Analyste de Données",
        candidateProfileId = "profile_id",
        enterpriseProfileId = "enterprise_profile_id",
        jobOfferId = "job_offer_id",
        companyName = "Data Insights Inc.",
        candidateFullName = "Techno",
        location = "New York",
        locationType = "Sur site",
        status = "En cours",
        stages = "Test Technique",
        appliedAt = System.currentTimeMillis() - 172800000 // Il y a 2 jours
    ),
    JobApplication(
        jobOfferTitle = "Chef de Produit",
        candidateProfileId = "profile_id",
        enterpriseProfileId = "enterprise_profile_id",
        jobOfferId = "job_offer_id",
        companyName = "Product Innovations",
        candidateFullName = "Techno",
        location = "Seattle",
        locationType = "À distance",
        status = "Approuvé",
        stages = "Entretien",
        appliedAt = System.currentTimeMillis() - 259200000, // Il y a 3 jours
        interviewDate = System.currentTimeMillis() - 172800000,
        offerReceived = true
    ),
    JobApplication(
        jobOfferTitle = "Designer UI/UX",
        candidateProfileId = "profile_id",
        enterpriseProfileId = "enterprise_profile_id",
        jobOfferId = "job_offer_id",
        companyName = "Design Studios",
        candidateFullName = "Techno",
        location = "Los Angeles",
        locationType = "Sur site",
        status = "Refusé",
        stages = "Terminer",
        appliedAt = System.currentTimeMillis() - 345600000 // Il y a 4 jours
    ),
    JobApplication(
        jobOfferTitle = "Spécialiste en Marketing",
        candidateProfileId = "profile_id",
        enterpriseProfileId = "enterprise_profile_id",
        jobOfferId = "job_offer_id",
        companyName = "Marketing Masters",
        candidateFullName = "Techno",
        location = "Chicago",
        locationType = "À distance",
        status = "En attente",
        stages = "Entretien",
        appliedAt = System.currentTimeMillis() - 432000000 // Il y a 5 jours
    ),
    JobApplication(
        jobOfferTitle = "Ingénieur Logiciel",
        candidateProfileId = "profile_id",
        enterpriseProfileId = "enterprise_profile_id",
        jobOfferId = "job_offer_id",
        companyName = "Tech Corp",
        candidateFullName = "Techno",
        location = "San Francisco",
        locationType = "À distance",
        status = "En attente",
        stages = "Entretien",
        appliedAt = System.currentTimeMillis() - 518400000, // Il y a 6 jours
        interviewDate = System.currentTimeMillis() - 172800000,
        offerReceived = true
    ),
    JobApplication(
        jobOfferTitle = "Analyste de Données",
        candidateProfileId = "profile_id",
        enterpriseProfileId = "enterprise_profile_id",
        jobOfferId = "job_offer_id",
        companyName = "Data Insights Inc.",
        candidateFullName = "Techno",
        location = "New York",
        locationType = "Sur site",
        status = "En cours",
        stages = "Test Technique",
        appliedAt = System.currentTimeMillis() - 604800000 // Il y a 7 jours
    ),
    JobApplication(
        jobOfferTitle = "Chef de Produit",
        companyName = "Product Innovations",
        candidateFullName = "Techno",
        enterpriseProfileId = "enterprise_profile_id",
        candidateProfileId = "profile_id",
        jobOfferId = "job_offer_id",
        location = "Seattle",
        locationType = "À distance",
        status = "Approuvé",
        stages = "Entretien RH",
        appliedAt = System.currentTimeMillis() - 691200000 // Il y a 8 jours
    ),
    JobApplication(
        jobOfferTitle = "Designer UI/UX",
        candidateProfileId = "profile_id",
        enterpriseProfileId = "enterprise_profile_id",
        jobOfferId = "job_offer_id",
        companyName = "Design Studios",
        candidateFullName = "Techno",
        location = "Los Angeles",
        locationType = "Sur site",
        status = "Refusé",
        stages = "Terminer",
        appliedAt = System.currentTimeMillis() - 777600000, // Il y a 9 jours
        interviewDate = System.currentTimeMillis() - 172800000,
        offerReceived = true
    ),
    JobApplication(
        jobOfferTitle = "Spécialiste en Marketing",
        candidateProfileId = "profile_id",
        enterpriseProfileId = "enterprise_profile_id",
        jobOfferId = "job_offer_id",
        companyName = "Marketing Masters",
        candidateFullName = "Techno",
        location = "Chicago",
        locationType = "À distance",
        status = "En attente",
        stages = "Entretien",
        appliedAt = System.currentTimeMillis() - 864000000 // Il y a 10 jours
    ),
    JobApplication(
        jobOfferTitle = "Ingénieur Logiciel",
        candidateProfileId = "profile_id",
        enterpriseProfileId = "enterprise_profile_id",
        jobOfferId = "job_offer_id",
        companyName = "Tech Corp",
        candidateFullName = "Techno",
        location = "San Francisco",
        locationType = "À distance",
        status = "En attente",
        stages = "Entretien",
        appliedAt = System.currentTimeMillis() - 950400000 // Il y a 11 jours
    ),
    JobApplication(
        jobOfferTitle = "Analyste de Données",
        candidateProfileId = "profile_id",
        enterpriseProfileId = "enterprise_profile_id",
        jobOfferId = "job_offer_id",
        companyName = "Data Insights Inc.",
        candidateFullName = "Techno",
        location = "New York",
        locationType = "Sur site",
        status = "En cours",
        stages = "Test d'évaluation",
        appliedAt = System.currentTimeMillis() - 1036800000 // Il y a 12 jours
    ),
    JobApplication(
        jobOfferTitle = "Chef de Produit",
        candidateProfileId = "profile_id",
        enterpriseProfileId = "enterprise_profile_id",
        jobOfferId = "job_offer_id",
        companyName = "Product Innovations",
        candidateFullName = "Techno",
        location = "Seattle",
        locationType = "À distance",
        status = "Approuvé",
        stages = "Terimner",
        appliedAt = System.currentTimeMillis() - 1123200000, // Il y a 13 jours
        interviewDate = System.currentTimeMillis() - 172800000,
        offerReceived = true
    ),
    JobApplication(
        jobOfferTitle = "Designer UI/UX",
        candidateProfileId = "profile_id",
        enterpriseProfileId = "enterprise_profile_id",
        jobOfferId = "job_offer_id",
        companyName = "Design Studios",
        candidateFullName = "Techno",
        location = "Los Angeles",
        locationType = "Sur site",
        status = "Refusé",
        stages = "Terminer",
        appliedAt = System.currentTimeMillis() - 1209600000 // Il y a 14 jours
    ),
    JobApplication(
        jobOfferTitle = "Spécialiste en Marketing",
        candidateProfileId = "profile_id",
        enterpriseProfileId = "enterprise_profile_id",
        jobOfferId = "job_offer_id",
        companyName = "Marketing Masters",
        candidateFullName = "Techno",
        location = "Chicago",
        locationType = "À distance",
        status = "En attente",
        stages = "Entretien",
        appliedAt = System.currentTimeMillis() - 1296000000 // Il y a 15 jours
    )
)
