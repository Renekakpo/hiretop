package com.example.hiretop.models

import com.google.firebase.firestore.DocumentId

data class EnterpriseProfile(
    @DocumentId
    val enterpriseID: String? = null,
    val bannerUrl: String?,
    val pictureUrl: String?,
    val name: String?,
    val headline: String?,
    val industry: String?,
    val location: String?,
    val about: String?,
    val cultureAndValues: String?,
    val contactDetails: String?,
    val createdAt: Long,
    val updatedAt: Long,
)
