package com.example.hiretop.models

import com.google.firebase.firestore.DocumentId

data class EnterpriseProfile(
    @DocumentId
    val enterpriseID: String? = null,
    val bannerUrl: String? = null,
    val pictureUrl: String? = null,
    val name: String? = null,
    val headline: String? = null,
    val industry: String? = null,
    val location: String? = null,
    val about: String? = null,
    val cultureAndValues: String? = null,
    val contactDetails: String? = null,
    val createdAt: Long? = null,
    val updatedAt: Long? = null,
) {
    constructor() : this(null, null, null, null, null, null, null, null, null, null, null, null)
}
