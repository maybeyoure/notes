package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity

open class ActivityWithoutBack: AppCompatActivity() {
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        //super.onBackPressed()
    }
}