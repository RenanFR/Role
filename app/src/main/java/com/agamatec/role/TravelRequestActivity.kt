package com.agamatec.role

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.DatePicker
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class TravelRequestActivity : AppCompatActivity() {

    private lateinit var travelersLayout: LinearLayout
    private lateinit var destinationsLayout: LinearLayout
    private var travelerCount = 1
    private val travelersList = mutableListOf<JSONObject>()
    private val destinationsList = mutableListOf<JSONObject>()
    private var isAddingDate = false

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

        if (travelersLayout.childCount > 0) {
            travelersLayout.removeAllViews()
        }

        val travelerForm = layoutInflater.inflate(R.layout.traveler_form, null)
        travelersLayout.addView(travelerForm)

        val addTravelerButton = travelerForm.findViewById<Button>(R.id.confirm_traveler_button)

        addTravelerButton.setOnClickListener {

            val travelerName =
                travelerForm.findViewById<EditText>(R.id.additional_traveler_name_edit_text).text.toString()
            val travelerFiliation =
                travelerForm.findViewById<EditText>(R.id.additional_traveler_filiation_edit_text).text.toString()
            val travelerBirthDatePicker =
                travelerForm.findViewById<DatePicker>(R.id.additional_traveler_birth_date_picker)
            val travelerGender =
                travelerForm.findViewById<Spinner>(R.id.additional_traveler_gender_spinner).selectedItem.toString()

            val day = travelerBirthDatePicker.dayOfMonth
            val month = travelerBirthDatePicker.month + 1
            val year = travelerBirthDatePicker.year
            val travelerBirthDate = "$day/$month/$year"

            val travelerJson = JSONObject().apply {
                put("name", travelerName)
                put("birthdate", travelerBirthDate)
                put("gender", travelerGender)
                put("filiation", travelerFiliation)
            }

            travelersList.add(travelerJson)

            val travelerLabel = TextView(this)
            travelerLabel.text =
                "Viajante: $travelerName - Nascimento: $travelerBirthDate - Gênero: $travelerGender - Filiação: $travelerFiliation"
            travelersLayout.addView(travelerLabel)

            travelersLayout.removeView(travelerForm)
        }
    }


    private fun addDestinationForm() {
        val destinationForm = layoutInflater.inflate(R.layout.destination_form, null)
        destinationsLayout.addView(destinationForm)

        val datesLayout = destinationForm.findViewById<LinearLayout>(R.id.dates_layout)
        val addDateButton = destinationForm.findViewById<Button>(R.id.add_date_button)
        val segmentDatesCheckbox =
            destinationForm.findViewById<CheckBox>(R.id.segment_dates_checkbox)
        var segmentedPeriod = false
        val datesList = mutableListOf<Pair<String, String>>()

        segmentDatesCheckbox.setOnCheckedChangeListener { _, isChecked ->
            segmentedPeriod = isChecked
            addDateButton.visibility = if (isChecked) View.VISIBLE else View.GONE
        }

        val initialDatePair = layoutInflater.inflate(R.layout.date_pair_form, null)
        datesLayout.addView(initialDatePair)

        val confirmDateButton = initialDatePair.findViewById<Button>(R.id.confirm_date_button)

        confirmDateButton.setOnClickListener {
            if (!isAddingDate) {
                val departureDate =
                    initialDatePair.findViewById<DatePicker>(R.id.departure_date_picker)
                val returnDate = initialDatePair.findViewById<DatePicker>(R.id.return_date_picker)

                val departureDateFormatted =
                    "${departureDate.dayOfMonth}/${departureDate.month + 1}/${departureDate.year}"
                val returnDateFormatted =
                    "${returnDate.dayOfMonth}/${returnDate.month + 1}/${returnDate.year}"

                datesList.add(Pair(departureDateFormatted, returnDateFormatted))

                val dateLabel = TextView(this)
                dateLabel.text = "Ida: $departureDateFormatted - Volta: $returnDateFormatted"
                datesLayout.addView(dateLabel)

                addDateButton.visibility = View.VISIBLE
                isAddingDate = false
            }
        }

        addDateButton.visibility = View.GONE

        addDateButton.setOnClickListener {
            if (!isAddingDate) {
                datesLayout.removeAllViews()

                val datePairView = layoutInflater.inflate(R.layout.date_pair_form, null)
                datesLayout.addView(datePairView)

                isAddingDate = true

                val confirmNewDateButton =
                    datePairView.findViewById<Button>(R.id.confirm_date_button)
                confirmNewDateButton.setOnClickListener {
                    val departureDate =
                        datePairView.findViewById<DatePicker>(R.id.departure_date_picker)
                    val returnDate = datePairView.findViewById<DatePicker>(R.id.return_date_picker)

                    val departureDateFormatted =
                        "${departureDate.dayOfMonth}/${departureDate.month + 1}/${departureDate.year}"
                    val returnDateFormatted =
                        "${returnDate.dayOfMonth}/${returnDate.month + 1}/${returnDate.year}"

                    datesList.add(Pair(departureDateFormatted, returnDateFormatted))

                    val dateLabel = TextView(this)
                    dateLabel.text = "Ida: $departureDateFormatted - Volta: $returnDateFormatted"
                    datesLayout.addView(dateLabel)

                    isAddingDate = false
                }
            }
        }

        destinationForm.findViewById<Button>(R.id.confirm_destination_button).setOnClickListener {
            val hotelReserved =
                destinationForm.findViewById<CheckBox>(R.id.hotel_reserved_checkbox).isChecked
            val hotelAddress =
                destinationForm.findViewById<EditText>(R.id.hotel_address_edit_text).text.toString()
            val country =
                destinationForm.findViewById<EditText>(R.id.destination_country_edit_text).text.toString()
            val city =
                destinationForm.findViewById<EditText>(R.id.destination_city_edit_text).text.toString()
            val budget =
                destinationForm.findViewById<EditText>(R.id.budget_edit_text).text.toString()
            val purpose =
                destinationForm.findViewById<EditText>(R.id.travel_purpose_edit_text).text.toString()

            val destinationJson = JSONObject().apply {
                put("segmentedPeriod", segmentedPeriod)
                put("dateRanges", JSONArray().apply {
                    datesList.forEach { (departure, returnDate) ->
                        put(JSONObject().apply {
                            put("departureDate", departure)
                            put("returnDate", returnDate)
                        })
                    }
                })
                put("hotelReserved", hotelReserved)
                put("hotelAddress", hotelAddress)
                put("country", country)
                put("city", city)
                put("budget", budget)
                put("purpose", purpose)
            }

            destinationsList.add(destinationJson)

            val destinationLabel = TextView(this)
            destinationLabel.text = "Destino: $city, $country - Orçamento: $budget"
            destinationsLayout.addView(destinationLabel)

            destinationsLayout.removeView(destinationForm)
        }
    }

    private fun submitForm() {
        Log.d("TravelRequestActivity", "submitForm: Iniciando processo de envio de formulário")

        try {
            val name = findViewById<EditText>(R.id.name_edit_text).text.toString()
            val cpf = findViewById<EditText>(R.id.cpf_edit_text).text.toString()
            val email = findViewById<EditText>(R.id.email_edit_text).text.toString()

            val birthdatePicker = findViewById<DatePicker>(R.id.birthdate_date_picker)
            val birthdate =
                "${birthdatePicker.dayOfMonth}/${birthdatePicker.month + 1}/${birthdatePicker.year}"

            val gender = findViewById<Spinner>(R.id.gender_spinner).selectedItem.toString()

            val jsonBody = JSONObject().apply {
                put("mainTraveler", JSONObject().apply {
                    put("name", name)
                    put("cpf", cpf)
                    put("email", email)
                    put("birthdate", birthdate)
                    put("gender", gender)
                })
                put("destinations", JSONArray(destinationsList))
                put("travelers", JSONArray(travelersList))
            }

            val mediaType = "application/json; charset=utf-8".toMediaTypeOrNull()
            val requestBody = jsonBody.toString().toRequestBody(mediaType)

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
