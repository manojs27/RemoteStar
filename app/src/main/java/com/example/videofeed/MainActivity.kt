package com.example.videofeed

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.videofeed.ui.home.HomeFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, HomeFragment())
                .commitNow()

        }
    }

    override fun onBackPressed() {
        val fragment = this.supportFragmentManager.findFragmentById(R.id.container)
        (fragment as? HomeFragment)?.let {
            super.onBackPressed()
        }
    }


}
