package com.echofit.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey val id: String = "primary_profile",
    val name: String,
    val age: Int,
    val gender: String,
    val height: Double,
    val weight: Double,
    val targetWeight: Double,
    val fitnessGoal: String,
    val budgetLevel: String,
    val dietType: String,
    val gymAccess: String,
    val experienceLevel: String,
    val preferredRestDay: String,
    val workoutTime: String,
    val breakfastTime: String,
    val lunchTime: String,
    val snackTime: String,
    val dinnerTime: String,
    val injuriesOrLimitations: String
)

@Entity(tableName = "meal_logs")
data class MealLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val foodName: String,
    val portionSize: String,
    val calories: Int,
    val protein: Int,
    val carbs: Int,
    val fats: Int,
    val fiber: Int,
    val photoUri: String?,
    val timestamp: Long
)

@Entity(tableName = "water_logs")
data class WaterLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val glasses: Int,
    val liters: Double,
    val timestamp: Long
)

@Entity(tableName = "workout_logs")
data class WorkoutLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val routineName: String,
    val durationMinutes: Int,
    val caloriesBurned: Int,
    val completedExercisesJson: String, // Stored as serialised JSON
    val timestamp: Long
)

@Entity(tableName = "attendance")
data class AttendanceEntity(
    @PrimaryKey val dateString: String, // "YYYY-MM-DD"
    val isDietDone: Boolean,
    val isWorkoutDone: Boolean,
    val isWaterDone: Boolean,
    val isProteinDone: Boolean,
    val isSleepTracked: Boolean,
    val isMealPhotosUploaded: Boolean,
    val isWeightLogged: Boolean,
    val score: Int,
    val notes: String
)

@Entity(tableName = "weight_logs")
data class WeightLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val weight: Double,
    val timestamp: Long
)

@Entity(tableName = "progress_photos")
data class ProgressPhotoEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val position: String, // "Front", "Side", "Back"
    val photoUri: String,
    val timestamp: Long
)

@Entity(tableName = "reminders")
data class ReminderEntity(
    @PrimaryKey val id: String, // e.g. "breakfast", "lunch", "workout"
    val name: String,
    val time: String,
    val isEnabled: Boolean
)

@Entity(tableName = "chat_history")
data class ChatMessageEntity(
    @PrimaryKey val id: String,
    val text: String,
    val isUser: Boolean,
    val timestamp: Long
)
