package com.example.flashcard

import android.app.Application
import com.example.flashcard.data.AppContainer
import com.example.flashcard.data.AppDataContainer

class FlashcardApplication : Application() {
    /**
     * AppContainer instance used by the rest of classes to obtain dependencies
     */
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}