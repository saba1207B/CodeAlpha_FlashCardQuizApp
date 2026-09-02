# 📚 Flashcard Quiz App

A simple, clean, and interactive Android Flashcard Quiz App developed as **Task 1 of the CodeAlpha App Development Internship**.

The app helps users study using digital flashcards: view a question, reveal the answer, and move between cards using Previous and Next navigation. Users can also manage their own flashcards by adding, editing, and deleting cards.

## ✨ Features

- 🃏 **Flashcard-based learning** — Display a question on the front of a card and reveal its answer.
- 👁️ **Show Answer** — Reveal the answer with a dedicated action.
- ⬅️ **Previous / Next navigation** — Move easily through the flashcard collection.
- ➕ **Add flashcards** — Create new question-and-answer cards.
- ✏️ **Edit flashcards** — Update existing flashcards.
- 🗑️ **Delete flashcards** — Remove flashcards that are no longer needed.
- 📱 **Android application** — Packaged as an installable APK.
- 🎨 **Clean and simple UI** — Designed for straightforward studying and navigation.

## 🛠️ Technology Stack

- **Platform:** Android
- **Language:** Kotlin
- **Build System:** Gradle / Kotlin DSL
- **Project Structure:** Standard Android application project
- **Source:** Kotlin + Android resources
- **CI/CD:** GitHub Actions

## 📂 Project Structure

```text
CodeAlpha_FlashCardQuizApp/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   ├── res/
│   │   │   └── AndroidManifest.xml
│   │   ├── test/
│   │   └── androidTest/
│   ├── build.gradle.kts
│   └── proguard-rules.pro
├── .github/
│   └── workflows/
│       └── android.yml
├── build.gradle.kts
├── gradle.properties
├── settings.gradle.kts
└── metadata.json
```

## 🚀 How to Build

### Option 1 — Android Studio

1. Clone or download this repository.
2. Open the project in Android Studio.
3. Allow Gradle to sync and download required dependencies.
4. Select the `app` configuration.
5. Build the project or run it on an Android device/emulator.

### Option 2 — Gradle

From the project root, run:

```bash
./gradlew assembleDebug
```

On Windows:

```powershell
.\gradlew.bat assembleDebug
```

The generated debug APK will normally be available at:

```text
app/build/outputs/apk/debug/app-debug.apk
```

## 📦 APK

A debug APK is built automatically by the repository's GitHub Actions workflow.

The workflow builds the Android application and uploads the generated `app-debug.apk` as the **flashcard-quiz-app** artifact.

To obtain the latest CI-built APK:

1. Open the repository on GitHub.
2. Go to **Actions**.
3. Open the latest successful Android workflow run.
4. Download the **flashcard-quiz-app** artifact.
5. Extract the artifact to access the APK.

## 🔄 Continuous Integration

GitHub Actions is configured to automatically build the Android project. This helps verify that the project can be compiled consistently and provides a downloadable debug APK artifact from successful workflow runs.

## 🎯 CodeAlpha Internship

**Program:** CodeAlpha App Development Internship  
**Task:** Task 1 — Flashcard Quiz App

This project was created to satisfy the Flashcard Quiz App requirements provided for the CodeAlpha App Development Internship.

### Task Requirements Covered

| Requirement | Implementation |
|---|---|
| Flashcard question and answer | ✅ |
| Show Answer button/action | ✅ |
| Next button | ✅ |
| Previous button | ✅ |
| Add flashcards | ✅ |
| Edit flashcards | ✅ |
| Delete flashcards | ✅ |
| Simple, clean UI | ✅ |
| Complete source code on GitHub | ✅ |
| Android APK | ✅ |

## 📸 Screenshots

Screenshots can be added here to demonstrate the main flashcard screen and flashcard management interface.

## 📥 Clone the Repository

```bash
git clone https://github.com/saba1207B/CodeAlpha_FlashCardQuizApp.git
cd CodeAlpha_FlashCardQuizApp
```

## 👨‍💻 Developer

**Sabareesh**  
GitHub: [@saba1207B](https://github.com/saba1207B)

## 📄 License

This project was developed for educational and internship purposes as part of the CodeAlpha App Development Internship.
