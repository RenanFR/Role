package com.agamatec.role

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class NextStepsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_next_steps)

        findViewById<Button>(R.id.exit_button).setOnClickListener {
            finishAffinity()
        }
    }
}
