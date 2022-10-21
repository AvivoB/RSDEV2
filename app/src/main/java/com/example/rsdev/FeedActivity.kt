package com.example.rsdev

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class FeedActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.actionBar?.hide()
        setContentView(R.layout.activity_feed)
    }
}