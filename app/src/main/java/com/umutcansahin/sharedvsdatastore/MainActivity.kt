package com.umutcansahin.sharedvsdatastore

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import com.umutcansahin.sharedvsdatastore.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.io.IOException


const val SHARED_NAME = "myPref"
const val NAME = "name"
const val AGE = "age"
const val IS_ADMIN = "isAdmin"

// At the top level of your kotlin file:
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "myDataStore")

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
            val getValue = sharedPreferences.getString(key, "empty")
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

    /** SAVE DATA WİTH SHARED DATA STORE*/
    private suspend fun saveDataWithSharedDataStore(key: String, value: String) {
        val dataStoreKey = stringPreferencesKey(key)
        dataStore.edit { preferences ->
            preferences[dataStoreKey] = value
        }
    }

    /**READ DATA FROM SHARED DATA STORE*/
    private suspend fun readDataFromSharedDataStore(key: String): String {
        val dataStoreKey = stringPreferencesKey(key)
        val result = dataStore.data.first()
        return result[dataStoreKey] ?: "empty"
    }

    /**REMOVE DATA FROM SHARED DATA STORE*/
    private suspend fun removeDataFromSharedDataStore(key: String) {
        val dataStoreKey = stringPreferencesKey(key)
        dataStore.edit { preferences ->
            preferences.remove(dataStoreKey)
        }
    }

    /** CLEAR ALL DATA FROM SHARED DATA STORE*/
    private suspend fun clearAllDataFromSharedDataStore() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    /**UPDATE DATA FROM SHARED DATA STORE*/
    private suspend fun updateDataFromSharedDataStore(key: String, value: String) {
        val dataStoreKey = stringPreferencesKey(key)
        dataStore.edit { preferences ->
            preferences[dataStoreKey] = value
        }
    }

    private fun trySharedDataStore() {

        binding.buttonSave.setOnClickListener {
            lifecycleScope.launch {
                saveDataWithSharedDataStore(
                    binding.editTextKey.text.toString(),
                    binding.editTextValue.text.toString()
                )

            }
        }
        binding.buttonGetData.setOnClickListener {
            lifecycleScope.launch {
                val value =
                    readDataFromSharedDataStoreWithTryCatch(binding.editTextGetValue.text.toString())
                binding.textView.text = value
            }
        }
        binding.buttonRemove.setOnClickListener {
            lifecycleScope.launch {
                removeDataFromSharedDataStore(binding.editTextGetValue.text.toString())
            }
        }
        binding.buttonClear.setOnClickListener {
            lifecycleScope.launch {
                clearAllDataFromSharedDataStore()
            }
        }
        binding.buttonUpdate.setOnClickListener {
            lifecycleScope.launch {
                updateDataFromSharedDataStore(
                    binding.editTextKey.text.toString(),
                    binding.editTextValue.text.toString()
                )
            }
        }
    }

    /**READ DATA FROM SHARED DATA STORE WITH TRY-CATCH*/
    private suspend fun readDataFromSharedDataStoreWithTryCatch(key: String): String {
        val dataStoreKey = stringPreferencesKey(key)
        val result = dataStore.data.catch { exception ->
            if (exception is IOException) {
                Log.e("TAG", "Error reading preferences.", exception)
                exception.toString()
            } else {
                exception.toString()
            }
        }.map { preferences ->
            preferences[dataStoreKey]
        }
        return result.first() ?: "empty"
    }

    /**READ DATA FROM SHARED DATA STORE WITH TRY-CATCH*/
    private suspend fun readDataFromSharedDataStoreWithTryCatch2(key: String): String {
        return try {
            val dataStoreKey = stringPreferencesKey(key)
            val result = dataStore.data.first()
            result[dataStoreKey] ?: "empty"
        } catch (e: Exception) {
            "handle error"
        }
    }

    /** SharedPreference dan dataStore gecis */
    private val Context.dataStore by preferencesDataStore(
        name = "USER_PREFERENCES_NAME",
        produceMigrations = { context ->
            listOf(SharedPreferencesMigration(context, "USER_PREFERENCES_NAME"))
        }
    )
}

