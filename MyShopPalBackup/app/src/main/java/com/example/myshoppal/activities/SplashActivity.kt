package com.example.myshoppal.activities

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowInsets
import android.view.WindowManager
import com.example.myshoppal.R

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        @Suppress("DEPRECATION")
        Handler().postDelayed(
            {
                //Launch Main Activity
                print("a")
                startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
                finish() //Call this when your activity is done and should be closed
            }, 1500

        )



//        val typeFace: Typeface = Typeface.createFromAsset(assets, "Montserrat-Bold.ttf");
//        val textView: TextView = findViewById(R.id.tv_app_name) as TextView
//        textView.typeface = typeFace

    }
}