package com.agamatec.role

import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class TravelRequestActivity : AppCompatActivity() {

    private lateinit var travelersLayout: LinearLayout
    private var travelerCount = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_travel_request)

        travelersLayout = findViewById(R.id.travelers_layout)

        findViewById<Button>(R.id.add_traveler_button).setOnClickListener {
            addTravelerForm()
        }

        findViewById<Button>(R.id.submit_button).setOnClickListener {
            submitForm()
        }
    }

    private fun addTravelerForm() {
        val travelerForm = layoutInflater.inflate(R.layout.traveler_form, null)
        travelersLayout.addView(travelerForm)
        travelerCount++
    }

    private fun submitForm() {
        
        Toast.makeText(this, "Formulário submetido!", Toast.LENGTH_SHORT).show()
    }
}
