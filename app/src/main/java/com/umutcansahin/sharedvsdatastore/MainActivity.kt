package com.umutcansahin.sharedvsdatastore

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import java.security.Key

const val SHARED_NAME = "myPref"
const val NAME = "name"
const val AGE = "age"
const val IS_ADMIN = "isAdmin"

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    private fun sharedPreferences() {
        /**CREATE SHARED PREFERENCES*/
        val sharedPreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE)

        /**EDITOR*/
        val editor = sharedPreferences.edit()

        /**SAVE DATA WİTH SHARED PREFERENCES*/
        editor.putBoolean("isAdmin", true)
        editor.putString("name", "John")
        editor.putInt("age", 20)
        editor.apply()

        /**GET DATA FROM SHARED PREFERENCES*/
        sharedPreferences.getBoolean("isAdmin", false)
        sharedPreferences.getString("name", null)
        sharedPreferences.getInt("age", 0)

        /**REMOVE DATA FROM SHARED*/
        editor.remove("isAdmin")
        editor.apply()
    }
}