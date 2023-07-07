package com.example.servivelog.di

import android.content.Context
import androidx.room.Room
import com.example.servivelog.data.database.ServiceLogRoom
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    const val SERVICELOG_DATABASE_NAME = "serviceLog"

    @Singleton
    @Provides
    fun provideRoom(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, ServiceLogRoom::class.java, SERVICELOG_DATABASE_NAME).build()

    @Singleton
    @Provides
    fun providesComputerDao(db: ServiceLogRoom) = db.computerDao()

    @Singleton
    @Provides
    fun providesDiagnosisDao(db: ServiceLogRoom) = db.diagnosisDao()

    @Singleton
    @Provides
    fun providesDLabDao(db: ServiceLogRoom) = db.labDao()

    @Singleton
    @Provides
    fun providesMantenimientoDao(db: ServiceLogRoom) = db.mantenimientoDao()

    @Singleton
    @Provides
    fun providesUserDao(db: ServiceLogRoom) = db.userDao()

    @Singleton
    @Provides
    fun providesTipoMantenimientoDao(db: ServiceLogRoom) = db.tipoMantenimientoDao()
}