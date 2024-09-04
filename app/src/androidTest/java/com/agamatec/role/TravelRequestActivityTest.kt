package com.agamatec.role

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TravelRequestActivityTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(TravelRequestActivity::class.java)

    @Test
    fun preencherFormularioEApertarEnviar() {

        onView(withId(R.id.name_edit_text)).perform(typeText("John Doe"), closeSoftKeyboard())


        onView(withId(R.id.cpf_edit_text)).perform(typeText("12345678900"), closeSoftKeyboard())


        onView(withId(R.id.email_edit_text)).perform(
            typeText("john@example.com"),
            closeSoftKeyboard()
        )


        onView(withId(R.id.age_edit_text)).perform(typeText("30"), closeSoftKeyboard())


        onView(withId(R.id.gender_spinner)).perform(click())
        onView(withText("Masculino")).perform(click())


        onView(withId(R.id.add_destination_button)).perform(click())


        onView(withId(R.id.destination_city_edit_text)).perform(
            typeText("Paris"),
            closeSoftKeyboard()
        )


        onView(withId(R.id.add_traveler_button)).perform(click())


        onView(withId(R.id.name_edit_text)).perform(typeText("Jane Doe"), closeSoftKeyboard())


        onView(withId(R.id.submit_button)).perform(click())
    }
}
