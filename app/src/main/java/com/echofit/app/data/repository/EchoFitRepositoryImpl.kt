package com.echofit.app.data.repository

import android.content.Context
import com.echofit.app.data.local.*
import com.echofit.app.data.local.entity.*
import com.echofit.app.data.remote.FirebaseSyncManager
import com.echofit.app.data.remote.GeminiApiClient
import com.echofit.app.domain.ai.EchoAIEngine
import com.echofit.app.domain.ai.OfflineAIEngine
import com.echofit.app.domain.model.*
import com.echofit.app.domain.repository.EchoFitRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class EchoFitRepositoryImpl(
    private val userProfileDao: com.echofit.app.data.local.UserProfileDao,
    private val dailyLogDao: com.echofit.app.data.local.DailyLogDao,
    private val dietWorkoutDao: com.echofit.app.data.local.DietWorkoutDao,
    private val context: Context
) : EchoFitRepository {

    // --- Profile Functions ---
    override fun getUserProfile(): Flow<UserProfile?> {
        return userProfileDao.getUserProfileFlow().map { entity ->
            entity?.let {
                UserProfile(
                    name = it.name,
                    age = it.age,
                    gender = it.gender,
                    height = it.height,
                    weight = it.weight,
                    targetWeight = it.targetWeight,
                    fitnessGoal = it.fitnessGoal,
                    budgetLevel = it.budgetLevel,
                    dietType = it.dietType,
                    gymAccess = it.gymAccess,
                    experienceLevel = it.experienceLevel,
                    preferredRestDay = it.preferredRestDay,
                    workoutTime = it.workoutTime,
                    mealTimings = MealTimings(
                        breakfast = it.breakfastTime,
                        lunch = it.lunchTime,
                        snack = it.snackTime,
                        dinner = it.dinnerTime
                    ),
                    injuriesOrLimitations = it.injuriesOrLimitations
                )
            }
        }
    }

    override suspend fun saveUserProfile(profile: UserProfile) {
        val entity = UserProfileEntity(
            name = profile.name,
            age = profile.age,
            gender = profile.gender,
            height = profile.height,
            weight = profile.weight,
            targetWeight = profile.targetWeight,
            fitnessGoal = profile.fitnessGoal,
            budgetLevel = profile.budgetLevel,
            dietType = profile.dietType,
            gymAccess = profile.gymAccess,
            experienceLevel = profile.experienceLevel,
            preferredRestDay = profile.preferredRestDay,
            workoutTime = profile.workoutTime,
            breakfastTime = profile.mealTimings.breakfast,
            lunchTime = profile.mealTimings.lunch,
            snackTime = profile.mealTimings.snack,
            dinnerTime = profile.mealTimings.dinner,
            injuriesOrLimitations = profile.injuriesOrLimitations
        )
        userProfileDao.insertOrUpdateProfile(entity)
        if (FirebaseSyncManager.isUserLoggedIn) {
            FirebaseSyncManager.syncProfileToCloud(entity)
        }
    }

    override suspend fun clearProfile() {
        userProfileDao.clearProfile()
    }

    // --- Meal Functions ---
    override fun getAllMeals(): Flow<List<MealLog>> {
        return dailyLogDao.getAllMealsFlow().map { list ->
            list.map {
                MealLog(
                    id = it.id,
                    foodName = it.foodName,
                    portionSize = it.portionSize,
                    calories = it.calories,
                    protein = it.protein,
                    carbs = it.carbs,
                    fats = it.fats,
                    fiber = it.fiber,
                    photoUri = it.photoUri,
                    timestamp = it.timestamp
                )
            }
        }
    }

    override suspend fun addMeal(meal: MealLog) {
        dailyLogDao.insertMeal(
            MealLogEntity(
                foodName = meal.foodName,
                portionSize = meal.portionSize,
                calories = meal.calories,
                protein = meal.protein,
                carbs = meal.carbs,
                fats = meal.fats,
                fiber = meal.fiber,
                photoUri = meal.photoUri,
                timestamp = meal.timestamp
            )
        )
    }

    override suspend fun removeMeal(meal: MealLog) {
        dailyLogDao.deleteMeal(
            MealLogEntity(
                id = meal.id,
                foodName = meal.foodName,
                portionSize = meal.portionSize,
                calories = meal.calories,
                protein = meal.protein,
                carbs = meal.carbs,
                fats = meal.fats,
                fiber = meal.fiber,
                photoUri = meal.photoUri,
                timestamp = meal.timestamp
            )
        )
    }

    // --- Water Functions ---
    override fun getAllWater(): Flow<List<WaterLog>> {
        return dailyLogDao.getAllWaterFlow().map { list ->
            list.map {
                WaterLog(
                    id = it.id,
                    glasses = it.glasses,
                    liters = it.liters,
                    timestamp = it.timestamp
                )
            }
        }
    }

    override suspend fun addWater(water: WaterLog) {
        dailyLogDao.insertWater(
            WaterLogEntity(
                glasses = water.glasses,
                liters = water.liters,
                timestamp = water.timestamp
            )
        )
    }

    override suspend fun clearWater() {
        dailyLogDao.clearWaterLogs()
    }

    // --- Weight Functions ---
    override fun getAllWeights(): Flow<List<WeightLog>> {
        return dailyLogDao.getAllWeightsFlow().map { list ->
            list.map {
                WeightLog(
                    id = it.id,
                    weight = it.weight,
                    timestamp = it.timestamp
                )
            }
        }
    }

    override suspend fun addWeight(weight: WeightLog) {
        dailyLogDao.insertWeight(
            WeightLogEntity(
                weight = weight.weight,
                timestamp = weight.timestamp
            )
        )
    }

    // --- Attendance Functions ---
    override fun getAllAttendance(): Flow<List<DailyAttendance>> {
        return dailyLogDao.getAllAttendanceFlow().map { list ->
            list.map {
                DailyAttendance(
                    dateString = it.dateString,
                    isDietDone = it.isDietDone,
                    isWorkoutDone = it.isWorkoutDone,
                    isWaterDone = it.isWaterDone,
                    isProteinDone = it.isProteinDone,
                    isSleepTracked = it.isSleepTracked,
                    isMealPhotosUploaded = it.isMealPhotosUploaded,
                    isWeightLogged = it.isWeightLogged,
                    score = it.score,
                    notes = it.notes
                )
            }
        }
    }

    override suspend fun getAttendanceForDate(dateStr: String): DailyAttendance? {
        val ent = dailyLogDao.getAttendanceForDate(dateStr) ?: return null
        return DailyAttendance(
            dateString = ent.dateString,
            isDietDone = ent.isDietDone,
            isWorkoutDone = ent.isWorkoutDone,
            isWaterDone = ent.isWaterDone,
            isProteinDone = ent.isProteinDone,
            isSleepTracked = ent.isSleepTracked,
            isMealPhotosUploaded = ent.isMealPhotosUploaded,
            isWeightLogged = ent.isWeightLogged,
            score = ent.score,
            notes = ent.notes
        )
    }

    override suspend fun saveAttendance(attendance: DailyAttendance) {
        dailyLogDao.insertOrReplaceAttendance(
            AttendanceEntity(
                dateString = attendance.dateString,
                isDietDone = attendance.isDietDone,
                isWorkoutDone = attendance.isWorkoutDone,
                isWaterDone = attendance.isWaterDone,
                isProteinDone = attendance.isProteinDone,
                isSleepTracked = attendance.isSleepTracked,
                isMealPhotosUploaded = attendance.isMealPhotosUploaded,
                isWeightLogged = attendance.isWeightLogged,
                score = attendance.score,
                notes = attendance.notes
            )
        )
    }

    // --- Photo Functions ---
    override fun getAllPhotos(): Flow<List<ProgressPhoto>> {
        return dailyLogDao.getAllPhotosFlow().map { list ->
            list.map {
                ProgressPhoto(
                    id = it.id,
                    position = it.position,
                    photoUri = it.photoUri,
                    timestamp = it.timestamp
                )
            }
        }
    }

    override suspend fun addPhoto(photo: ProgressPhoto) {
        dailyLogDao.insertPhoto(
            ProgressPhotoEntity(
                position = photo.position,
                photoUri = photo.photoUri,
                timestamp = photo.timestamp
            )
        )
    }

    override suspend fun removePhoto(photo: ProgressPhoto) {
        dailyLogDao.deletePhoto(
            ProgressPhotoEntity(
                id = photo.id,
                position = photo.position,
                photoUri = photo.photoUri,
                timestamp = photo.timestamp
            )
        )
    }

    // --- Reminders ---
    override fun getReminders(): Flow<List<ReminderSetting>> {
        return dietWorkoutDao.getRemindersFlow().map { list ->
            list.map {
                ReminderSetting(
                    id = it.id,
                    name = it.name,
                    time = it.time,
                    isEnabled = it.isEnabled
                )
            }
        }
    }

    override suspend fun saveReminder(reminder: ReminderSetting) {
        dietWorkoutDao.insertOrReplaceReminder(
            ReminderEntity(
                id = reminder.id,
                name = reminder.name,
                time = reminder.time,
                isEnabled = reminder.isEnabled
            )
        )
    }

    // --- Chat History ---
    override fun getChatMessages(): Flow<List<ChatMessage>> {
        return dietWorkoutDao.getChatHistoryFlow().map { list ->
            list.map {
                ChatMessage(
                    id = it.id,
                    text = it.text,
                    isUser = it.isUser,
                    timestamp = it.timestamp
                )
            }
        }
    }

    override suspend fun addChatMessage(message: ChatMessage) {
        dietWorkoutDao.insertChatMessage(
            ChatMessageEntity(
                id = message.id,
                text = message.text,
                isUser = message.isUser,
                timestamp = message.timestamp
            )
        )
    }

    override suspend fun clearChat() {
        dietWorkoutDao.clearChatHistory()
    }

    // --- AI Client Calls ---
    private suspend fun getOfflineResponse(message: String): String {
        val profileEntity = userProfileDao.getUserProfile()
        val userProfile = profileEntity?.let {
            UserProfile(
                name = it.name,
                age = it.age,
                gender = it.gender,
                height = it.height,
                weight = it.weight,
                targetWeight = it.targetWeight,
                fitnessGoal = it.fitnessGoal,
                budgetLevel = it.budgetLevel,
                dietType = it.dietType,
                gymAccess = it.gymAccess,
                experienceLevel = it.experienceLevel,
                preferredRestDay = it.preferredRestDay,
                workoutTime = it.workoutTime,
                mealTimings = MealTimings(
                    breakfast = it.breakfastTime,
                    lunch = it.lunchTime,
                    snack = it.snackTime,
                    dinner = it.dinnerTime
                ),
                injuriesOrLimitations = it.injuriesOrLimitations
            )
        } ?: UserProfile()

        val todayStr = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val attendance = dailyLogDao.getAttendanceForDate(todayStr) ?: AttendanceEntity(
            dateString = todayStr,
            isDietDone = false,
            isWorkoutDone = false,
            isWaterDone = false,
            isProteinDone = false,
            isSleepTracked = false,
            isMealPhotosUploaded = false,
            isWeightLogged = false,
            score = 0,
            notes = ""
        )
        
        val todayMeals = dailyLogDao.getAllMealsFlow().first().filter {
            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(it.timestamp)) == todayStr
        }
        
        val todayWater = dailyLogDao.getAllWaterFlow().first().filter {
            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(it.timestamp)) == todayStr
        }

        return OfflineAIEngine.generateResponse(
            newMessage = message,
            profile = userProfile,
            attendance = DailyAttendance(
                dateString = attendance.dateString,
                isDietDone = attendance.isDietDone,
                isWorkoutDone = attendance.isWorkoutDone,
                isWaterDone = attendance.isWaterDone,
                isProteinDone = attendance.isProteinDone,
                isSleepTracked = attendance.isSleepTracked,
                isMealPhotosUploaded = attendance.isMealPhotosUploaded,
                isWeightLogged = attendance.isWeightLogged,
                score = attendance.score,
                notes = attendance.notes
            ),
            todayMeals = todayMeals.map {
                MealLog(
                    id = it.id,
                    foodName = it.foodName,
                    portionSize = it.portionSize,
                    calories = it.calories,
                    protein = it.protein,
                    carbs = it.carbs,
                    fats = it.fats,
                    fiber = it.fiber,
                    timestamp = it.timestamp
                )
            },
            todayWaterLitres = todayWater.sumOf { it.liters }
        )
    }

    override suspend fun getAIResponse(
        messageHistory: List<ChatMessage>,
        message: String,
        apiKey: String?
    ): String {
        if (apiKey.isNullOrBlank() || (!apiKey.startsWith("AIza") && !apiKey.startsWith("AQ."))) {
            return getOfflineResponse(message)
        }
        return try {
            val response = GeminiApiClient.chatWithEcho(messageHistory, message, apiKey)
            if (response.startsWith("Echo hit a bump:") || response.contains("quota", ignoreCase = true) || response.contains("QuotaExceededException")) {
                getOfflineResponse(message)
            } else {
                response
            }
        } catch (e: Exception) {
            getOfflineResponse(message)
        }
    }

    override suspend fun analyzeMealPhoto(imageBytes: ByteArray, apiKey: String?): MealLog {
        return GeminiApiClient.analyzeMealPhoto(imageBytes, apiKey)
    }

    override suspend fun analyzeBodyProgress(
        beforeBytes: ByteArray,
        afterBytes: ByteArray,
        apiKey: String?
    ): String {
        return GeminiApiClient.compareBodyProgressPhotos(beforeBytes, afterBytes, apiKey)
    }

    // --- Cloud Sync ---
    override suspend fun syncLocalDataToCloud() {
        if (!FirebaseSyncManager.isUserLoggedIn) return
        try {
            // Sync user profile
            val prof = userProfileDao.getUserProfile()
            if (prof != null) {
                FirebaseSyncManager.syncProfileToCloud(prof)
            }
            // Sync attendance
            val attend = dailyLogDao.getAllAttendanceFlow().first()
            FirebaseSyncManager.syncAttendanceToCloud(attend)
            // Sync water logs
            val water = dailyLogDao.getAllWaterFlow().first()
            FirebaseSyncManager.syncWaterLogsToCloud(water)
            // Sync weight logs
            val weights = dailyLogDao.getAllWeightsFlow().first()
            FirebaseSyncManager.syncWeightLogsToCloud(weights)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
