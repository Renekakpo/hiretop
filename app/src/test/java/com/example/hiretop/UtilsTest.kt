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

    /*@Test
    fun testCompressImage() {
        val mockContext = InstrumentationRegistry.getInstrumentation().targetContext
        val uri = // Provide a valid URI for testing
        val filename = "test_image.png"

        val file = Utils.compressImage(mockContext, uri, filename) { errorMessage ->
            println("Compression error: $errorMessage")
        }

        assertTrue(file != null)
    }*/

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
        val link = "https://example.com/images%2Fimage_name.jpg?"
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