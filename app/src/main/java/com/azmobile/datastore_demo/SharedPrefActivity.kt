package com.azmobile.datastore_demo

import android.content.Context
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class SharedPrefActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shared_pref)

        findViewById<Button>(R.id.btnAdd).setOnClickListener {
            val sharedPref = getSharedPreferences(USER_PREFERENCES_NAME, Context.MODE_PRIVATE)
            with(sharedPref.edit()) {
                putString("name", "abcdefgh")
                apply()
            }
        }
    }
}
