package com.example.hiretop

import com.example.hiretop.utils.Utils
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import java.util.*

@RunWith(MockitoJUnitRunner::class)
class UtilsTest {

    @Test
    fun testGetYearsList() {
        val yearsList = Utils.getYearsList()
        assertTrue(yearsList.isNotEmpty())
        assertEquals("Année", yearsList.first())
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        assertEquals((currentYear - 1922).toString(), yearsList.size.toString())
    }

    @Test
    fun testFormatDate() {
        // Mocking timestamp
        val timestamp = 1613337600000 // 14 Feb 2021
        val result = Utils.formatDate(timestamp)
        assertEquals("14 - février - 2021 à 22h20", result)
    }

    @Test
    fun testMonthToNumber() {
        assertEquals("01", Utils.monthToNumber("janvier"))
        assertEquals("02", Utils.monthToNumber("février"))
    }

    @Test
    fun testExtractStringFromLink() {
        val link = "https://firebasestorage.googleapis.com/v0/b/hiretop-6c0ef.appspot.com/o/images%2F30c8df62-2199-410e-8a83-c4b7a7cbed43_compressed_profile_picture?alt=media&token=1742bcf2-c7a8-4892-965a-c14346bea954"
        val result = Utils.extractStringFromLink(link)
        assertEquals("30c8df62-2199-410e-8a83-c4b7a7cbed43_compressed_profile_picture", result)
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