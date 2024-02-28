package com.example.hiretop

import android.content.Context
import com.example.hiretop.utils.Utils
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import java.util.*

@RunWith(MockitoJUnitRunner::class)
class UtilsTest {

    @Mock
    private lateinit var mockContext: Context

    @Test
    fun testGetYearsList() {
        val yearsList = Utils.getYearsList()
        assertTrue(yearsList.isNotEmpty())
        assertEquals("Année", yearsList.first())
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        assertEquals((currentYear - 1922).toString(), yearsList.size.toString())
    }

    @Test
    fun testGetPostedTimeAgo() {
        val timestamp = System.currentTimeMillis() - (24 * 60 * 60 * 1000) // 1 day ago
        val result = Utils.getPostedTimeAgo(mockContext, timestamp)
        val expected = mockContext.getString(R.string.posted_days_ago_text, 1)
        assertEquals(expected, result)
    }

    @Test
    fun testGetAppliedTimeAgo() {
        // Similar to testGetPostedTimeAgo
    }

    @Test
    fun testFormatDate() {
        // Mocking timestamp
        val timestamp = 1613337600000 // 15 Feb 2021
        val result = Utils.formatDate(timestamp)
        assertEquals("15 - février - 2021 à 00h00", result)
    }

    @Test
    fun testGetFormattedDateOrHour() {
        // Similar to testGetPostedTimeAgo
    }

    @Test
    fun testMonthToNumber() {
        assertEquals("01", Utils.monthToNumber("janvier"))
        assertEquals("02", Utils.monthToNumber("février"))
        // Add similar assertions for other months
    }

    @Test
    fun testExtractStringFromLink() {
        val link = "https://firebasestorage.googleapis.com/v0/b/hiretop-6c0ef.appspot.com/o/images%2F30c8df62-2199-410e-8a83-c4b7a7cbed43_compressed_profile_picture?alt=media&token=1742bcf2-c7a8-4892-965a-c14346bea954"
        val result = Utils.extractStringFromLink(link)
        assertEquals("image_name.jpg", result)
    }

    @Test
    fun testIsEmailValid() {
        assertTrue(Utils.isEmailValid("test@example.com"))
        assertTrue(!Utils.isEmailValid("testexample.com")) // Invalid email without '@'
    }

    @Test
    fun testIsValidPassword() {
        assertTrue(Utils.isValidPassword("Password123@")) // Valid password
        assertTrue(!Utils.isValidPassword("password123")) // Missing uppercase
        assertTrue(!Utils.isValidPassword("PASSWORD123")) // Missing lowercase
        assertTrue(!Utils.isValidPassword("Password")) // Missing digit and special character
    }
}