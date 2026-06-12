package com.echofit.app.domain.ai

import com.echofit.app.domain.model.*
import java.util.Locale

object OfflineAIEngine {

    private val classifier = NaiveBayesClassifier()

    fun generateResponse(
        newMessage: String,
        profile: UserProfile,
        attendance: DailyAttendance,
        todayMeals: List<MealLog>,
        todayWaterLitres: Double
    ): String {
        val category = classifier.classify(newMessage)
        val goalAnalysis = EchoAIEngine.analyzeGoal(profile)

        return when (category) {
            NaiveBayesClassifier.Category.DIET -> generateDietResponse(profile, goalAnalysis, todayMeals)
            NaiveBayesClassifier.Category.WORKOUT -> generateWorkoutResponse(profile, newMessage)
            NaiveBayesClassifier.Category.FORM -> generateFormResponse(newMessage)
            NaiveBayesClassifier.Category.PROGRESS -> generateProgressResponse(profile, goalAnalysis, attendance)
            NaiveBayesClassifier.Category.MOTIVATION -> generateMotivationResponse(profile, attendance, todayMeals, todayWaterLitres)
            NaiveBayesClassifier.Category.GREETINGS -> generateGreetingsResponse(profile)
        }
    }

    private fun generateDietResponse(
        profile: UserProfile,
        analysis: GoalAnalysis,
        todayMeals: List<MealLog>
    ): String {
        val targetCals = analysis.calorieTarget
        val targetProt = analysis.proteinTarget

        val loggedCals = todayMeals.sumOf { it.calories }
        val loggedProt = todayMeals.sumOf { it.protein }

        val remainingCals = (targetCals - loggedCals).coerceAtLeast(0)
        val remainingProt = (targetProt - loggedProt).coerceAtLeast(0)

        val isLowBudget = profile.budgetLevel.equals("Low", ignoreCase = true)
        val options = when (profile.dietType) {
            "Vegetarian" -> {
                if (isLowBudget) {
                    listOf("Roasted Chana (100g) ~ 19g Protein / 15 rupees", "Soya Chunks fry (50g) ~ 26g Protein / 10 rupees", "Double-toned Milk (500ml) ~ 16g Protein / 28 rupees")
                } else {
                    listOf("Whey Protein Shake (1 scoop) ~ 24g Protein", "Paneer Tikka (150g) ~ 28g Protein", "Greek Yogurt (200g) ~ 15g Protein")
                }
            }
            "Eggetarian" -> {
                if (isLowBudget) {
                    listOf("Whole Boiled Eggs (4) ~ 24g Protein / 24 rupees", "Egg Bhurji (3 eggs) ~ 18g Protein / 18 rupees", "Roasted Chana (100g) ~ 19g Protein")
                } else {
                    listOf("Egg White Omelet (6 eggs) ~ 24g Protein", "Paneer Sandwich ~ 20g Protein", "Whey Protein Shake ~ 24g Protein")
                }
            }
            "Vegan" -> {
                listOf("Tofu scramble (150g) ~ 18g Protein", "Soya chunks (50g) ~ 26g Protein", "Roasted Sprouts salad (1 bowl) ~ 15g Protein")
            }
            else -> { // Non-Veg
                if (isLowBudget) {
                    listOf("Boiled Eggs (3) ~ 18g Protein / 18 rupees", "Chicken Breast (150g) ~ 33g Protein / 40 rupees", "Double-toned Milk (500ml) ~ 16g Protein")
                } else {
                    listOf("Grilled Chicken (250g) ~ 55g Protein", "Salmon/Fish Fillet (200g) ~ 40g Protein", "Whey Protein Shake (1 scoop) ~ 24g Protein")
                }
            }
        }

        return """
            ### Offline Nutrition Assistant 🥗
            *Powered by Coach Echo local AI engine. No cloud connection or API Key active.*

            Here is your personalized diet intake report:
            
            * **Daily Calorie Target**: `$targetCals kcal` (Logged: `$loggedCals kcal` | Remaining: `$remainingCals kcal`)
            * **Daily Protein Target**: `${targetProt}g` (Logged: `${loggedProt}g` | Remaining: `${remainingProt}g`)

            **Recommended Protein Snacks for your custom profile (${profile.dietType} | Budget: ${profile.budgetLevel})**:
            ${options.joinToString("\n") { "* **$it**" }}

            > [!TIP]
            > Track your food intake consistently under the Diet tab to achieve your daily calorie target!
        """.trimIndent()
    }

    private fun generateWorkoutResponse(
        profile: UserProfile,
        newMessage: String
    ): String {
        val lowerMessage = newMessage.lowercase(Locale.getDefault())
        
        val exerciseToSwap = when {
            lowerMessage.contains("bench press") -> "Bench Press"
            lowerMessage.contains("dumbbell") -> "Dumbbells"
            lowerMessage.contains("pull up") || lowerMessage.contains("pull-up") -> "Pull-Ups"
            lowerMessage.contains("leg press") -> "Leg Press"
            lowerMessage.contains("cable") -> "Cable Exercises"
            else -> null
        }

        val swapTip = if (exerciseToSwap != null) {
            "**Substitution request detected**: " + EchoAIEngine.substituteExercise(exerciseToSwap)
        } else {
            "No specific exercise recognized in request. If you want to swap a specific lift, ask like *'how to swap bench press'*!"
        }

        val circuit = when (profile.fitnessGoal) {
            "Fat Loss" -> listOf(
                "Jumping Jacks - 45 seconds",
                "Bodyweight Squats - 20 reps",
                "Standard Pushups - 15 reps",
                "Plank Hold - 60 seconds",
                "Rest - 45 seconds (Repeat 4 times)"
            )
            "Muscle Gain" -> listOf(
                "Backpack Squats (weighted) - 15 reps",
                "Decline Pushups - 12 reps",
                "Backpack Bent Over Rows - 12 reps",
                "Dips on Chair/Bed - 15 reps",
                "Rest - 60 seconds (Repeat 4 times)"
            )
            else -> listOf(
                "Air Squats - 15 reps",
                "Standard Pushups - 12 reps",
                "Glute Bridges - 15 reps",
                "Bicycle Crunches - 20 reps",
                "Rest - 45 seconds (Repeat 3 times)"
            )
        }

        val injuryWarning = if (profile.injuriesOrLimitations != "None") {
            "\n> [!WARNING]\n> **Injury Alert**: Mind your *${profile.injuriesOrLimitations}* limitations. Execute exercises slowly or substitute them to avoid exacerbating joint strain."
        } else ""

        return """
            ### Offline Workout Switchboard 🏋️
            *Operating locally. Let's make this session count!*

            $swapTip

            **Quick Home Burner Routine (Customized for ${profile.fitnessGoal})**:
            ${circuit.joinToString("\n") { "* $it" }}
            $injuryWarning

            > [!TIP]
            > Consistent efforts compound. Do the work today so you can thank yourself tomorrow!
        """.trimIndent()
    }

    private fun generateFormResponse(newMessage: String): String {
        val lower = newMessage.lowercase(Locale.getDefault())

        val exName = when {
            lower.contains("pushup") || lower.contains("push-up") -> "Pushups"
            lower.contains("squat") -> "Squats"
            lower.contains("deadlift") -> "Deadlifts"
            lower.contains("pullup") || lower.contains("pull-up") -> "Pullups"
            lower.contains("shoulder press") || lower.contains("overhead press") -> "Shoulder Press"
            lower.contains("lateral raise") -> "Lateral Raises"
            lower.contains("tricep") -> "Triceps Cable Pushdowns"
            lower.contains("bicep curl") || lower.contains("curl") -> "Biceps Curls"
            else -> null
        }

        if (exName == null) {
            return """
                ### Technique Clinic 🔍
                *Operating in Local Guide mode.*
                
                I can explain the proper form and common mistakes for key exercises:
                * **Squats**
                * **Push-ups**
                * **Deadlifts**
                * **Pull-ups**
                * **Shoulder Press / Lateral Raises**
                * **Bicep Curls / Triceps Pushdowns**
                
                Please ask about form using one of these names, e.g., *"How do I do squats?"*
            """.trimIndent()
        }

        val (setup, mistakes, variations) = when (exName) {
            "Pushups" -> Triple(
                listOf("Place hands slightly wider than shoulders", "Lower body in straight line till chest almost touches floor", "Brace core and press up extending elbows"),
                listOf("Flaring elbows out to 90 degrees", "Sagging lower back and hips"),
                "Wall Pushups (Beginner) / Decline Pushups (Advanced)"
            )
            "Squats" -> Triple(
                listOf("Stand feet shoulder-width, toes out slightly", "Hinge hips back and bend knees keeping chest up", "Drive up through heels back to top"),
                listOf("Knees caving inwards", "Heels lifting off ground"),
                "Wall Sits (Beginner) / Pistol Squats (Advanced)"
            )
            "Deadlifts" -> Triple(
                listOf("Stand feet hip-width under bar", "Hinge hips keeping back flat and grip bar", "Drive hips forward and stand tall lifting weight"),
                listOf("Rounding lower spine", "Squatting the weight instead of hinging"),
                "Glute Bridges (Beginner) / Deficit Deadlifts (Advanced)"
            )
            "Pullups" -> Triple(
                listOf("Hang with palms facing away wider than shoulders", "Pull chest up to bar pulling elbows down", "Slowly lower back to dead hang"),
                listOf("Swinging legs for momentum", "Not completing full range of motion"),
                "Assisted Pull-ups (Beginner) / Weighted Pull-ups (Advanced)"
            )
            "Shoulder Press" -> Triple(
                listOf("Stand tall, clean dumbbells to shoulders", "Press weight straight up overhead locking out core", "Control path down back to shoulders"),
                listOf("Arching lower back", "Bouncing bar off chest"),
                "Resistance Band Press (Beginner) / Handstand Pushups (Advanced)"
            )
            "Lateral Raises" -> Triple(
                listOf("Stand holding weights at sides, slight elbow bend", "Raise weights to shoulder level leading with elbows", "Slowly control deceleration back down"),
                listOf("Swinging body to lift weight", "Shrugging shoulders upwards"),
                "Banded Raises (Beginner) / Cable Raises (Advanced)"
            )
            else -> Triple(
                listOf("Hold dumbbells with elbows pinned to sides", "Curl weights to shoulder height slowly", "Extend arm completely at bottom"),
                listOf("Swinging torso to cheat weight", "Elbows drifting forward/backward"),
                "Resistance Band Curls (Beginner) / Spider Curls (Advanced)"
            )
        }

        return """
            ### Technique Clinic: $exName 🔍
            *Correct execution prevents injury and increases muscle recruitment.*

            **Step-by-Step Instructions**:
            ${setup.mapIndexed { i, s -> "${i + 1}. $s" }.joinToString("\n")}

            **Common Mistakes (Avoid These!)**:
            ${mistakes.joinToString("\n") { "* ❌ $it" }}

            **Variations**:
            * **Progression/Regression**: `$variations`
        """.trimIndent()
    }

    private fun generateProgressResponse(
        profile: UserProfile,
        analysis: GoalAnalysis,
        attendance: DailyAttendance
    ): String {
        return """
            ### Physical Progress Audit 📊
            *Analyzing your local metrics database on-device.*

            **Current Metrics**:
            * **Height**: `${profile.height} cm`
            * **Current Weight**: `${profile.weight} kg`
            * **Target Weight**: `${profile.targetWeight} kg`
            * **Estimated Midpoint Weight**: `${analysis.suggestedWeightGoal} kg`
            
            **Body Composition**:
            * **Body Mass Index (BMI)**: `${analysis.bmi}` (`${analysis.bmiCategory}`)
            * **Estimated Body Fat %**: `${analysis.estimatedBodyFat}%`
            * **Profile Experience Level**: `${profile.experienceLevel}`

            **Consistency Target**: `${analysis.consistencyTargetPercentage}%`
            **Today's Logged Score**: `${attendance.score}/100`

            > [!TIP]
            > Regular logs build habit loops. Keep logging weight under the **Progress** tab to track change curves!
        """.trimIndent()
    }

    private fun generateMotivationResponse(
        profile: UserProfile,
        attendance: DailyAttendance,
        todayMeals: List<MealLog>,
        todayWaterLitres: Double
    ): String {
        val jokes = listOf(
            "Sweat is just fat crying. Let's make it sob today!",
            "Your body is a temple, not a trash can. Feed it protein, not donuts.",
            "If it was easy, everyone would look like an action hero. Grab your pack and get moving!",
            "Roman posture and steel biceps weren't built in a day, but they were built daily.",
            "Excuses don't burn calories. Go hit that burner session!"
        )

        val workoutLogged = if (attendance.isWorkoutDone) "✅ Completed" else "❌ Pending"
        val waterLogged = if (attendance.isWaterDone) "✅ Logged ($todayWaterLitres L)" else "❌ Pending"
        val mealsCount = todayMeals.size
        val dietLogged = if (attendance.isDietDone) "✅ $mealsCount meals logged" else "❌ No meals logged yet"
        val weightLogged = if (attendance.isWeightLogged) "✅ Weight logged" else "❌ Weight log pending"

        return """
            ### Accountability & Motivation Engine 🔥
            *Local coach session. Checking today's effort logs.*

            > "${jokes.random()}"

            **Today's Progress Checklist**:
            * **Workout**: `$workoutLogged`
            * **Water**: `$waterLogged`
            * **Diet Log**: `$dietLogged`
            * **Weight Tracker**: `$weightLogged`

            **Coaching Advice**:
            You are chasing the **${profile.fitnessGoal}** goal. Consistency beats intensity every single day. If you missed a target, don't sweat it. Pick one checklist item and complete it right now!
        """.trimIndent()
    }

    private fun generateGreetingsResponse(profile: UserProfile): String {
        return """
            ### Meet Coach Echo (Offline Mode) 👋
            *I am your AI fitness companion, running fully offline on your device!*

            **What is this mode?**
            I am powered by a local **Naive Bayes text classifier** trained directly on your device. I classify your queries in real-time to answer questions without sending any data to the cloud or requiring an API key.

            **What can you ask me?**
            1. **Diet Help**: *"Suggest a low-budget protein snack"* or *"What should I eat"*
            2. **Workout Swaps**: *"Missed gym today. Swap for home"* or *"How to swap bench press"*
            3. **Exercise Form**: *"How to execute squats"* or *"Push up common mistakes"*
            4. **Progress Checks**: *"What is my estimated body fat"* or *"What is my BMI"*
            5. **Motivation & Streaks**: *"I feel lazy today"* or *"Give me motivation"*

            Let's crush your **${profile.fitnessGoal}** goal together, champion! 🚀
        """.trimIndent()
    }
}
