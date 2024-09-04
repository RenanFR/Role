package com.agamatec.role

import android.view.View
import android.widget.DatePicker
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import org.hamcrest.Matcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TravelRequestActivityTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(TravelRequestActivity::class.java)

    @Test
    fun preencherFormularioEApertarEnviar() {


        onView(withId(R.id.name_edit_text)).perform(
            typeText("Renan Fernandes Rodrigues"), closeSoftKeyboard()
        )


        onView(withId(R.id.cpf_edit_text)).perform(typeText("151.311.560-09"), closeSoftKeyboard())


        onView(withId(R.id.email_edit_text)).perform(
            typeText("renanfr1047@gmail.com"), closeSoftKeyboard()
        )


        onView(withId(R.id.age_edit_text)).perform(typeText("28"), closeSoftKeyboard())


        onView(withId(R.id.gender_spinner)).perform(click())
        onView(withText("Masculino")).perform(click())


        onView(withId(R.id.add_destination_button)).perform(click())


        onView(withId(R.id.destination_city_edit_text)).check(matches(isDisplayed()))
        onView(withId(R.id.destination_city_edit_text)).perform(
            typeText("Madrid"), closeSoftKeyboard()
        )



        onView(withId(R.id.destination_country_edit_text)).perform(
            typeText("Espanha"), closeSoftKeyboard()
        )


        onView(withId(R.id.budget_edit_text)).perform(typeText("1000 EUR"), closeSoftKeyboard())


        onView(withId(R.id.hotel_address_edit_text)).perform(
            typeText("Calle Mayor, 10"), closeSoftKeyboard()
        )


        onView(withId(R.id.hotel_reserved_checkbox)).perform(click())


        onView(withId(R.id.departure_date_picker)).perform(setDate(2024, 9, 15))


        onView(withId(R.id.return_date_picker)).perform(setDate(2024, 9, 20))


        onView(withId(R.id.travel_purpose_edit_text)).perform(
            typeText("Férias"), closeSoftKeyboard()
        )


        onView(withId(R.id.add_traveler_button)).perform(click())


        onView(withId(R.id.name_edit_text)).perform(
            typeText("Maria Jéssica Pereira Mendes"), closeSoftKeyboard()
        )


        onView(withId(R.id.filiation_edit_text)).perform(typeText("Filha"), closeSoftKeyboard())


        onView(withId(R.id.birth_date_edit_text)).perform(
            typeText("15/05/1995"), closeSoftKeyboard()
        )


        onView(withId(R.id.submit_button)).perform(click())
    }


    private fun setDate(year: Int, month: Int, day: Int): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> = isAssignableFrom(DatePicker::class.java)

            override fun getDescription(): String =
                "Configura a data no DatePicker para $day/$month/$year"

            override fun perform(uiController: UiController?, view: View?) {
                val datePicker = view as DatePicker
                datePicker.updateDate(year, month - 1, day)
            }
        }
    }
}
