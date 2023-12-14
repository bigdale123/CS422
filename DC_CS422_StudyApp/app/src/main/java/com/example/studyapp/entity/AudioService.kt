package com.example.studyapp.entity

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import com.example.studyapp.R

class AudioService: Service() {
    private lateinit var player: MediaPlayer
    var media = false

    override fun onCreate() {
        super.onCreate()
        player = MediaPlayer.create(this, R.raw.music)
        player.isLooping = true
    }

    override fun onBind(p0: Intent): IBinder {
        return AudioBinder()
    }

    fun start(){
        player.start()
    }

    fun pause(){
        player.pause()
    }

    inner class AudioBinder: Binder() {
        fun getService() = this@AudioService
    }
}