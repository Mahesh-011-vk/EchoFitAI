div align="center">

<img src="https://capsule-render.vercel.app/api?type=waving&color=0:00C853,50:00B0FF,100:7C4DFF&height=220&section=header&text=EchoFit%20%F0%9F%9A%80&fontSize=64&fontColor=ffffff&fontAlignY=38&desc=Your%20AI-powered%20fitness%20%26%20nutrition%20companion&descAlignY=60&descSize=20" alt="EchoFit banner" />
[![Platform](https://img.shields.io/badge/Platform-Android-green.svg?style=flat-square)](#-tech-stack)
[![Language](https://img.shields.io/badge/Language-Kotlin%20%2F%20TypeScript-blue.svg?style=flat-square)](#-tech-stack)
[![License](https://img.shields.io/badge/License-MIT-orange.svg?style=flat-square)](#-license)

<img src="https://readme-typing-svg.demolab.com?font=Fira+Code&weight=600&size=20&pause=1200&color=00B0FF&center=true&vCenter=true&width=700&lines=Scan+your+meal.+Get+instant+macros.;Compare+your+physique+progress+with+AI.;Works+online+with+Gemini.+Works+offline+too." alt="Typing animation" />
<img src="https://capsule-render.vercel.app/api?type=waving&color=0:00C853,50:00B0FF,100:7C4DFF&height=220&section=header&text=EchoFit%20%F0%9F%9A%80&fontSize=64&fontColor=ffffff&fontAlignY=38&desc=Your%20AI-powered%20fitness%20%26%20nutrition%20companion&descAlignY=60&descSize=20" alt="EchoFit banner" width="100%" />

<br/>
### Your AI-Powered Personal Fitness & Nutrition Companion

<img src="https://readme-typing-svg.demolab.com?font=Fira+Code&weight=600&size=20&pause=1200&color=00B0FF&center=true&vCenter=true&width=700&lines=Scan+your+meal.+Get+instant+macros.;Compare+your+physique+progress+with+AI.;Works+online+with+Gemini.+Works+offline+too." alt="Typing animation" />

![Platform](https://img.shields.io/badge/Platform-Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)
![Kotlin](https://img.shields.io/badge/Kotlin-Jetpack%20Compose-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)
![TypeScript](https://img.shields.io/badge/TypeScript-React-3178C6?style=for-the-badge&logo=typescript&logoColor=white)
![Gemini](https://img.shields.io/badge/Google-Gemini%20API-4285F4?style=for-the-badge&logo=googlegemini&logoColor=white)
![Firebase](https://img.shields.io/badge/Firebase-Firestore-FFCA28?style=for-the-badge&logo=firebase&logoColor=black)
![License](https://img.shields.io/badge/License-MIT-FF9800?style=for-the-badge)
<p align="center">
  <b>EchoFit</b> pairs a modern <b>Android app</b> built with Jetpack Compose & Material 3 with a sleek <b>interactive web companion</b>. Powered by <b>Google Gemini API</b> online with an intelligent on-device <b>Naive Bayes</b> offline fallback.
</p>

<br/>
<p align="center">
  <img src="https://img.shields.io/badge/Platform-Android-3DDC84?style=for-the-badge&logo=android&logoColor=white" alt="Platform Android" />
  <img src="https://img.shields.io/badge/Kotlin-Jetpack%20Compose-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white" alt="Kotlin Jetpack Compose" />
  <img src="https://img.shields.io/badge/TypeScript-React-3178C6?style=for-the-badge&logo=typescript&logoColor=white" alt="TypeScript React" />
  <img src="https://img.shields.io/badge/Google-Gemini%20API-4285F4?style=for-the-badge&logo=googlegemini&logoColor=white" alt="Google Gemini" />
  <img src="https://img.shields.io/badge/Firebase-Firestore-FFCA28?style=for-the-badge&logo=firebase&logoColor=black" alt="Firebase" />
  <img src="https://img.shields.io/badge/License-MIT-FF9800?style=for-the-badge" alt="License MIT" />
</p>

**[📸 Screenshots](#-screenshots) · [🌟 Features](#-key-features) · [🛠 Tech Stack](#-tech-stack) · [🏗 Architecture](#-architecture) · [🚀 Getting Started](#-getting-started) · [🧪 CI/CD](#-cicd-workflows) · [🤝 Contributing](#-contributing)**
**[📸 Screenshots](#-screenshots) · [🌟 Key Features](#-key-features) · [🛠 Tech Stack](#-tech-stack) · [🏗 Architecture](#-architecture) · [🚀 Getting Started](#-getting-started) · [🧪 CI/CD](#-cicd-workflows) · [🤝 Contributing](#-contributing)**

</div>

---

## 🎯 About

**EchoFit** is an advanced, AI-powered personal fitness and nutrition companion. It pairs a modern **Android app** for tracking and coaching with a sleek **web landing page**.
**EchoFit** is an advanced, AI-powered personal fitness and nutrition companion. It pairs a modern **Android app** for daily coaching and tracking with a sleek **web companion**.

> 🧠 **Smart online, resilient offline.**
> The app uses the **Google Gemini API** for its visual meal scanner and physique comparison assessments. With no connection, it falls back automatically to an on-device **Naive Bayes text classifier** and a rule-based estimation engine.
> 🧠 **Smart online, resilient offline.**  
> The app uses the **Google Gemini API** for its visual meal scanner and physique comparison diagnostics. When network connectivity is unavailable, it falls back seamlessly to an on-device **Naive Bayes text classifier** and a rule-based nutritional estimation engine.

---

## 📸 Screenshots

<div align="center">

| 📊 Dashboard | ⚙️ Settings |
| 📊 Dashboard Interface | ⚙️ Settings & System Status |
| :---: | :---: |
| <img src="screenshot.png" alt="Dashboard" width="300" /> | <img src="screenshot_settings.png" alt="Settings" width="300" /> |
| <img src="screenshot.png" alt="EchoFit Dashboard" width="340" /> | <img src="screenshot_settings.png" alt="EchoFit Settings" width="340" /> |

</div>

@@ -50,41 +58,41 @@

### 🍽️ AI Diet Planner & Calorie Vision Scanner
Snap or upload a meal photo and Gemini Vision estimates:
- Food name and portion size
- Calories, protein, carbs, fats, fiber
- Food identity and portion sizes
- Calorie counts and macro breakdown (protein, carbs, fats, fiber)

📴 **Offline mode:** falls back to high-quality local menu options when the cloud is unavailable.
📴 **Offline Mode:** Falls back automatically to an on-device classification and local recipe database when offline.

</td>
<td width="50%" valign="top">

### 💪 AI Physique Transformation Assessments
Safely upload and compare progress photos (**Front, Side, Back**). Gemini compares before and after to produce:
- Visual body fat estimates
- Muscle toning analysis
- Motivational feedback
Safely upload and compare progress photos (**Front, Side, Back**). Gemini compares before-and-after captures to evaluate:
- Visual body fat percentage trends
- Muscle definition and toning analysis
- Motivational coaching insights

</td>
</tr>
<tr>
<td width="50%" valign="top">

### ✅ Daily Checklist & Gamification
Track **water intake, workouts, meal logs, and sleep**. Progress levels up your character (e.g. *Level 3 - Gym Warrior*) with gamified badges.
Track **water hydration, workout sessions, meal logging, and sleep cycles**. Every achievement levels up your character tier (e.g. *Level 3 - Gym Warrior*) with gamified reward badges.

</td>
<td width="50%" valign="top">

### ⏰ WorkManager Reminder System
Automated local background alerts for **breakfast, lunch, snacks, dinner, and workouts**.
Automated, battery-efficient local background notifications scheduled precisely for **breakfast, lunch, afternoon snacks, dinner, and workouts**.

</td>
</tr>
<tr>
<td colspan="2" valign="top">

### ☁️ Firebase Cloud Sync
A force-sync feature pushes local Room database entities securely into **Firebase Cloud Firestore**.
Reliable offline-first architecture with on-demand cloud sync that pushes local Room database entities securely into **Firebase Cloud Firestore**.

</td>
</tr>
@@ -95,38 +103,38 @@ A force-sync feature pushes local Room database entities securely into **Firebas
## 🛠 Tech Stack

<div align="center">

<img src="https://skillicons.dev/icons?i=kotlin,androidstudio,firebase,react,ts,vite,css,gradle,github,githubactions&theme=dark" alt="Tech icons" />

  <img src="https://skillicons.dev/icons?i=kotlin,androidstudio,firebase,react,ts,vite,css,gradle,github,githubactions&theme=dark" alt="Tech Stack Icons" />
</div>

<br/>

### 📱 Android Application (`/app`)

| Area | Technology |
| --- | --- |
| 🎨 **UI Framework** | Jetpack Compose & Material 3 (rich aesthetics, dark theme, glassmorphism cards) |
| Area | Technology & Details |
| :--- | :--- |
| 🎨 **UI Framework** | Jetpack Compose & Material 3 (Rich aesthetics, dark theme, glassmorphic cards) |
| 🧑‍💻 **Language** | Kotlin |
| 🏛️ **Architecture** | MVVM with Flow, StateFlow, and Repository patterns |
| 💾 **Persistence** | Room Database (local SQL) |
| ⚡ **Concurrency** | Kotlin Coroutines |
| ⏱️ **Background Work** | WorkManager (local notification and reminder scheduler) |
| 🔄 **Database Sync** | Cloud Firestore via Firebase SDK (syncs Room with cloud) |
| 💾 **Persistence** | Room Database (Local SQL persistence with type converters) |
| ⚡ **Concurrency** | Kotlin Coroutines & Structured Concurrency |
| ⏱️ **Background Work** | Android Jetpack WorkManager (Automated notification & reminder scheduler) |
| 🔄 **Database Sync** | Cloud Firestore via Firebase SDK (Synchronizes Room databases to cloud) |

**🤖 AI Engine**

| Mode | Details |
| --- | --- |
| 🌐 **Online** | Gemini API (Google Generative AI SDK) for calorie estimation from food images and physique photo diagnostics |
| 📴 **Offline fallback** | Custom **Naive Bayes classifier** running fully on-device to classify user queries, plus offline rule-based estimators |
| :--- | :--- |
| 🌐 **Online** | Google Gemini API (via Google Generative AI SDK) for multimodal calorie vision scanning & comparative physique diagnostics |
| 📴 **Offline Fallback** | Custom **Naive Bayes text classifier** running 100% on-device coupled with a rule-based macro estimation engine |

### 💻 Web Landing Page (`/landing-page`)

| Area | Technology |
| --- | --- |
| ⚛️ **Framework** | React |
| Area | Technology & Details |
| :--- | :--- |
| ⚛️ **Framework** | React 18 |
| ⚡ **Build System** | Vite |
| 🔷 **Language** | TypeScript |
| 🎨 **Styling** | Vanilla CSS |
| 🎨 **Styling** | Vanilla CSS (Zero bloated CSS runtime, ultra-fast performance) |

---

@@ -155,68 +163,67 @@ flowchart TD

## 🚀 Getting Started

### 📱 Run the Android App

**Prerequisites**

- 🧰 Android Studio (Jellyfish 2024.1.1 or newer recommended)
- ☕ JDK 17
- 📱 Android SDK 34 (API level 34)

**Steps**

1. Open the project root directory in Android Studio.
2. Let Gradle synchronize dependencies.
3. Start an emulator or connect a physical device (e.g. `Pixel_7` AVD).
4. Run from Android Studio, or build and run from the CLI:

### 📱 Android Application

#### Prerequisites
- 🧰 **Android Studio** (Jellyfish 2024.1.1 or newer recommended)
- ☕ **JDK 17**
- 📱 **Android SDK 34** (API level 34)

#### Steps
1. Clone the repository:
   ```bash
   git clone https://github.com/Mahesh-011-vk/EchoFitAI.git
   cd EchoFitAI
   ```
2. Open the project root directory in Android Studio.
3. Allow Gradle to synchronize dependencies.
4. Ensure your emulator or physical test device is active (e.g. `Pixel_7` AVD).
5. Run the application from Android Studio, or build and install via CLI:
   ```bash
   ./gradlew assembleDebug
   # To deploy and run via CLI:
   android run --apks=app/build/outputs/apk/debug/app-debug.apk --device=emulator-5554
   ```

### 💻 Web Landing Page

#### Prerequisites
- 🟢 **Node.js** (v18 or newer)
- 📦 **npm** or **yarn**

#### Steps
```bash
./gradlew assembleDebug

# Deploy and run via CLI
android run --apks=app/build/outputs/apk/debug/app-debug.apk --device=emulator-5554
```

### 💻 Run the Landing Page

**Prerequisites**

- 🟢 Node.js v18 or newer
- 📦 npm or yarn

**Steps**

```bash
# 1. Go to the landing page folder
# 1. Navigate to the landing page folder
cd landing-page

# 2. Install dependencies
npm install

# 3. Start the dev server
# 3. Launch the development server
npm run dev
```

Then open **http://localhost:5173** in your browser.
Then open **`http://localhost:5173`** in your browser.

---

## 🧪 CI/CD Workflows

Pre-configured **GitHub Actions** workflows live in `.github/workflows/`:

| Workflow | What it does |
| --- | --- |
| 🤖 **Android CI** | Runs compilation checks on every pull request or push to `main` |
| 🌐 **Web Landing Page CI** | Installs Node dependencies and runs build tests to prevent breaking the website |
| Workflow | Trigger | Description |
| :--- | :--- | :--- |
| 🤖 **Android CI** | PR / Push to `main` | Runs lint checks & compiles debug APK to verify build integrity |
| 🌐 **Web Landing Page CI** | PR / Push to `main` | Installs dependencies and runs build tests to prevent regressions |
| 🚀 **Release Workflow** | Tag push (`v*`) | Automatically builds debug & unsigned release APKs and publishes a GitHub Release |

---

## 🤝 Contributing

Contributions are welcome!
Contributions are always welcome!

1. 🍴 Fork the project
1. 🍴 Fork the project (`https://github.com/Mahesh-011-vk/EchoFitAI`)
2. 🌿 Create your feature branch: `git checkout -b feature/AmazingFeature`
3. 💾 Commit your changes: `git commit -m 'Add some AmazingFeature'`
4. 📤 Push to the branch: `git push origin feature/AmazingFeature`
@@ -226,7 +233,7 @@ Contributions are welcome!

## 📄 License

Distributed under the **MIT License**. See `LICENSE` for more information.
Distributed under the **MIT License**. See [`LICENSE`](LICENSE) for more information.

---

@@ -241,8 +248,8 @@ Distributed under the **MIT License**. See `LICENSE` for more information.

<br/>

⭐ **If EchoFit helps you, give it a star!** ⭐
⭐ **If EchoFit helps you on your fitness journey, don't forget to give it a star!** ⭐

<img src="https://capsule-render.vercel.app/api?type=waving&color=0:7C4DFF,50:00B0FF,100:00C853&height=120&section=footer" alt="footer wave" />
<img src="https://capsule-render.vercel.app/api?type=waving&color=0:7C4DFF,50:00B0FF,100:00C853&height=120&section=footer" alt="footer wave" width="100%" />

</div>
give corrected code
