package com.diva.pimpad.ui.register

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.diva.pimpad.R
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class RegisterActivityTest {
    private val dummyName = "test"
    private val dummyEmail = "test@gmail.com"
    private val dummyPassword = "test123"
    private val dummyConfirmPassword = "test123"
    private val emptyInput = ""
    private val fieldEmptyName = "Masukkan nama"
    private val fieldEmptyEmail = "Masukkan email"
    private val fieldEmptyPassword = "Masukkan password"
    private val fieldEmptyConfirmPassword = "Masukkan konfirmasi password"

    @Before
    fun setup(){
        ActivityScenario.launch(RegisterActivity::class.java)
    }

    @Test
    fun assetsGetRegister() {
        onView(withId(R.id.edt_name)).perform(typeText(dummyName), closeSoftKeyboard())
        onView(withId(R.id.edt_email)).perform(typeText(dummyEmail), closeSoftKeyboard())
        onView(withId(R.id.edt_password)).perform(typeText(dummyPassword), closeSoftKeyboard())
        onView(withId(R.id.edt_confirmpassword)).perform(typeText(dummyConfirmPassword), closeSoftKeyboard())

        onView(withId(R.id.btn_register)).check(matches(isDisplayed()))
        onView(withId(R.id.btn_register)).perform(click())

    }


    //Pengecekan untuk empty input
    @Test
    fun assertEmptyInput() {
        // pengecekan input untuk name
        onView(withId(R.id.edt_name)).perform(typeText(emptyInput), closeSoftKeyboard())

        onView(withId(R.id.btn_register)).check(matches(isDisplayed()))
        onView(withId(R.id.btn_register)).perform(click())

        onView(withId(R.id.edt_name)).check(matches(hasErrorText(fieldEmptyName)))
        onView(withId(R.id.edt_name)).perform(typeText(dummyName), closeSoftKeyboard())

        // pengecekan input untuk email
        onView(withId(R.id.edt_email)).perform(typeText(emptyInput), closeSoftKeyboard())

        onView(withId(R.id.btn_register)).check(matches(isDisplayed()))
        onView(withId(R.id.btn_register)).perform(click())

        onView(withId(R.id.edt_email)).check(matches(hasErrorText(fieldEmptyEmail)))
        onView(withId(R.id.edt_email)).perform(typeText(dummyEmail), closeSoftKeyboard())

        // pengecekan input untuk password
        onView(withId(R.id.edt_password)).perform(typeText(emptyInput), closeSoftKeyboard())

        onView(withId(R.id.btn_register)).check(matches(isDisplayed()))
        onView(withId(R.id.btn_register)).perform(click())

        onView(withId(R.id.edt_password)).check(matches(hasErrorText(fieldEmptyPassword)))
        onView(withId(R.id.edt_password)).perform(typeText(dummyPassword), closeSoftKeyboard())

        // pengecekan input untuk konfirmasi password
        onView(withId(R.id.edt_confirmpassword)).perform(typeText(emptyInput), closeSoftKeyboard())

        onView(withId(R.id.btn_register)).check(matches(isDisplayed()))
        onView(withId(R.id.btn_register)).perform(click())

        onView(withId(R.id.edt_confirmpassword)).check(matches(hasErrorText(fieldEmptyConfirmPassword)))
        onView(withId(R.id.edt_confirmpassword)).perform(typeText(dummyConfirmPassword), closeSoftKeyboard())

    }
}