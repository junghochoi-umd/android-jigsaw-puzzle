package com.example.jigsaw_puzzle

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.FirebaseFirestore

import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference


class AppActivity : AppCompatActivity() {

    private val TAG = "AppActivity"

    private lateinit var bottomNav : BottomNavigationView


    private lateinit var db: FirebaseFirestore

    private lateinit var storage: FirebaseStorage
    private lateinit var storageRef: StorageReference



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app)

        storage = FirebaseStorage.getInstance()
        storageRef = storage.reference


        loadFragment(HomeFragment())

        bottomNav = findViewById(R.id.bottomNav)
        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.menu_play -> {
                    Log.i(TAG, "PlayMenuItem Clicked")
                    loadFragment(HomeFragment())
                    return@setOnItemSelectedListener true
                }
                R.id.menu_friends -> {
                    Log.i(TAG, "PlayMenuItem Clicked")
                    loadFragment(FriendsFragment())
                    return@setOnItemSelectedListener true
                }
                R.id.menu_puzzles -> {
                    Log.i(TAG, "PlayMenuItem Clicked")

                    var puzzleView: Fragment = PuzzleFragment()
                    var args: Bundle = Bundle()



                    puzzleView.arguments
                    loadFragment(PuzzleFragment())
                    return@setOnItemSelectedListener true
                }

                R.id.menu_you -> {
                    Log.i(TAG, "PlayMenuItem Clicked")
                    loadFragment(ProfileFragment())
                    return@setOnItemSelectedListener true
                }
                else -> {
                    Log.i(TAG, "else")
                    return@setOnItemSelectedListener false
                }
            }
        }
    }

    private  fun loadFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        Log.i(TAG, "transaction created")
        transaction.replace(R.id.container,fragment)
        Log.i(TAG, "1")
        transaction.addToBackStack(null)
        Log.i(TAG, "2")
        transaction.commit()
        Log.i(TAG, "3")
    }


}