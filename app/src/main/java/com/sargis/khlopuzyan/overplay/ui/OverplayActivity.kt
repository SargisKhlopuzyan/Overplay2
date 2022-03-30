package com.sargis.khlopuzyan.overplay.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sargis.khlopuzyan.overplay.R

class OverplayActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_overplay)

        if (supportFragmentManager.findFragmentById(R.id.container) == null) {
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.container, OverplayFragment.newInstance())
                .commit()
        }
    }

}