package com.example.servivelog

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp //Esto genera all el codigo por detras, haciendo el uso de dagger hilt super facil
class MyApp: Application() //Extiende meramente de la app