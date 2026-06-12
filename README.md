# EchoFit 🚀

[![Platform](https://img.shields.io/badge/Platform-Android-green.svg?style=flat-square)](#tech-stack)
[![Language](https://img.shields.io/badge/Language-Kotlin%20%2F%20TypeScript-blue.svg?style=flat-square)](#tech-stack)
[![License](https://img.shields.io/badge/License-MIT-orange.svg?style=flat-square)](#license)

**EchoFit** is an advanced, AI-powered personal fitness and nutrition companion app. It features a modern mobile application for tracking and coaching, combined with a sleek web landing page. 

The application utilizes the **Google Gemini API** for visual meal scanner and physique comparative assessments, falling back automatically to a local, on-device **Naive Bayes text classifier** and rule-based estimation engine when running offline.

---

## 📸 Screenshots

| Dashboard Interface | Settings Interface |
| :---: | :---: |
| ![Dashboard](screenshot.png) | ![Settings](screenshot_settings.png) |

---

## 🛠 Tech Stack

### 📱 Android Application (`/app`)
* **Framework**: Jetpack Compose & Material 3 (Rich aesthetics, dark theme, and glassmorphism cards).
* **Language**: Kotlin.
* **Architecture**: MVVM with Flow, StateFlow, and Repository patterns.
* **Persistence**: Room Database (Local SQL persistence).
* **Concurrency**: Kotlin Coroutines.
* **Background Processing**: WorkManager (Manages local automated notifications/reminders scheduler).
* **AI Engine**:
  * **Online**: Gemini API (via Google Generative AI SDK) for calorie estimation from food images and physique photo diagnostics.
  * **Offline Fallback**: Custom **Naive Bayes Classifier** running entirely on-device to classify user queries, coupled with offline rule-based estimators.
* **Database Sync**: Cloud Firestore integration via Firebase SDK (Synchronizes Room databases with cloud servers).

### 💻 Web Landing Page (`/landing-page`)
* **Framework**: React.
* **Build System**: Vite.
* **Language**: TypeScript.
* **Styling**: Vanilla CSS.

---

## 🌟 Key Features

1. **AI Diet Planner & Calorie Vision Scanner**
   * Snap/upload a meal photo; the Gemini Vision engine estimates the food name, portion sizes, calories, proteins, carbs, fats, and fiber.
   * *Offline Mode*: Automatically falls back to high-quality offline local menu options if cloud connection is unavailable.
2. **AI Physique Transformation Assessments**
   * Safely upload and compare progress photos (Front, Side, Back).
   * Gemini compares before/after photos and generates visual body fat estimates, muscle toning analysis, and motivational feedback.
3. **Daily Checklist & Gamification**
   * Keep track of water intake, workout completion, meal logs, and sleep tracking.
   * Progress levels up your character (e.g., Level 3 - Gym Warrior) with gamified badges.
4. **WorkManager Reminder System**
   * Automated, local background alerts scheduled for breakfast, lunch, snacks, dinner, and workouts.
5. **Firebase Cloud Sync**
   * A force cloud sync feature that pushes local Room database entities securely into Firebase Cloud Firestore.

---

## 🚀 Getting Started

### 📱 Running the Android App
#### Prerequisites
* Android Studio (Jellyfish 2024.1.1 or newer recommended)
* JDK 17
* Android SDK 34 (API level 34)

#### Steps
1. Open the project root directory in Android Studio.
2. Allow Gradle to synchronize dependencies.
3. Ensure your emulator or physical test device is active (e.g. `Pixel_7` AVD).
4. Run the application from Android Studio, or build and run from CLI:
   ```bash
   ./gradlew assembleDebug
   # To deploy and run via CLI:
   android run --apks=app/build/outputs/apk/debug/app-debug.apk --device=emulator-5554
   ```

### 💻 Running the Landing Page Web App
#### Prerequisites
* Node.js (v18 or newer)
* npm or yarn

#### Steps
1. Navigate to the landing page folder:
   ```bash
   cd landing-page
   ```
2. Install npm dependencies:
   ```bash
   npm install
   ```
3. Launch the local developer server:
   ```bash
   npm run dev
   ```
4. Open your browser and navigate to `http://localhost:5173`.

---

## 🧪 CI/CD Workflows
This repository has pre-configured **GitHub Actions** workflows located under `.github/workflows/`:
* **Android CI**: Runs compilation checks on every pull request or push to `main` branch.
* **Web Landing Page CI**: Runs Node dependency installation and build tests to prevent breaking changes on the website.

---

## 🤝 Contributing
1. Fork the Project.
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`).
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`).
4. Push to the Branch (`git push origin feature/AmazingFeature`).
5. Open a Pull Request.

---

## 📄 License
Distributed under the MIT License. See `LICENSE` for more information.
