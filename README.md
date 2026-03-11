# CodeWars Android Client 🚀

A modern, robust Android application built with **Kotlin**, **Jetpack Compose**, and **MVVM Architecture**. This project serves as a showcase for integrating the CodeWars API using industry-standard libraries and clean architecture principles.

---

## 📱 Features

- **User Search:** Real-time search for CodeWars users by their username.
- **User Profile Details:** Comprehensive view of a user's stats, including rank, honor, and clan.
- **Paginated Challenge History:** Browse through a user's completed challenges with seamless infinite scrolling using **Paging 3**.
- **Authored Challenges:** View challenges created by a specific user.
- **Detailed Challenge View:** Deep dive into specific challenge descriptions, supported languages, and approval status.
- **Dark Mode Support:** Fully compatible with Material 3 theming.

---

## 🏗️ Architecture

The project follows the **Clean Architecture** principles and the **MVVM (Model-View-ViewModel)** pattern to ensure a scalable, maintainable, and testable codebase.

- **UI Layer:** Built entirely with **Jetpack Compose** for a reactive and declarative UI.
- **Domain Layer:** Business logic and data models.
- **Data Layer:** Repository pattern for managing data from both local (Room) and remote (Retrofit) sources.

---

## 🛠️ Tech Stack & Libraries

- **Language:** [Kotlin](https://kotlinlang.org/)
- **UI Framework:** [Jetpack Compose](https://developer.android.com/jetpack/compose) (Material 3)
- **Architecture:** MVVM, Repository Pattern
- **Dependency Injection:** [Dagger Hilt](https://dagger.dev/hilt/)
- **Networking:** [Retrofit 2](https://square.github.io/retrofit/) & [OkHttp](https://square.github.io/okhttp/)
- **Serialization:** [Moshi](https://github.com/square/moshi)
- **Database:** [Room](https://developer.android.com/training/data-storage/room) (for caching and offline support)
- **Pagination:** [Paging 3](https://developer.android.com/topic/libraries/architecture/paging/v3-paged-data)
- **Concurrency:** [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html) & [Flow](https://kotlinlang.org/docs/flow.html)
- **Lifecycle:** ViewModel, Lifecycle-runtime-compose

---

## 🚀 Getting Started

### Prerequisites
- Android Studio Ladybug | 2024.2.1 or newer
- JDK 17
- Android SDK 34+

### Installation
1. Clone the repository:
   ```bash
   git clone https://github.com/your-username/Android-MVVM.git
   ```
2. Open the project in Android Studio.
3. Sync Project with Gradle Files.
4. Run the app on an emulator or a physical device.

---

*Developed with ❤️ by [Faraz Nilawar]*
