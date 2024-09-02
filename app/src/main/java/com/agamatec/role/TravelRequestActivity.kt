package com.agamatec.role

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

class TravelRequestActivity : AppCompatActivity() {

    private lateinit var travelersLayout: LinearLayout
    private var travelerCount = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_travel_request)

        Log.d("TravelRequestActivity", "onCreate: Activity inicializada")

        travelersLayout = findViewById(R.id.travelers_layout)

        findViewById<Button>(R.id.add_traveler_button).setOnClickListener {
            Log.d("TravelRequestActivity", "onCreate: Botão de adicionar viajante clicado")
            addTravelerForm()
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

    private fun submitForm() {
        Log.d("TravelRequestActivity", "submitForm: Iniciando processo de envio de formulário")

        try {
            val jsonBody = JSONObject().apply {
                put("name", "Nome do Viajante")
                put("cpf", "000.000.000-00")
                put("email", "email@exemplo.com")
            }

            val mediaType = "application/json; charset=utf-8".toMediaTypeOrNull()
            val requestBody = RequestBody.create(mediaType, jsonBody.toString())

            Log.d("TravelRequestActivity", "submitForm: Corpo da requisição criado: $jsonBody")

            val client = OkHttpClient()
            val url = "http://10.0.2.2:8080/api/travel/submit"
            val request = Request.Builder()
                .url(url)
                .post(requestBody)
                .build()

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
                            this,
                            "Erro ao conectar à API: ${e.message}",
                            Toast.LENGTH_SHORT
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
            "TravelRequestActivity",
            "navigateToNextSteps: Navegando para a tela de próximos passos"
        )
        val intent = Intent(this, NextStepsActivity::class.java)
        startActivity(intent)
        finish()
    }
}
