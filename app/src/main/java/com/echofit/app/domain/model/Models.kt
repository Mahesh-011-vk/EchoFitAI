package com.echofit.app.domain.model

import java.util.Date

// Clean Architecture Domain models

data class UserProfile(
    val name: String = "",
    val age: Int = 25,
    val gender: String = "Male",
    val height: Double = 175.0, // in cm
    val weight: Double = 70.0,  // in kg
    val targetWeight: Double = 70.0,
    val fitnessGoal: String = "Fat Loss", // "Fat Loss", "Muscle Gain", "Body Recomposition", "General Fitness"
    val budgetLevel: String = "Medium", // "Low", "Medium", "High"
    val dietType: String = "Vegetarian", // "Vegetarian", "Eggetarian", "Non-Vegetarian", "Vegan"
    val gymAccess: String = "Both", // "Gym", "Home", "Both"
    val experienceLevel: String = "Beginner", // "Beginner", "Intermediate", "Advanced"
    val preferredRestDay: String = "Sunday",
    val workoutTime: String = "07:00 PM",
    val mealTimings: MealTimings = MealTimings(),
    val injuriesOrLimitations: String = "None"
)

data class MealTimings(
    val breakfast: String = "08:30 AM",
    val lunch: String = "01:30 PM",
    val snack: String = "05:00 PM",
    val dinner: String = "08:30 PM"
)

data class DietPlan(
    val targetCalories: Int,
    val targetProtein: Int,
    val targetCarbs: Int,
    val targetFats: Int,
    val targetFiber: Int,
    val meals: List<MealMeal>
)

data class MealMeal(
    val mealName: String, // Breakfast, Lunch, Snack, Dinner
    val time: String,
    val items: List<FoodItem>
)

data class FoodItem(
    val name: String,
    val portion: String,
    val calories: Int,
    val protein: Int,
    val carbs: Int,
    val fats: Int,
    val fiber: Int,
    val isChecked: Boolean = false,
    val isBudgetFriendly: Boolean = true
)

data class WorkoutPlan(
    val routineName: String, // Push, Pull, Legs, Core, Recovery
    val durationMinutes: Int,
    val caloriesEstimated: Int,
    val exercises: List<Exercise>
)

data class Exercise(
    val id: String,
    val name: String,
    val sets: Int,
    val reps: String,
    val targetMuscle: String,
    val targetEquipment: String,
    val instructions: List<String>,
    val commonMistakes: List<String>,
    val beginnerVariation: String,
    val advancedVariation: String,
    val isCompleted: Boolean = false
)

data class MealLog(
    val id: Long = 0,
    val foodName: String,
    val portionSize: String,
    val calories: Int,
    val protein: Int,
    val carbs: Int,
    val fats: Int,
    val fiber: Int,
    val photoUri: String? = null,
    val timestamp: Long = System.currentTimeMillis()
)

data class WaterLog(
    val id: Long = 0,
    val glasses: Int,
    val liters: Double,
    val timestamp: Long = System.currentTimeMillis()
)

data class DailyAttendance(
    val dateString: String, // "YYYY-MM-DD"
    val isDietDone: Boolean = false,
    val isWorkoutDone: Boolean = false,
    val isWaterDone: Boolean = false,
    val isProteinDone: Boolean = false,
    val isSleepTracked: Boolean = false,
    val isMealPhotosUploaded: Boolean = false,
    val isWeightLogged: Boolean = false,
    val score: Int = 0,
    val notes: String = ""
)

data class WeightLog(
    val id: Long = 0,
    val weight: Double,
    val timestamp: Long = System.currentTimeMillis()
)

data class ProgressPhoto(
    val id: Long = 0,
    val position: String, // "Front", "Side", "Back"
    val photoUri: String,
    val timestamp: Long = System.currentTimeMillis()
)

data class ReminderSetting(
    val id: String, // "breakfast", "lunch", "snack", "dinner", "water", "workout"
    val name: String,
    val time: String,
    val isEnabled: Boolean = true
)

data class ChatMessage(
    val id: String = java.util.UUID.randomUUID().toString(),
    val text: String,
    val isUser: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)

data class GoalAnalysis(
    val bmi: Double,
    val bmiCategory: String,
    val estimatedBodyFat: Double,
    val calorieTarget: Int,
    val proteinTarget: Int,
    val carbsTarget: Int,
    val fatsTarget: Int,
    val fiberTarget: Int,
    val waterIntakeTargetLiters: Double,
    val suggestedWeightGoal: Double,
    val thirtyDayMission: String,
    val weeklyGoals: List<String>,
    val mainFocusAreas: List<String>,
    val fitnessScore: Int,
    val consistencyTargetPercentage: Int
)
