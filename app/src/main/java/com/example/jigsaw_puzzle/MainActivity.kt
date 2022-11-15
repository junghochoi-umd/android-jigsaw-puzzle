package com.example.jigsaw_puzzle

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun logout(view: View) {
        FirebaseAuth.getInstance().signOut()
        intent = Intent(applicationContext, Login::class.java)
        startActivity(intent)

    }
}