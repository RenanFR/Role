package com.agamatec.role

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.DatePicker
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class TravelRequestActivity : AppCompatActivity() {

    private lateinit var travelersLayout: LinearLayout
    private lateinit var destinationsLayout: LinearLayout
    private var travelerCount = 1
    private var destinationCount = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_travel_request)

        Log.d("TravelRequestActivity", "onCreate: Activity inicializada")

        travelersLayout = findViewById(R.id.travelers_layout)
        destinationsLayout = findViewById(R.id.destinations_layout)

        findViewById<Button>(R.id.add_traveler_button).setOnClickListener {
            Log.d("TravelRequestActivity", "onCreate: Botão de adicionar viajante clicado")
            addTravelerForm()
        }

        findViewById<Button>(R.id.add_destination_button).setOnClickListener {
            Log.d("TravelRequestActivity", "onCreate: Botão de adicionar destino clicado")
            addDestinationForm()
        }

        findViewById<Button>(R.id.submit_button).setOnClickListener {
            Log.d("TravelRequestActivity", "onCreate: Botão de enviar formulário clicado")
            submitForm()
        }
    }

    private fun addTravelerForm() {
        Log.d("TravelRequestActivity", "addTravelerForm: Adicionando formulário de viajante")
        val travelerForm = layoutInflater.inflate(R.layout.traveler_form, null)
        travelersLayout.addView(travelerForm)
        travelerCount++
    }

    private fun addDestinationForm() {
        Log.d("TravelRequestActivity", "addDestinationForm: Adicionando formulário de destino")
        val destinationForm = layoutInflater.inflate(R.layout.destination_form, null)
        destinationsLayout.addView(destinationForm)
        destinationCount++
    }

    private fun submitForm() {
        Log.d("TravelRequestActivity", "submitForm: Iniciando processo de envio de formulário")

        try {

            val name = findViewById<EditText>(R.id.name_edit_text).text.toString()
            val cpf = findViewById<EditText>(R.id.cpf_edit_text).text.toString()
            val email = findViewById<EditText>(R.id.email_edit_text).text.toString()
            val age = findViewById<EditText>(R.id.age_edit_text).text.toString().toIntOrNull()
            val gender = findViewById<Spinner>(R.id.gender_spinner).selectedItem.toString()


            val destinations = JSONArray()
            for (i in 0 until destinationsLayout.childCount) {
                val destinationView = destinationsLayout.getChildAt(i)

                val departureDate =
                    (destinationView.findViewById<DatePicker>(R.id.departure_date_picker)).let {
                        "${it.dayOfMonth}/${it.month + 1}/${it.year}"
                    }
                val returnDate =
                    (destinationView.findViewById<DatePicker>(R.id.return_date_picker)).let {
                        "${it.dayOfMonth}/${it.month + 1}/${it.year}"
                    }
                val hotelReserved =
                    destinationView.findViewById<CheckBox>(R.id.hotel_reserved_checkbox).isChecked
                val hotelAddress =
                    destinationView.findViewById<EditText>(R.id.hotel_address_edit_text).text.toString()
                val country =
                    destinationView.findViewById<EditText>(R.id.destination_country_edit_text).text.toString()
                val city =
                    destinationView.findViewById<EditText>(R.id.destination_city_edit_text).text.toString()
                val budget =
                    destinationView.findViewById<EditText>(R.id.budget_edit_text).text.toString()
                val purpose =
                    destinationView.findViewById<EditText>(R.id.travel_purpose_edit_text).text.toString()

                val destinationJson = JSONObject().apply {
                    put("departureDate", departureDate)
                    put("returnDate", returnDate)
                    put("hotelReserved", hotelReserved)
                    put("hotelAddress", hotelAddress)
                    put("country", country)
                    put("city", city)
                    put("budget", budget)
                    put("purpose", purpose)
                }
                destinations.put(destinationJson)
            }


            val travelers = JSONArray()
            for (i in 0 until travelersLayout.childCount) {
                val travelerView = travelersLayout.getChildAt(i)

                val travelerName =
                    travelerView.findViewById<EditText>(R.id.additional_traveler_name_edit_text).text.toString()

                val travelerGender =
                    travelerView.findViewById<Spinner>(R.id.additional_traveler_gender_spinner).selectedItem.toString()

                val travelerJson = JSONObject().apply {
                    put("name", travelerName)

                    put("gender", travelerGender)
                }
                travelers.put(travelerJson)
            }


            val jsonBody = JSONObject().apply {
                put("name", name)
                put("cpf", cpf)
                put("email", email)
                put("age", age)
                put("gender", gender)
                put("destinations", destinations)
                put("travelers", travelers)
            }

            val mediaType = "application/json; charset=utf-8".toMediaTypeOrNull()
            val requestBody = RequestBody.create(mediaType, jsonBody.toString())

            Log.d("TravelRequestActivity", "submitForm: Corpo da requisição criado: $jsonBody")

            val client = OkHttpClient()
            val url = "http://10.0.2.2:8080/api/travel/submit"
            val request = Request.Builder().url(url).post(requestBody).build()

            Log.d("TravelRequestActivity", "submitForm: Requisição construída, URL: $url")

            Thread {
                try {
                    val response: Response = client.newCall(request).execute()
                    Log.d(
                        "TravelRequestActivity",
                        "submitForm: Requisição enviada, aguardando resposta"
                    )

                    if (response.isSuccessful) {
                        val responseBody = response.body?.string()
                        Log.d(
                            "TravelRequestActivity",
                            "submitForm: Requisição bem-sucedida: ${response.code}, Resposta: $responseBody"
                        )

                        runOnUiThread {
                            Toast.makeText(this, responseBody, Toast.LENGTH_LONG).show()
                            navigateToNextSteps()
                        }
                    } else {
                        Log.e("API_ERROR", "Erro na requisição: ${response.code}")
                        runOnUiThread {
                            Toast.makeText(
                                this,
                                "Erro ao enviar a requisição: ${response.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                } catch (e: IOException) {
                    Log.e("API_ERROR", "Exceção na requisição", e)
                    runOnUiThread {
                        Toast.makeText(
                            this, "Erro ao conectar à API: ${e.message}", Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }.start()

        } catch (e: Exception) {
            Log.e("submitForm", "Erro ao criar a requisição", e)
            Toast.makeText(this, "Erro ao processar o formulário: ${e.message}", Toast.LENGTH_SHORT)
                .show()
        }
    }


    private fun navigateToNextSteps() {
        Log.d(
            "TravelRequestActivity", "navigateToNextSteps: Navegando para a tela de próximos passos"
        )
        val intent = Intent(this, NextStepsActivity::class.java)
        startActivity(intent)
        finish()
    }
}
