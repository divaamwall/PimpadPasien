package com.diva.pimpad

import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class MainActivityTest {
    private val dummyEmail = "test@gmail.com"
    private val dummyPassword = "test123"

//    @get:Rule
//    val activity = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun setUp(){
        ActivityScenario.launch(MainActivity::class.java)
    }

    @Test
    fun recyclerViewTest() {
//        Espresso.onView(ViewMatchers.withId(R.id.ed_email))
//            .perform(ViewActions.typeText(dummyEmail), ViewActions.closeSoftKeyboard())
//        Espresso.onView(ViewMatchers.withId(R.id.ed_password))
//            .perform(ViewActions.typeText(dummyPassword), ViewActions.closeSoftKeyboard())
//
//        Espresso.onView(ViewMatchers.withId(R.id.btn_login))
//            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
//        Espresso.onView(ViewMatchers.withId(R.id.btn_login)).perform(ViewActions.click())


//        Espresso.onView(ViewMatchers.withId(R.id.acnes_rv))
//            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.acnes_rv))
            .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(4))

//        Espresso.onView(ViewMatchers.withId(R.id.acnes_rv))
//            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.acnes_rv)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(4,
            ViewActions.click()
        ))
//        ViewActions.pressBack()
    }
}