package com.echofit.app.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.echofit.app.EchoFitApplication
import com.echofit.app.domain.ai.EchoAIEngine
import com.echofit.app.domain.model.*
import com.echofit.app.domain.repository.EchoFitRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: EchoFitRepository = (application as EchoFitApplication).repository

    // UI Navigation state
    var selectedTab = MutableStateFlow("Dashboard")

    // Core Domain data flows
    val userProfile = repository.getUserProfile().stateIn(viewModelScope, SharingStarted.Eagerly, null)
    val meals = repository.getAllMeals().stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
    val waterLogs = repository.getAllWater().stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
    val weightLogs = repository.getAllWeights().stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
    val attendanceLogs = repository.getAllAttendance().stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
    val progressPhotos = repository.getAllPhotos().stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
    val chatMessages = repository.getChatMessages().stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
    val reminders = repository.getReminders().stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    // Generative API config
    var geminiApiKey = MutableStateFlow("AQ.Ab8RN6IYtxEZGSH4RXFx8v3scLiT0jY3tjaJ-L4qFwTaaywi-A")

    // Calculations based on loaded profile
    val goalAnalysis: StateFlow<GoalAnalysis?> = userProfile.map { profile ->
        profile?.let { EchoAIEngine.analyzeGoal(it) }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, null)

    val currentDietPlan: StateFlow<DietPlan?> = combine(userProfile, goalAnalysis) { profile, analysis ->
        if (profile != null && analysis != null) {
            EchoAIEngine.generateDietPlan(profile, analysis)
        } else null
    }.stateIn(viewModelScope, SharingStarted.Eagerly, null)

    val currentWorkoutPlan: StateFlow<WorkoutPlan?> = userProfile.map { profile ->
        profile?.let { EchoAIEngine.generateWorkoutPlan(it) }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, null)

    init {
        // Pre-populate standard reminders on first launch
        viewModelScope.launch {
            repository.getReminders().first().let { current ->
                val list = if (current.isEmpty()) {
                    val defaults = listOf(
                        ReminderSetting("breakfast", "Breakfast Reminder", "08:30 AM", true),
                        ReminderSetting("lunch", "Lunch Reminder", "01:30 PM", true),
                        ReminderSetting("snack", "Evening Snack Reminder", "05:00 PM", true),
                        ReminderSetting("workout", "Workout Gym/Home Time", "07:00 PM", true),
                        ReminderSetting("dinner", "Dinner Reminder", "08:30 PM", true)
                    )
                    defaults.forEach { repository.saveReminder(it) }
                    defaults
                } else current

                list.forEach {
                    com.echofit.app.data.receiver.ReminderScheduler.scheduleReminder(getApplication(), it)
                }
            }
        }
    }

    // --- Profile Actions ---
    fun saveProfile(profile: UserProfile) {
        viewModelScope.launch {
            repository.saveUserProfile(profile)
        }
    }

    // --- Meal Actions ---
    fun logMeal(name: String, portion: String, calories: Int, protein: Int, carbs: Int, fats: Int, fiber: Int, uri: String? = null) {
        viewModelScope.launch {
            repository.addMeal(
                MealLog(
                    foodName = name,
                    portionSize = portion,
                    calories = calories,
                    protein = protein,
                    carbs = carbs,
                    fats = fats,
                    fiber = fiber,
                    photoUri = uri
                )
            )
            updateAttendanceToday { it.copy(isDietDone = true, isMealPhotosUploaded = uri != null) }
        }
    }

    fun removeMeal(meal: MealLog) {
        viewModelScope.launch {
            repository.removeMeal(meal)
        }
    }

    // --- Water Actions ---
    fun addWaterGlass() {
        viewModelScope.launch {
            repository.addWater(WaterLog(glasses = 1, liters = 0.25))
            updateAttendanceToday { it.copy(isWaterDone = true) }
        }
    }

    fun clearWater() {
        viewModelScope.launch {
            repository.clearWater()
        }
    }

    // --- Weight Actions ---
    fun logWeight(weight: Double) {
        viewModelScope.launch {
            repository.addWeight(WeightLog(weight = weight))
            updateAttendanceToday { it.copy(isWeightLogged = true) }
        }
    }

    // --- Progress Photo Actions ---
    fun logProgressPhoto(position: String, uri: String) {
        viewModelScope.launch {
            repository.addPhoto(ProgressPhoto(position = position, photoUri = uri))
            updateAttendanceToday { it.copy(isMealPhotosUploaded = true) }
        }
    }

    fun deletePhoto(photo: ProgressPhoto) {
        viewModelScope.launch {
            repository.removePhoto(photo)
        }
    }

    // --- Attendance and Streak helper ---
    private fun getTodayDateString(): String {
        return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    }

    private suspend fun updateAttendanceToday(update: (DailyAttendance) -> DailyAttendance) {
        val todayStr = getTodayDateString()
        val current = repository.getAttendanceForDate(todayStr) ?: DailyAttendance(dateString = todayStr)
        val updated = update(current)
        
        // Calculate daily score based on ticks
        var ticks = 0
        if (updated.isDietDone) ticks += 15
        if (updated.isWorkoutDone) ticks += 25
        if (updated.isWaterDone) ticks += 15
        if (updated.isProteinDone) ticks += 15
        if (updated.isSleepTracked) ticks += 10
        if (updated.isMealPhotosUploaded) ticks += 10
        if (updated.isWeightLogged) ticks += 10
        
        val scored = updated.copy(score = ticks.coerceAtMost(100))
        repository.saveAttendance(scored)
    }

    fun markWorkoutCompleted() {
        viewModelScope.launch {
            updateAttendanceToday { it.copy(isWorkoutDone = true) }
        }
    }

    // --- AI Assistant Actions ---
    fun sendMessageToEcho(text: String) {
        viewModelScope.launch {
            // Append User message
            val userMsg = ChatMessage(text = text, isUser = true)
            repository.addChatMessage(userMsg)

            // Prompt Echo
            val history = chatMessages.value
            val responseText = repository.getAIResponse(history, text, geminiApiKey.value)
            
            // Append Echo Response
            val echoMsg = ChatMessage(text = responseText, isUser = false)
            repository.addChatMessage(echoMsg)
        }
    }

    fun clearChat() {
        viewModelScope.launch {
            repository.clearChat()
        }
    }

    // --- Reminders configuration ---
    fun updateReminderSetting(id: String, isEnabled: Boolean) {
        viewModelScope.launch {
            val list = reminders.value
            list.find { it.id == id }?.let {
                val updated = it.copy(isEnabled = isEnabled)
                repository.saveReminder(updated)
                com.echofit.app.data.receiver.ReminderScheduler.scheduleReminder(getApplication(), updated)
            }
        }
    }

    fun syncLocalDataToCloud() {
        viewModelScope.launch {
            repository.syncLocalDataToCloud()
        }
    }
}
