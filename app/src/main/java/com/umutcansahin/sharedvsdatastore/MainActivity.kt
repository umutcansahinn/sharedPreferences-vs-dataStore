package com.umutcansahin.sharedvsdatastore

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.umutcansahin.sharedvsdatastore.databinding.ActivityMainBinding
import java.security.Key

const val SHARED_NAME = "myPref"
const val NAME = "name"
const val AGE = "age"
const val IS_ADMIN = "isAdmin"

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
        editor.remove("name")
        editor.commit()

        /**CLEAR DATA FROM SHARED(CLEAR IS DELETE ALL İTEMS)*/
        editor.clear()
        editor.apply()
    }

    private fun trySharedPreferences() {
        /**CREATE SHARED PREFERENCES*/
        val sharedPreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE)

        /**EDITOR*/
        val editor = sharedPreferences.edit()

        binding.buttonSave.setOnClickListener {
            val key = binding.editTextKey.text.toString()
            val value = binding.editTextValue.text.toString()
            editor.putString(key, value)
            editor.apply()
        }
        binding.buttonGetData.setOnClickListener {
            val key = binding.editTextGetValue.text.toString()
            val getValue = sharedPreferences.getString(key,"empty")
            binding.textView.text = getValue
        }
        binding.buttonRemove.setOnClickListener {
            val key = binding.editTextGetValue.text.toString()
            editor.remove(key)
            editor.apply()
        }
        binding.buttonClear.setOnClickListener {
            editor.clear()
            editor.apply()
        }
    }
}