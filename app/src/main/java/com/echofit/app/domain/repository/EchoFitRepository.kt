package com.echofit.app.domain.repository

import com.echofit.app.domain.model.*
import kotlinx.coroutines.flow.Flow

interface EchoFitRepository {

    // Profile functions
    fun getUserProfile(): Flow<UserProfile?>
    suspend fun saveUserProfile(profile: UserProfile)
    suspend fun clearProfile()

    // Meal functions
    fun getAllMeals(): Flow<List<MealLog>>
    suspend fun addMeal(meal: MealLog)
    suspend fun removeMeal(meal: MealLog)

    // Water functions
    fun getAllWater(): Flow<List<WaterLog>>
    suspend fun addWater(water: WaterLog)
    suspend fun clearWater()

    // Weight functions
    fun getAllWeights(): Flow<List<WeightLog>>
    suspend fun addWeight(weight: WeightLog)

    // Attendance functions
    fun getAllAttendance(): Flow<List<DailyAttendance>>
    suspend fun saveAttendance(attendance: DailyAttendance)
    suspend fun getAttendanceForDate(dateStr: String): DailyAttendance?

    // Photo functions
    fun getAllPhotos(): Flow<List<ProgressPhoto>>
    suspend fun addPhoto(photo: ProgressPhoto)
    suspend fun removePhoto(photo: ProgressPhoto)

    // Reminders
    fun getReminders(): Flow<List<ReminderSetting>>
    suspend fun saveReminder(reminder: ReminderSetting)

    // Chat History
    fun getChatMessages(): Flow<List<ChatMessage>>
    suspend fun addChatMessage(message: ChatMessage)
    suspend fun clearChat()

    // AI Integrations
    suspend fun getAIResponse(messageHistory: List<ChatMessage>, message: String, apiKey: String?): String
    suspend fun analyzeMealPhoto(imageBytes: ByteArray, apiKey: String?): MealLog
    suspend fun analyzeBodyProgress(beforeBytes: ByteArray, afterBytes: ByteArray, apiKey: String?): String

    // Firebase Sync
    suspend fun syncLocalDataToCloud()
}
