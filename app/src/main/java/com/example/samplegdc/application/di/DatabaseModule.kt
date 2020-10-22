package com.example.samplegdc.application.di

import android.content.Context
import androidx.room.Room
import com.example.samplegdc.feature.data.local.TaskDao
import com.example.samplegdc.feature.database.GdcDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext appContext: Context): GdcDatabase =
        Room.databaseBuilder(
            appContext,
            GdcDatabase::class.java,
            GdcDatabase.DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()

    @Singleton
    @Provides
    fun provideTaskDao(gdcDatabase: GdcDatabase): TaskDao =
        gdcDatabase.taskDao()
}