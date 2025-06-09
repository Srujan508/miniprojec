package com.example.miniproject.di

import android.content.Context
import com.example.miniproject.data.local.AppDatabase
import com.example.miniproject.data.local.MedicineDao
import com.example.miniproject.data.repository.AuthRepository
import com.example.miniproject.data.repository.MedicineRepository
import com.example.miniproject.service.ChatbotService
import com.example.miniproject.service.NotificationService
import com.example.miniproject.util.ReminderScheduler
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = Firebase.auth

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase = AppDatabase.getDatabase(context)

    @Provides
    @Singleton
    fun provideMedicineDao(database: AppDatabase): MedicineDao = database.medicineDao()

    @Provides
    @Singleton
    fun provideAuthRepository(
        auth: FirebaseAuth
    ): AuthRepository = AuthRepository(auth)

    @Provides
    @Singleton
    fun provideMedicineRepository(
        medicineDao: MedicineDao
    ): MedicineRepository = MedicineRepository(medicineDao)

    @Provides
    @Singleton
    fun provideChatbotService(): ChatbotService = ChatbotService()

    @Provides
    @Singleton
    fun provideNotificationService(
        @ApplicationContext context: Context
    ): NotificationService = NotificationService(context)

    @Provides
    @Singleton
    fun provideReminderScheduler(
        @ApplicationContext context: Context
    ): ReminderScheduler = ReminderScheduler(context)
} 