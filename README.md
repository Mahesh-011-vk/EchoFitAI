<div align="center">

[![Platform](https://img.shields.io/badge/Platform-Android-green.svg?style=flat-square)](#-tech-stack)
[![Language](https://img.shields.io/badge/Language-Kotlin%20%2F%20TypeScript-blue.svg?style=flat-square)](#-tech-stack)
[![License](https://img.shields.io/badge/License-MIT-orange.svg?style=flat-square)](#-license)

<img src="https://capsule-render.vercel.app/api?type=waving&color=0:00C853,50:00B0FF,100:7C4DFF&height=220&section=header&text=EchoFit%20%F0%9F%9A%80&fontSize=64&fontColor=ffffff&fontAlignY=38&desc=Your%20AI-powered%20fitness%20%26%20nutrition%20companion&descAlignY=60&descSize=20" alt="EchoFit banner" width="100%" />

### Your AI-Powered Personal Fitness & Nutrition Companion

<img src="https://readme-typing-svg.demolab.com?font=Fira+Code&weight=600&size=20&pause=1200&color=00B0FF&center=true&vCenter=true&width=700&lines=Scan+your+meal.+Get+instant+macros.;Compare+your+physique+progress+with+AI.;Works+online+with+Gemini.+Works+offline+too." alt="Typing animation" />

<p align="center">
  <b>EchoFit</b> pairs a modern <b>Android app</b> built with Jetpack Compose & Material 3 with a sleek <b>interactive web companion</b>. Powered by <b>Google Gemini API</b> online with an intelligent on-device <b>Naive Bayes</b> offline fallback.
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Platform-Android-3DDC84?style=for-the-badge&logo=android&logoColor=white" alt="Platform Android" />
  <img src="https://img.shields.io/badge/Kotlin-Jetpack%20Compose-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white" alt="Kotlin Jetpack Compose" />
  <img src="https://img.shields.io/badge/TypeScript-React-3178C6?style=for-the-badge&logo=typescript&logoColor=white" alt="TypeScript React" />
  <img src="https://img.shields.io/badge/Google-Gemini%20API-4285F4?style=for-the-badge&logo=googlegemini&logoColor=white" alt="Google Gemini" />
  <img src="https://img.shields.io/badge/Firebase-Firestore-FFCA28?style=for-the-badge&logo=firebase&logoColor=black" alt="Firebase" />
  <img src="https://img.shields.io/badge/License-MIT-FF9800?style=for-the-badge" alt="License MIT" />
</p>

**[📸 Screenshots](#-screenshots) · [🌟 Key Features](#-key-features) · [🛠 Tech Stack](#-tech-stack) · [🏗 Architecture](#-architecture) · [🚀 Getting Started](#-getting-started) · [🧪 CI/CD](#-cicd-workflows) · [🤝 Contributing](#-contributing)**

</div>

---

## 🎯 About

**EchoFit** is an advanced, AI-powered personal fitness and nutrition companion. It pairs a modern **Android app** for daily coaching and tracking with a sleek **web companion**.

> 🧠 **Smart online, resilient offline.**  
> The app uses the **Google Gemini API** for its visual meal scanner and physique comparison diagnostics. When network connectivity is unavailable, it falls back seamlessly to an on-device **Naive Bayes text classifier** and a rule-based nutritional estimation engine.

---

## 📸 Screenshots

<div align="center">

| 📊 Dashboard Interface | ⚙️ Settings & System Status |
| :---: | :---: |
| <img src="screenshot.png" alt="EchoFit Dashboard" width="340" /> | <img src="screenshot_settings.png" alt="EchoFit Settings" width="340" /> |

</div>

---

## 🌟 Key Features

<table>
<tr>
<td width="50%" valign="top">

### 🍽️ AI Diet Planner & Calorie Vision Scanner
Snap or upload a meal photo and Gemini Vision estimates:
- Food identity and portion sizes
- Calorie counts and macro breakdown (protein, carbs, fats, fiber)

📴 **Offline Mode:** Falls back automatically to an on-device classification and local recipe database when offline.

</td>
<td width="50%" valign="top">

### 💪 AI Physique Transformation Assessments
Safely upload and compare progress photos (**Front, Side, Back**). Gemini compares before-and-after captures to evaluate:
- Visual body fat percentage trends
- Muscle definition and toning analysis
- Motivational coaching insights

</td>
</tr>
<tr>
<td width="50%" valign="top">

### ✅ Daily Checklist & Gamification
Track **water hydration, workout sessions, meal logging, and sleep cycles**. Every achievement levels up your character tier (e.g. *Level 3 - Gym Warrior*) with gamified reward badges.

</td>
<td width="50%" valign="top">

### ⏰ WorkManager Reminder System
Automated, battery-efficient local background notifications scheduled precisely for **breakfast, lunch, afternoon snacks, dinner, and workouts**.

</td>
</tr>
<tr>
<td colspan="2" valign="top">

### ☁️ Firebase Cloud Sync
Reliable offline-first architecture with on-demand cloud sync that pushes local Room database entities securely into **Firebase Cloud Firestore**.

</td>
</tr>
</table>

---

## 🛠 Tech Stack

<div align="center">
  <img src="https://skillicons.dev/icons?i=kotlin,androidstudio,firebase,react,ts,vite,css,gradle,github,githubactions&theme=dark" alt="Tech Stack Icons" />
</div>

<br/>

### 📱 Android Application (`/app`)

| Area | Technology & Details |
| :--- | :--- |
| 🎨 **UI Framework** | Jetpack Compose & Material 3 (Rich aesthetics, dark theme, glassmorphic cards) |
| 🧑‍💻 **Language** | Kotlin |
| 🏛️ **Architecture** | MVVM with Flow, StateFlow, and Repository patterns |
| 💾 **Persistence** | Room Database (Local SQL persistence with type converters) |
| ⚡ **Concurrency** | Kotlin Coroutines & Structured Concurrency |
| ⏱️ **Background Work** | Android Jetpack WorkManager (Automated notification & reminder scheduler) |
| 🔄 **Database Sync** | Cloud Firestore via Firebase SDK (Synchronizes Room databases to cloud) |

**🤖 AI Engine**

| Mode | Details |
| :--- | :--- |
| 🌐 **Online** | Google Gemini API (via Google Generative AI SDK) for multimodal calorie vision scanning & comparative physique diagnostics |
| 📴 **Offline Fallback** | Custom **Naive Bayes text classifier** running 100% on-device coupled with a rule-based macro estimation engine |

### 💻 Web Landing Page (`/landing-page`)

| Area | Technology & Details |
| :--- | :--- |
| ⚛️ **Framework** | React 18 |
| ⚡ **Build System** | Vite |
| 🔷 **Language** | TypeScript |
| 🎨 **Styling** | Vanilla CSS (Zero bloated CSS runtime, ultra-fast performance) |

---

## 🏗 Architecture

```mermaid
flowchart TD
    U([🧑 User]) --> UI[🎨 Jetpack Compose UI<br/>Material 3]
    UI --> VM[🧠 ViewModels<br/>StateFlow / Flow]
    VM --> R[📦 Repository]
    R --> DB[(💾 Room Database)]
    R --> AI{🌐 Online?}
    AI -- Yes --> G[🤖 Gemini API<br/>Meal scan + Physique analysis]
    AI -- No --> NB[📴 Naive Bayes +<br/>Rule-based estimator]
    DB -. Force sync .-> FS[(☁️ Cloud Firestore)]
    WM[⏱️ WorkManager] --> N[🔔 Local reminders]

    style U fill:#7C4DFF,color:#fff,stroke:none
    style G fill:#4285F4,color:#fff,stroke:none
    style NB fill:#FF9800,color:#fff,stroke:none
    style FS fill:#FFCA28,color:#000,stroke:none
    style DB fill:#00C853,color:#fff,stroke:none
```

---

## 🚀 Getting Started

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
# 1. Navigate to the landing page folder
cd landing-page

# 2. Install dependencies
npm install

# 3. Launch the development server
npm run dev
```
Then open **`http://localhost:5173`** in your browser.

---

## 🧪 CI/CD Workflows

Pre-configured **GitHub Actions** workflows live in `.github/workflows/`:

| Workflow | Trigger | Description |
| :--- | :--- | :--- |
| 🤖 **Android CI** | PR / Push to `main` | Runs lint checks & compiles debug APK to verify build integrity |
| 🌐 **Web Landing Page CI** | PR / Push to `main` | Installs dependencies and runs build tests to prevent regressions |
| 🚀 **Release Workflow** | Tag push (`v*`) | Automatically builds debug & unsigned release APKs and publishes a GitHub Release |

---

## 🤝 Contributing

Contributions are always welcome!

1. 🍴 Fork the project (`https://github.com/Mahesh-011-vk/EchoFitAI`)
2. 🌿 Create your feature branch: `git checkout -b feature/AmazingFeature`
3. 💾 Commit your changes: `git commit -m 'Add some AmazingFeature'`
4. 📤 Push to the branch: `git push origin feature/AmazingFeature`
5. 🔀 Open a pull request

---

## 📄 License

Distributed under the **MIT License**. See [`LICENSE`](LICENSE) for more information.

---

<div align="center">

### 👤 Author

**Mahesh S**

[![GitHub](https://img.shields.io/badge/GitHub-Mahesh--011--vk-181717?style=for-the-badge&logo=github&logoColor=white)](https://github.com/Mahesh-011-vk)
[![LinkedIn](https://img.shields.io/badge/LinkedIn-Mahesh%20S-0A66C2?style=for-the-badge&logo=linkedin&logoColor=white)](https://linkedin.com/in/mahesh-s-318367203)

<br/>

⭐ **If EchoFit helps you on your fitness journey, don't forget to give it a star!** ⭐

<img src="https://capsule-render.vercel.app/api?type=waving&color=0:7C4DFF,50:00B0FF,100:00C853&height=120&section=footer" alt="footer wave" width="100%" />

</div>
