package com.example.hiretop.models

import kotlin.random.Random

data class JobOffer(
    val title: String,
    val company: String,
    val jobType: String,
    val description: String,
    val locationType: String,
    val skills: List<String>,
    val education: List<String>,
    val updatedAt: Long,
    val closedAt: Long? = null,
    val postedAt: Long
)

fun generateFakeJobOffers(count: Int): List<JobOffer> {
    val jobTitles = listOf(
        "Ingénieur Logiciel",
        "Analyste de Données",
        "Chef de Produit",
        "Spécialiste en Marketing",
        "Designer Graphique",
        "Analyste Financier",
        "Directeur des Ressources Humaines",
        "Représentant des Ventes",
        "Représentant du Service Client",
        "Chef de Projet"
    )

    val companies = listOf(
        "Tech Corp",
        "Solutions de Données Ltd.",
        "InnovateTech",
        "Pros du Marketing",
        "Studios de Design Inc.",
        "Groupe de Consultants Financiers",
        "Solutions RH",
        "Entreprises de Ventes",
        "Inc. de Soin Client",
        "Experts en Gestion de Projet"
    )

    val jobTypes = listOf(
        "Temps Plein",
        "Temps Partiel",
        "Contrat",
        "Stage"
    )

    val jobDescription = listOf(
        "Nous recherchons un ingénieur logiciel hautement qualifié pour rejoindre notre équipe...",
        "Rejoignez notre équipe marketing pour aider à développer et mettre en œuvre des stratégies de marketing...",
        "Nous recherchons un analyste de données qualifié pour analyser et interpréter les données...",
        "Nous embauchons un talentueux designer UX/UI pour créer des expériences utilisateur engageantes et intuitives...",
        "Rejoignez notre équipe financière pour analyser les données financières et fournir des insights...",
        "Nous recherchons un représentant des ventes motivé pour stimuler les ventes et la croissance des revenus...",
        "Diriger le développement de produits et gérer le cycle de vie des produits pour nos produits innovants...",
        "Coordonner les activités RH et soutenir l'équipe RH dans diverses tâches administratives...",
        "Créer des graphiques et des designs visuellement attrayants pour divers projets et campagnes...",
        "Fournir un support client exceptionnel pour résoudre les demandes de renseignements et les problèmes..."
    )

    val locationTypes = listOf(
        "À Distance",
        "Sur Place",
        "Hybride"
    )

    val skills = listOf(
        listOf("Java", "Kotlin", "Android", "Développement Mobile"),
        listOf("Python", "R", "Analyse de Données", "SQL"),
        listOf("Gestion de Produit", "Agile", "Scrum"),
        listOf("Marketing", "Médias Sociaux", "SEO"),
        listOf("Adobe Photoshop", "Illustrator", "Design UI/UX"),
        listOf("Analyse Financière", "Comptabilité", "Excel"),
        listOf("Recrutement", "Relations Employés", "Politiques RH"),
        listOf("Ventes", "Négociation", "CRM"),
        listOf("Support Client", "Communication", "Résolution de Problèmes"),
        listOf("Gestion de Projet", "Leadership d'Équipe", "Gestion du Temps")
    )

    val educations = listOf(
        listOf("Baccalauréat", "Informatique"),
        listOf("Master", "Statistiques"),
        listOf("MBA", "Administration des Affaires"),
        listOf("Baccalauréat", "Marketing"),
        listOf("Baccalauréat", "Design Graphique"),
        listOf("Baccalauréat", "Finance"),
        listOf("Master", "Ressources Humaines"),
        listOf("Baccalauréat", "Gestion des Ventes"),
        listOf("Baccalauréat", "Communication"),
        listOf("Master", "Gestion de Projet")
    )

    val currentTimeMillis = System.currentTimeMillis()

    return List(count) {
        JobOffer(
            title = jobTitles.random(),
            company = companies.random(),
            jobType = jobTypes.random(),
            description = jobDescription.random(),
            locationType = locationTypes.random(),
            skills = skills.random(),
            education = educations.random(),
            updatedAt = Random.nextLong(currentTimeMillis - 2592000000, currentTimeMillis),
            closedAt = Random.nextLong(currentTimeMillis - 2592000000, currentTimeMillis),
            postedAt = Random.nextLong(currentTimeMillis - 2592000000, currentTimeMillis)
        )
    }
}