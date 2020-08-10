package com.hfad.ideasworld

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class SplashSreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_sreen)
        val background= object: Thread() {
            override fun run() {
                try {
                    sleep(1000)
                    val splashIntent= Intent(baseContext,MainActivity::class.java)
                    startActivity(splashIntent)
                    finish()
                }catch (e:Exception){
                    e.printStackTrace()
                }

            }
        }
        background.start()
    }

    override fun onRestart() {
        super.onRestart()
        val splashIntent= Intent(baseContext,MainActivity::class.java)
        startActivity(splashIntent)
        finish()
    }
}