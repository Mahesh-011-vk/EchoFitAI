package com.echofit.app.domain.ai

import com.echofit.app.domain.model.*
import kotlin.math.roundToInt

object EchoAIEngine {

    /**
     * Complete AI Analysis based on User profile parameters.
     */
    fun analyzeGoal(profile: UserProfile): GoalAnalysis {
        val heightM = profile.height / 100.0
        val bmi = (profile.weight / (heightM * heightM) * 10.0).roundToInt() / 10.0
        
        val bmiCategory = when {
            bmi < 18.5 -> "Underweight"
            bmi < 25.0 -> "Normal Weight"
            bmi < 30.0 -> "Overweight"
            else -> "Obese"
        }

        // Visual Body fat estimation adjusted by goal, gender and BMI
        val baseFat = if (profile.gender.equals("Male", ignoreCase = true)) {
            (1.20 * bmi) + (0.23 * profile.age) - 16.2
        } else {
            (1.20 * bmi) + (0.23 * profile.age) - 5.4
        }
        val estimatedBodyFat = (baseFat.coerceIn(5.0, 50.0) * 10.0).roundToInt() / 10.0

        // BMR (Harris-Benedict Equation)
        val bmr = if (profile.gender.equals("Male", ignoreCase = true)) {
            88.362 + (13.397 * profile.weight) + (4.799 * profile.height) - (5.677 * profile.age)
        } else {
            447.593 + (9.247 * profile.weight) + (3.098 * profile.height) - (4.330 * profile.age)
        }

        // TDEE estimate (Moderate activity assumed for fitness seekers)
        val tdee = bmr * 1.375

        // Calorie and Macro targets based on fitness goals
        val calorieTarget = when (profile.fitnessGoal) {
            "Fat Loss" -> (tdee - 500).roundToInt().coerceAtLeast(1200)
            "Muscle Gain" -> (tdee + 300).roundToInt()
            "Body Recomposition" -> tdee.roundToInt()
            else -> tdee.roundToInt() // General Fitness
        }

        // Protein Target: fat loss requires higher protein for muscle retention; weight lifters need more
        val proteinFactor = when {
            profile.fitnessGoal == "Muscle Gain" -> 2.0
            profile.fitnessGoal == "Fat Loss" -> 2.2
            else -> 1.6
        }
        val proteinTarget = (profile.weight * proteinFactor).roundToInt()

        // Calorie Allocation: 1g Protein = 4kcal, 1g Carb = 4kcal, 1g Fat = 9kcal
        val proteinKcal = proteinTarget * 4
        val fatKcal = (calorieTarget * 0.25).roundToInt() // 25% fats
        val fatsTarget = (fatKcal / 9).coerceAtLeast(40)
        
        val carbsKcal = calorieTarget - proteinKcal - fatKcal
        val carbsTarget = (carbsKcal / 4).coerceAtLeast(100)
        
        val fiberTarget = if (profile.gender.equals("Male", ignoreCase = true)) 35 else 25
        val waterIntakeTargetLiters = 3.0 + (if (profile.weight > 80) 1.0 else 0.0) + (if (profile.fitnessGoal == "Muscle Gain") 0.5 else 0.0)

        // Suggest weight goal based on height (BMI 22 ideal midpoint)
        val suggestedWeightGoal = (22.0 * heightM * heightM * 10.0).roundToInt() / 10.0

        val thirtyDayMission = when (profile.fitnessGoal) {
            "Fat Loss" -> "Mission Shred: Shed body fat while preserving lean mass. Focus on a strict calorie deficit, hit ${proteinTarget}g protein daily, and log 4 workouts per week."
            "Muscle Gain" -> "Mission Bulk: Create a clean surplus. Focus on heavy progressive workouts, sleeping 8 hours, and consuming ${calorieTarget} kcal daily."
            "Body Recomposition" -> "Mission Recomp: Build strength and burn fat simultaneously. Consistency is key: 100% attendance, hit protein targets, and do hybrid circuits."
            else -> "Mission Active: Establish consistent wellness habits. Track water intake daily, workout 3-4 times, and establish balanced food choices."
        }

        val weeklyGoals = listOf(
            "Complete 4 scheduled workouts.",
            "Average ${waterIntakeTargetLiters} liters of water daily.",
            "Stay within +/- 100 kcal of your ${calorieTarget} kcal daily calorie budget.",
            "Pre-plan meal nutrition using budget foods before eating."
        )

        val mainFocusAreas = mutableListOf<String>()
        if (profile.injuriesOrLimitations != "None") {
            mainFocusAreas.add("Injury adaptation & Joint preservation")
        }
        when (profile.fitnessGoal) {
            "Fat Loss" -> {
                mainFocusAreas.add("Calorie deficit compliance")
                mainFocusAreas.add("High volume protein sources")
                mainFocusAreas.add("Daily steps and cardio pacing")
            }
            "Muscle Gain" -> {
                mainFocusAreas.add("Progressive overload lifting")
                mainFocusAreas.add("Calorie surplus meal packing")
                mainFocusAreas.add("Optimal sleep recovery")
            }
            else -> {
                mainFocusAreas.add("Habit consistency")
                mainFocusAreas.add("Hydration pacing")
            }
        }

        // Fitness score calculation (1-100) based on starting metrics
        var fitnessScore = 50
        if (bmi in 18.5..24.9) fitnessScore += 20
        if (profile.experienceLevel == "Intermediate") fitnessScore += 10
        if (profile.experienceLevel == "Advanced") fitnessScore += 20
        if (profile.injuriesOrLimitations == "None") fitnessScore += 10

        return GoalAnalysis(
            bmi = bmi,
            bmiCategory = bmiCategory,
            estimatedBodyFat = estimatedBodyFat,
            calorieTarget = calorieTarget,
            proteinTarget = proteinTarget,
            carbsTarget = carbsTarget,
            fatsTarget = fatsTarget,
            fiberTarget = fiberTarget,
            waterIntakeTargetLiters = waterIntakeTargetLiters,
            suggestedWeightGoal = suggestedWeightGoal,
            thirtyDayMission = thirtyDayMission,
            weeklyGoals = weeklyGoals,
            mainFocusAreas = mainFocusAreas,
            fitnessScore = fitnessScore.coerceIn(10, 100),
            consistencyTargetPercentage = 85
        )
    }

    /**
     * Generates a fully dynamic budget-friendly diet plan
     */
    fun generateDietPlan(profile: UserProfile, analysis: GoalAnalysis): DietPlan {
        val isLowBudget = profile.budgetLevel.equals("Low", ignoreCase = true)
        val isMediumBudget = profile.budgetLevel.equals("Medium", ignoreCase = true)
        
        // Define protein sources based on Diet Type and Budget
        val mealSource = when (profile.dietType) {
            "Vegetarian" -> {
                if (isLowBudget) {
                    listOf("Roasted Chana", "Dal Tadka", "Sprouts Salad", "Tofu/Soya chunks", "Banana & Peanuts")
                } else {
                    listOf("Paneer Tikka", "Greek Yogurt", "Sprouted Dal", "Tofu Curry", "Whey Protein Shake")
                }
            }
            "Eggetarian" -> {
                if (isLowBudget) {
                    listOf("Boiled Eggs (4)", "Egg Bhurji", "Roasted Chana", "Milk", "Banana with Peanut Butter")
                } else {
                    listOf("Egg White Omelet (6)", "Paneer Sandwiches", "Sprouted Dal", "Egg Salad", "Peanut butter toast")
                }
            }
            "Vegan" -> {
                listOf("Tofu Salad", "Sprouts stir fry", "Roasted Chana", "Dal Fry", "Soya chunk pulao")
            }
            else -> { // Non-Vegetarian
                if (isLowBudget) {
                    listOf("Boiled Eggs (3)", "Chicken Breast (150g)", "Dal with Rice", "Roasted Chana", "Peanuts")
                } else {
                    listOf("Grilled Chicken (250g)", "Salmon / Fish Curry", "Egg White Frittata", "Paneer stir-fry", "Protein Shake")
                }
            }
        }

        // Design a typical day with 4 meal targets
        val cal = analysis.calorieTarget
        val prot = analysis.proteinTarget
        val carb = analysis.carbsTarget
        val fat = analysis.fatsTarget
        val fib = analysis.fiberTarget

        val breakfast = MealMeal(
            mealName = "Breakfast",
            time = profile.mealTimings.breakfast,
            items = listOf(
                FoodItem(
                    name = if (profile.dietType.contains("Veg")) "Sprouts & Banana Shake" else "Oatmeal & 3 Egg Whites",
                    portion = "1 Bowl / 3 Eggs",
                    calories = (cal * 0.25).roundToInt(),
                    protein = (prot * 0.25).roundToInt(),
                    carbs = (carb * 0.25).roundToInt(),
                    fats = (fat * 0.20).roundToInt(),
                    fiber = (fib * 0.25).roundToInt(),
                    isBudgetFriendly = true
                )
            )
        )

        val lunch = MealMeal(
            mealName = "Lunch",
            time = profile.mealTimings.lunch,
            items = listOf(
                FoodItem(
                    name = if (profile.dietType == "Vegetarian" || profile.dietType == "Vegan") "Tofu/Paneer Pulao & Dal Tadka" else "Grilled Chicken Breast with Rice & Dal",
                    portion = "1 Plate",
                    calories = (cal * 0.35).roundToInt(),
                    protein = (prot * 0.35).roundToInt(),
                    carbs = (carb * 0.35).roundToInt(),
                    fats = (fat * 0.30).roundToInt(),
                    fiber = (fib * 0.30).roundToInt(),
                    isBudgetFriendly = true
                )
            )
        )

        val snack = MealMeal(
            mealName = "Evening Snacks",
            time = profile.mealTimings.snack,
            items = listOf(
                FoodItem(
                    name = "Roasted Chana & Peanuts",
                    portion = "50g",
                    calories = (cal * 0.15).roundToInt(),
                    protein = (prot * 0.15).roundToInt(),
                    carbs = (carb * 0.15).roundToInt(),
                    fats = (fat * 0.20).roundToInt(),
                    fiber = (fib * 0.20).roundToInt(),
                    isBudgetFriendly = true
                )
            )
        )

        val dinner = MealMeal(
            mealName = "Dinner",
            time = profile.mealTimings.dinner,
            items = listOf(
                FoodItem(
                    name = if (profile.dietType.contains("Veg")) "Paneer/Soya chunks stir-fry with Roti" else "Fish / Chicken Curry with Chapati & Green Salad",
                    portion = "2 Chapatis + 150g Stir-fry",
                    calories = (cal * 0.25).roundToInt(),
                    protein = (prot * 0.25).roundToInt(),
                    carbs = (carb * 0.25).roundToInt(),
                    fats = (fat * 0.30).roundToInt(),
                    fiber = (fib * 0.25).roundToInt(),
                    isBudgetFriendly = true
                )
            )
        )

        return DietPlan(
            targetCalories = cal,
            targetProtein = prot,
            targetCarbs = carb,
            targetFats = fat,
            targetFiber = fib,
            meals = listOf(breakfast, lunch, snack, dinner)
        )
    }

    /**
     * Generates custom workout splits based on access, goal, and injuries
     */
    fun generateWorkoutPlan(profile: UserProfile, dayIndex: Int = 1): WorkoutPlan {
        val isHomeOnly = profile.gymAccess.equals("Home", ignoreCase = true)
        val isGymOnly = profile.gymAccess.equals("Gym", ignoreCase = true)
        
        val splitDay = dayIndex % 4 // 4 Day rotation: 1=Push, 2=Pull, 3=Legs, 4=Core/Active Rest
        
        val name = when (splitDay) {
            1 -> "Push Day"
            2 -> "Pull Day"
            3 -> "Leg Day"
            else -> "Core & Cardio"
        }

        val gymExercises = when (splitDay) {
            1 -> listOf(
                Exercise("g1", "Incline Dumbbell Press", 4, "8-12", "Upper Chest", "Dumbbells, Incline Bench", 
                    listOf("Set bench to 30 degrees", "Lower weights to upper chest slowly", "Press up without locking elbows"),
                    listOf("Setting incline too high, using front delts", "Flaring elbows 90 degrees"), "Standard Flat Pushups", "Incline Dumbbell Flyes"),
                Exercise("g2", "Overhead Barbell Press", 4, "6-10", "Shoulders", "Barbell",
                    listOf("Clean bar to shoulders", "Brace core and squeeze glutes", "Press bar straight overhead"),
                    listOf("Arching lower back excessively", "Bouncing bar off collarbone"), "Dumbbell Lateral Raises", "Handstand Pushups"),
                Exercise("g3", "Dumbbell Lateral Raises", 3, "12-15", "Side Delts", "Dumbbells",
                    listOf("Slight bend in elbows", "Raise weights to shoulder level", "Control deceleration"),
                    listOf("Using momentum to swing weights", "Lifting shoulders up"), "Resistance Band Lateral Raises", "Cable Lateral Raises"),
                Exercise("g4", "Triceps Cable Pushdowns", 3, "12-15", "Triceps", "Cable Machine",
                    listOf("Keep elbows pinned to ribcage", "Extend elbows downward completely", "Slowly return to start"),
                    listOf("Letting elbows flare or travel forward", "Hunching forward"), "Bench Dips", "Triceps Kickbacks")
            )
            2 -> listOf(
                Exercise("g5", "Pull-Ups", 4, "Max", "Lats / Back Width", "Pull-Up Bar",
                    listOf("Hang with palms facing away", "Pull collarbone to the bar", "Squeeze shoulder blades"),
                    listOf("Swinging legs for momentum", "Not doing full range of motion"), "Assisted Pull-ups / Lat Pulldown", "Weighted Pull-ups"),
                Exercise("g6", "Seated Cable Rows", 4, "10-12", "Mid Back", "Cable Machine, Bench",
                    listOf("Sit upright, pull handle to chest", "Squeeze shoulder blades", "Stretch lats back fully"),
                    listOf("Leaning forward and backward excessively", "Pulling with wrists"), "Dumbbell Rows", "Weighted Pull-ups"),
                Exercise("g7", "Barbell Biceps Curls", 3, "10-12", "Biceps", "Barbell",
                    listOf("Keep elbows pinned", "Curl bar to chest level", "Control the lowering phase"),
                    listOf("Swinging torso to cheat weight", "Short-changing lower range"), "Dumbbell Curls", "Spider Curls")
            )
            3 -> listOf(
                Exercise("g8", "Barbell Back Squats", 4, "6-10", "Quads & Glutes", "Barbell, Squat Rack",
                    listOf("Place bar on upper traps", "Hinge hips and squat down to parallel", "Drive up through mid-foot"),
                    listOf("Knees caving inward", "Heels lifting off ground"), "Goblet Squats", "Front Squats"),
                Exercise("g9", "Romanian Deadlifts", 4, "8-12", "Hamstrings & Glutes", "Barbell or Dumbbells",
                    listOf("Soft bend in knees", "Hinge hips back, sliding weight down thighs", "Feel hamstring stretch, return"),
                    listOf("Rounding lower back", "Squatting the weight down"), "Glute Bridges", "Deficit Deadlifts"),
                Exercise("g10", "Dumbbell Lunges", 3, "10 per leg", "Legs / Balance", "Dumbbells",
                    listOf("Step forward, lower rear knee near floor", "Keep front shin upright", "Push back to starting spot"),
                    listOf("Stepping in a straight tightrope line", "Knee shooting past toes"), "Bodyweight Lunges", "Bulgarian Split Squats")
            )
            else -> listOf(
                Exercise("g11", "Hanging Leg Raises", 3, "10-15", "Lower Abs", "Pull-up Bar",
                    listOf("Hang from bar, raise feet to waist", "Control swing", "Squeeze abs at top"),
                    listOf("Swinging back and forth", "Using hip flexors only"), "Lying Leg Raises", "Toes to Bar"),
                Exercise("g12", "Plank Hold", 3, "60 seconds", "Core Stability", "Floor",
                    listOf("Rest on forearms, brace body straight", "Squeeze glutes and core", "Keep neck neutral"),
                    listOf("Letting hips sag down", "Poking butt in the air"), "Plank on Knees", "Weighted Plank")
            )
        }

        val homeExercises = when (splitDay) {
            1 -> listOf(
                Exercise("h1", "Standard Pushups", 4, "15-20", "Chest & Triceps", "Floor",
                    listOf("Hands shoulder width", "Lower body in straight line", "Push floor away"),
                    listOf("Sagging lower back", "Flaring elbows outward"), "Incline Pushups (wall/chair)", "Decline Pushups"),
                Exercise("h2", "Decline Pushups", 4, "10-15", "Upper Chest", "Chair / Elevated Surface",
                    listOf("Elevate feet on chair", "Perform standard pushup motion", "Keep core braced tight"),
                    listOf("Allowing hips to drop", "Shrugging shoulders"), "Standard Pushups", "Handstand Pushups"),
                Exercise("h3", "Pike Pushups", 3, "8-12", "Shoulders", "Floor",
                    listOf("Hips high in V-shape", "Lower forehead towards ground between hands", "Push straight up"),
                    listOf("Flaring elbows out", "Rounding lower spine"), "Standard Pushups", "Handstand Pushups"),
                Exercise("h4", "Bench/Chair Dips", 3, "12-15", "Triceps", "Chair",
                    listOf("Place hands on chair edge", "Slide butt off, lower hips down", "Press up extending elbows"),
                    listOf("Letting body drift away from chair", "Going too deep hurting shoulders"), "Knee Pushups", "Weighted Dips")
            )
            2 -> listOf(
                Exercise("h5", "Backpack Rows", 4, "12-15", "Mid Back", "Backpack with weights",
                    listOf("Fill backpack with books/bottles", "Hinge at hips, pull pack to waist", "Slowly lower"),
                    listOf("Rounding lower spine", "Yanking weight up using shoulders"), "Towel Rows", "Weighted Backpack Pullups"),
                Exercise("h6", "Resistance Band Pulldowns", 4, "15-20", "Lats / Back Width", "Resistance Band",
                    listOf("Anchor band high on door", "Kneel down, pull handles to chest", "Squeeze lats"),
                    listOf("Letting bands snap back", "Bending chest forward"), "Towel Lat Pulls", "Standard Pull-ups"),
                Exercise("h7", "Backpack Biceps Curls", 3, "12-15", "Biceps", "Backpack",
                    listOf("Hold backpack handles", "Perform standard curl slowly", "Fully extend arms at bottom"),
                    listOf("Swinging elbows back", "Rushing lowering phase"), "Resistance Band Curls", "Concentration Curls")
            )
            3 -> listOf(
                Exercise("h8", "Air Squats", 4, "20-25", "Quads & Glutes", "Bodyweight",
                    listOf("Stand feet hip width", "Drop hips down past parallel", "Keep chest proud, push up"),
                    listOf("Knees buckling inwards", "Hunching upper back"), "Wall Sit", "Bulgarian Split Squats"),
                Exercise("h9", "Bulgarian Split Squats", 4, "12 per leg", "Quads & Hamstrings", "Chair",
                    listOf("Place back foot on chair behind you", "Squat down with front leg", "Drive front heel up"),
                    listOf("Front knee caving", "Leaning too far back"), "Bodyweight Lunges", "Weighted Split Squats"),
                Exercise("h10", "Glute Bridges", 3, "15-20", "Glutes & Hamstrings", "Floor",
                    listOf("Lie on back, knees bent", "Drive hips up squeezing glutes", "Slowly lower"),
                    listOf("Arching lower back instead of squeezing glutes", "Feet too far forward"), "Single Leg Bridges", "Weighted Glute Bridges")
            )
            else -> listOf(
                Exercise("h11", "Bicycle Crunches", 3, "20 reps", "Abs & Obliques", "Floor",
                    listOf("Lie flat, hands behind head", "Bring elbow to opposite knee while extending other leg", "Alternate fluidly"),
                    listOf("Pulling on neck with hands", "Rushing through reps"), "Lying Leg Raises", "V-Ups"),
                Exercise("h12", "Plank Hold", 3, "60 seconds", "Core Stability", "Floor",
                    listOf("Rest on elbows, form straight line", "Squeeze glutes and core", "Keep neck straight"),
                    listOf("Letting hips drop", "Holding breath"), "Plank on Knees", "Side Plank")
            )
        }

        // Apply adaptations if user has injuries
        val exercises = (if (isHomeOnly) homeExercises else gymExercises).map { ex ->
            if (profile.injuriesOrLimitations.contains("shoulder", ignoreCase = true) && ex.targetMuscle.contains("Shoulder")) {
                ex.copy(name = "Light Shoulder Mobility Circles", reps = "30s", instructions = listOf("Perform slow arm circles", "Avoid weight if pain exists"))
            } else if (profile.injuriesOrLimitations.contains("knee", ignoreCase = true) && ex.name.contains("Squat")) {
                ex.copy(name = "Glute Wall Sit", reps = "30s hold", instructions = listOf("Place back flat on wall", "Slide down to comfortable 45 degrees"))
            } else {
                ex
            }
        }

        return WorkoutPlan(
            routineName = name,
            durationMinutes = 45,
            caloriesEstimated = 250 + (profile.weight * 1.5).roundToInt(),
            exercises = exercises
        )
    }

    /**
     * Substitution Engine: Replaces an exercise depending on missing/unavailable items.
     */
    fun substituteExercise(exerciseName: String): String {
        return when {
            exerciseName.contains("Bench Press", ignoreCase = true) || exerciseName.contains("Chest Press", ignoreCase = true) -> 
                "No bench? Try Standard Floor Dumbbell Press or floor Push-ups!"
            exerciseName.contains("Dumbbell", ignoreCase = true) -> 
                "No dumbbells? Load a standard school Backpack with books or water bottles!"
            exerciseName.contains("Pull-Ups", ignoreCase = true) || exerciseName.contains("Lat Pulldown", ignoreCase = true) -> 
                "No pull-up bar? Try High-Anchor Resistance Band Rows or Door-Towel Rows!"
            exerciseName.contains("Leg Press", ignoreCase = true) -> 
                "No Leg Press? Try heavy Dumbbell Goblet Squats combined with Bulgarian Split Squats!"
            exerciseName.contains("Cable", ignoreCase = true) -> 
                "No cable machine? Substitute loop or tube Resistance Bands anchored securely!"
            else -> "Try standard Bodyweight equivalents emphasizing slow, 3-second negative reps!"
        }
    }

    /**
     * Witty, humorous, and highly motivating responses from Echo the AI Coach.
     */
    fun getEchoResponse(topic: String, streakCount: Int = 0): String {
        return when (topic.lowercase()) {
            "streak" -> {
                if (streakCount >= 7) "Look at you on a $streakCount-day streak! Even the mirrors in the gym are starting to admire you. Don't stop now, hero!"
                else "You're building fire! Streak is $streakCount days. Consistency makes champions, excuses make clothes tight."
            }
            "cheat" -> "Enjoyed that cheat meal? Delicious, right? Good. Now zip that backpack, tie your laces, and let's sweat it off. One cheat meal is a mental reward, but let's make sure it doesn't turn into a cheat year."
            "rest" -> "It's Rest Day! That means you put down the weights and pick up the hydration. Stretching, easy yoga, or walking is allowed. Do NOT lay like a potato all day, let the muscles recover actively!"
            "motivate" -> listOf(
                "Sweat is just fat crying. Let's make it sob today!",
                "Your body is a temple, not a trash can. Feed it protein, not donuts.",
                "If it was easy, everyone would look like an action hero. Let's go!",
                "Remember, Roman posture and steel biceps weren't built in a day, but they were built daily."
            ).random()
            "missed" -> "Missed a session today? It's fine, life happens. But Echo expects you to hit a quick 20-minute home burner to keep the momentum alive. Let's not make it two days in a row!"
            else -> "Hey champion! Echo is here. Hit me with your questions: nutrition, workout swaps, or straight motivation!"
        }
    }
}
