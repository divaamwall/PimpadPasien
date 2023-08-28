package com.diva.pimpad.ui.login

import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ActivityScenario.launch
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.diva.pimpad.R
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginActivityTest {
    private val dummyEmail = "test@gmail.com"
    private val dummyPassword = "test123"
    private val emptyInput = ""
    private val fieldEmptyEmail = "Masukkan email"
    private val fieldEmptyPassword = "Masukkan password"
    private val logout = "Logout"

    @get:Rule
    val activity = ActivityScenarioRule(LoginActivity::class.java)

//    @Before
//    fun setUp() {
////        ActivityScenario.launch(LoginActivity::class.java)
//    }

    @Test
    fun chatUITest(){
        onView(withId(R.id.navigation_chat)).check(matches(isDisplayed()))
        onView(withId(R.id.navigation_chat)).perform(click())
    }

    @Test
    fun detectUICameraTest(){
        onView(withId(R.id.navigation_detect)).check(matches(isDisplayed()))
        onView(withId(R.id.navigation_detect)).perform(click())

        onView(withId(R.id.camera_btn)).check(matches(isDisplayed()))
        onView(withId(R.id.camera_btn)).perform(click())

//        onView(withId(R.id.captureImage)).check(matches(isDisplayed()))
//        onView(withId(R.id.captureImage)).perform(click())
    }


    @Test
    fun detectUIGaleriTest(){
        onView(withId(R.id.navigation_detect)).check(matches(isDisplayed()))
        onView(withId(R.id.navigation_detect)).perform(click())

        onView(withId(R.id.galeri_btn)).check(matches(isDisplayed()))
        onView(withId(R.id.galeri_btn)).perform(click())
    }

    @Test
    fun recyclerViewItemTest(){
        onView(withId(R.id.acnes_rv)).check(matches(isDisplayed()))
        onView(withId(R.id.acnes_rv)).perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(3))
    }

    @Test
    fun recyclerViewDetailTest(){
        onView(withId(R.id.acnes_rv)).check(matches(isDisplayed()))
        onView(withId(R.id.acnes_rv)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(3, click()))
    }

    @Test
    fun loginTest(){
        //pengecekan login
        onView(withId(R.id.ed_email)).perform(typeText(dummyEmail), closeSoftKeyboard())
        onView(withId(R.id.ed_password)).perform(typeText(dummyPassword), closeSoftKeyboard())

        onView(withId(R.id.btn_login)).check(matches(isDisplayed()))
        onView(withId(R.id.btn_login)).perform(click())
    }
}