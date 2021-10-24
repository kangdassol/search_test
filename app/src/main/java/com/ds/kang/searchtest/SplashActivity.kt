package com.ds.kang.searchtest

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper

class SplashActivity : Activity() {
    private val SPLASH_DELAY_MS = 1000L

    private val mainHandler: Handler = Handler(Looper.getMainLooper())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.splash)

        mainHandler.postDelayed({
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)

            finish()
        }, SPLASH_DELAY_MS)
    }

    override fun onDestroy() {
        super.onDestroy()
        mainHandler.removeCallbacksAndMessages(null)
    }
}