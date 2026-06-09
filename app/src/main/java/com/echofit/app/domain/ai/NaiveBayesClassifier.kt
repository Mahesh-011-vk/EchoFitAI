package com.echofit.app.domain.ai

import java.util.Locale

class NaiveBayesClassifier {

    enum class Category {
        DIET, WORKOUT, FORM, PROGRESS, MOTIVATION, GREETINGS
    }

    private val trainingData = listOf(
        // DIET category
        "suggest a low-budget protein snack" to Category.DIET,
        "what should i eat for dinner" to Category.DIET,
        "tell me food options with high protein" to Category.DIET,
        "i am hungry what is a healthy recipe" to Category.DIET,
        "suggest cheap vegetarian protein sources" to Category.DIET,
        "how many calories are in sprouts salad" to Category.DIET,
        "low budget meal options and foods" to Category.DIET,
        "healthy snacks for weight loss" to Category.DIET,
        "what is a good diet for muscle building" to Category.DIET,
        "should I drink milk for protein" to Category.DIET,

        // WORKOUT category
        "missed gym today swap for home" to Category.WORKOUT,
        "give me a home workout routine" to Category.WORKOUT,
        "suggest exercise splits for beginner" to Category.WORKOUT,
        "what exercises should i do for legs" to Category.WORKOUT,
        "cannot go to gym swap exercise" to Category.WORKOUT,
        "train chest at home without equipment" to Category.WORKOUT,
        "how to swap push day routine" to Category.WORKOUT,
        "bicep exercises with backpacks" to Category.WORKOUT,
        "cardio and core training ideas" to Category.WORKOUT,
        "do I need gym weights to build muscle" to Category.WORKOUT,

        // FORM category
        "explain push-up form and mistakes" to Category.FORM,
        "how to execute squats properly" to Category.FORM,
        "common deadlift mistakes to avoid" to Category.FORM,
        "what is the proper setup for pull ups" to Category.FORM,
        "how to do lateral raises safely" to Category.FORM,
        "bench press joint pain and elbow flare" to Category.FORM,
        "how to avoid injury during workout" to Category.FORM,
        "correct posture for overhead barbell press" to Category.FORM,
        "what are the beginner variations for squat" to Category.FORM,

        // PROGRESS category
        "what is my estimated body fat" to Category.PROGRESS,
        "am i overweight check my bmi" to Category.PROGRESS,
        "what is my suggested weight goal" to Category.PROGRESS,
        "tell me my current fitness score" to Category.PROGRESS,
        "am I on track with weight targets" to Category.PROGRESS,
        "how is my weekly consistency score" to Category.PROGRESS,
        "check body metrics profile stats" to Category.PROGRESS,

        // MOTIVATION category
        "i feel lazy today and tired" to Category.MOTIVATION,
        "give me some motivation to hit the gym" to Category.MOTIVATION,
        "i want to give up today is too hard" to Category.MOTIVATION,
        "what happens if I eat a cheat meal" to Category.MOTIVATION,
        "is it okay to take a rest day" to Category.MOTIVATION,
        "how is my streak looking" to Category.MOTIVATION,
        "i missed my workout sessions" to Category.MOTIVATION,
        "inspire me to stay consistent" to Category.MOTIVATION,

        // GREETINGS category
        "hello coach echo" to Category.GREETINGS,
        "hi who are you" to Category.GREETINGS,
        "how can you help me" to Category.GREETINGS,
        "introduce yourself AI coach" to Category.GREETINGS,
        "hey echo tell me what you do" to Category.GREETINGS,
        "good morning fitness coach" to Category.GREETINGS
    )

    private val vocabulary = mutableSetOf<String>()
    private val classDocumentCounts = mutableMapOf<Category, Int>()
    private val wordCountsPerClass = mutableMapOf<Category, MutableMap<String, Int>>()
    private val totalWordCountPerClass = mutableMapOf<Category, Int>()
    private var totalDocuments = 0

    init {
        train()
    }

    private fun train() {
        totalDocuments = trainingData.size
        for (category in Category.values()) {
            classDocumentCounts[category] = 0
            wordCountsPerClass[category] = mutableMapOf()
            totalWordCountPerClass[category] = 0
        }

        for ((text, category) in trainingData) {
            classDocumentCounts[category] = classDocumentCounts[category]!! + 1
            val tokens = tokenize(text)
            for (token in tokens) {
                vocabulary.add(token)
                val wordCounts = wordCountsPerClass[category]!!
                wordCounts[token] = wordCounts.getOrDefault(token, 0) + 1
                totalWordCountPerClass[category] = totalWordCountPerClass[category]!! + 1
            }
        }
    }

    private fun tokenize(text: String): List<String> {
        return text.lowercase(Locale.getDefault())
            .replace(Regex("[^a-zA-Z0-9\\s]"), "")
            .split(Regex("\\s+"))
            .filter { it.isNotBlank() && it.length > 2 }
    }

    fun classify(text: String): Category {
        val tokens = tokenize(text)
        if (tokens.isEmpty()) {
            val lower = text.lowercase(Locale.getDefault())
            return when {
                lower.contains("protein") || lower.contains("snack") || lower.contains("eat") || lower.contains("diet") || lower.contains("meal") -> Category.DIET
                lower.contains("gym") || lower.contains("home") || lower.contains("workout") || lower.contains("exercise") || lower.contains("swap") -> Category.WORKOUT
                lower.contains("form") || lower.contains("mistake") || lower.contains("how to") || lower.contains("correct") -> Category.FORM
                lower.contains("fat") || lower.contains("bmi") || lower.contains("weight") || lower.contains("progress") || lower.contains("score") -> Category.PROGRESS
                lower.contains("hello") || lower.contains("hi") || lower.contains("hey") || lower.contains("who") -> Category.GREETINGS
                else -> Category.MOTIVATION
            }
        }

        var bestCategory = Category.MOTIVATION
        var bestScore = Double.NEGATIVE_INFINITY

        val vocabSize = vocabulary.size

        for (category in Category.values()) {
            val classDocCount = classDocumentCounts[category] ?: 0
            if (classDocCount == 0) continue

            // Prior probability P(C)
            var score = kotlin.math.log10(classDocCount.toDouble() / totalDocuments)

            val wordCounts = wordCountsPerClass[category]!!
            val totalClassWords = totalWordCountPerClass[category] ?: 0

            // Likelihood probability P(W|C) with Laplace smoothing
            for (token in tokens) {
                val count = wordCounts.getOrDefault(token, 0)
                score += kotlin.math.log10((count + 1).toDouble() / (totalClassWords + vocabSize))
            }

            if (score > bestScore) {
                bestScore = score
                bestCategory = category
            }
        }

        return bestCategory
    }
}
