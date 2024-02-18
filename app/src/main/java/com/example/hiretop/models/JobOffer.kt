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
    val postedAt: Long
)

fun generateFakeJobOffers(count: Int): List<JobOffer> {
    val jobTitles = listOf(
        "Software Engineer",
        "Data Analyst",
        "Product Manager",
        "Marketing Specialist",
        "Graphic Designer",
        "Financial Analyst",
        "Human Resources Manager",
        "Sales Representative",
        "Customer Service Representative",
        "Project Manager"
    )

    val companies = listOf(
        "Tech Corp",
        "Data Solutions Ltd.",
        "InnovateTech",
        "Marketing Pros",
        "Design Studios Inc.",
        "Financial Consultants Group",
        "HR Solutions",
        "Sales Enterprises",
        "Customer Care Inc.",
        "Project Management Experts"
    )

    val jobTypes = listOf(
        "Full-time",
        "Part-time",
        "Contract",
        "Internship"
    )

    val jobDescription = listOf(
        "We're looking for a highly skilled software engineer to join our team...",
        "Join our marketing team to help develop and implement marketing strategies...",
        "We're seeking a skilled data analyst to analyze and interpret data...",
        "We're hiring a talented UX/UI designer to create engaging and intuitive user experiences...",
        "Join our finance team to analyze financial data and provide insights...",
        "We're seeking a motivated sales representative to drive sales and revenue growth...",
        "Lead product development and manage product lifecycle for our innovative products...",
        "Coordinate HR activities and support the HR team in various administrative tasks...",
        "Create visually appealing graphics and designs for various projects and campaigns...",
        "Provide exceptional customer support to resolve inquiries and issues..."
    )

    val locationTypes = listOf(
        "Remote",
        "On-site"
    )

    val skills = listOf(
        listOf("Java", "Kotlin", "Android", "Mobile Development"),
        listOf("Python", "R", "Data Analysis", "SQL"),
        listOf("Product Management", "Agile", "Scrum"),
        listOf("Marketing", "Social Media", "SEO"),
        listOf("Adobe Photoshop", "Illustrator", "UI/UX Design"),
        listOf("Financial Analysis", "Accounting", "Excel"),
        listOf("Recruitment", "Employee Relations", "HR Policies"),
        listOf("Sales", "Negotiation", "CRM"),
        listOf("Customer Support", "Communication", "Problem Solving"),
        listOf("Project Management", "Team Leadership", "Time Management")
    )

    val educations = listOf(
        listOf("Bachelor's Degree", "Computer Science"),
        listOf("Master's Degree", "Statistics"),
        listOf("MBA", "Business Administration"),
        listOf("Bachelor's Degree", "Marketing"),
        listOf("Bachelor's Degree", "Graphic Design"),
        listOf("Bachelor's Degree", "Finance"),
        listOf("Master's Degree", "Human Resources"),
        listOf("Bachelor's Degree", "Sales Management"),
        listOf("Bachelor's Degree", "Communication"),
        listOf("Master's Degree", "Project Management")
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
            postedAt = Random.nextLong(currentTimeMillis - 2592000000, currentTimeMillis) // Random timestamp within the last 30 days
        )
    }
}