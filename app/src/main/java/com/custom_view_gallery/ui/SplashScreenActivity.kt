package com.custom_view_gallery.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.custom_view_gallery.R
import com.custom_view_gallery.databinding.ActivitySplashScreenBinding

class SplashScreenActivity : AppCompatActivity() {
    private lateinit var binding:ActivitySplashScreenBinding
    private val SPLASH_DELAY: Long = 3000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        moveToScreen()
    }


    private fun moveToScreen() {
        Handler(Looper.getMainLooper())
            .postDelayed({
                startActivity(Intent(this,HomeActivity::class.java))
                finish()

            }, SPLASH_DELAY)

    }

}