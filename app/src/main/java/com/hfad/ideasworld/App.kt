package com.hfad.ideasworld

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App: Application()
{
    companion object{
        private lateinit var _context: Context
        val context:Context get() = _context
    }
    override fun onCreate() {
        super.onCreate()
        _context=this
    }
}