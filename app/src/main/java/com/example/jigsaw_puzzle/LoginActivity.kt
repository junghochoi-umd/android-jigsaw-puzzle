package com.example.jigsaw_puzzle

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val loginButton: Button = findViewById(R.id.login)

        loginButton.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, AppActivity::class.java)
            startActivity(intent)
        })
    }
}