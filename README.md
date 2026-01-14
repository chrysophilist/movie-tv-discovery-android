# 🎬 Movie & TV Show Discovery App

An Android application built using **Jetpack Compose** that allows users to discover movies and TV shows using the [Watchmode API](https://api.watchmode.com/).
This project is developed as part of an internship interview assignment.

---

## 🗂️ Table of Contents

- [Features](#-features)
- [Architecture & Tech Stack](#-architecture--tech-stack)
- [Testing](#-testing)
- [APK](#-apk)
- [Setup Instructions](#-setup-instructions)
- [Assumptions Made](#-assumptions-made)
- [Challenges Faced](#-challenges-faced)
- [Experience](#-experience)
---

## 📱 Features

### Home Screen
- Displays a list of **Movies** and **TV Shows**
- Toggle between Movies & TV Shows using tabs
- Shimmer loading effect while data is being fetched

### Details Screen
- Displays:
  - Title
  - Description
  - Release date
  - Poster image
- Shimmer effect while loading details

### API Integration
- Fetches Movies and TV Shows title lists **simultaneously** using `Singles.zip`
- Uses **Retrofit** for network operations

### Error Handling
- Graceful error handling using **Snackbars / Toasts**
- Retry mechanism for network failures

---

## 🧱 Architecture & Tech Stack

### Architecture
- **MVVM (Model–View–ViewModel)**

### Tech Stack
- **Kotlin**
- **Jetpack Compose**
- **Retrofit**
- **RxJava / RxKotlin**
- **Koin (Dependency Injection)**
- **Material 3**
- **Coil** (for image loading)

---

## 🧪 Testing
- Unit tests written for ViewModels and data layer
- Test cases cover:
  - Successful API response
  - API error handling
  - Empty response
  - Network timeout scenario

(See [`Unit_Test_Cases.md`](./docs/Unit_Test_Cases_.md) for detailed documentation)

---

## 📦 APK
- A debug APK is provided for installation and testing
- APK can be found in the **Releases** section

---

## ⚙️ Setup Instructions

1. Clone the repository
   ```bash
   git clone https://github.com/chrysophilist/movie-tv-discovery-android.git
   ````

2. Open in **Android Studio**
3. Add your Watchmode API key in `local.properties`:

   ```kotlin
   WATCHMODE_API_KEY=your_api_key_here
   ```
4. Run the app on an emulator or physical device

---

## 🧠 Assumptions Made

- The Watchmode API responses are assumed to be **well-structured and consistent** with the documented schema.
- Pagination handling is limited to the initial pages, as infinite scrolling was not explicitly required in the assignment.
- A stable internet connection is assumed for normal app usage; offline caching is considered out of scope.
- Movies and TV Shows are differentiated using the `type` field provided by the API.
- The app is targeted for **modern Android devices** (minSDK 29 A10).
- The shimmer loading effect is implemented **manually using Jetpack Compose**, assuming that third-party shimmer libraries were not required.
- UI/UX design is intentionally kept **simple and functional**, with the assumption that visual improvements can be made after the assignment deadline.

---
## 🚧 Challenges Faced

- Handling simultaneous API calls
- Managing UI state for loading, success, and error
- Implementing **simultaneous API calls** using RxKotlin (`Singles.zip`) and managing their combined result effectively.
- Designing clean and maintainable **DTO-to-domain model mappings** to avoid leaking API models into the UI layer.
- Working with **Rx for the first time**, including understanding reactive streams, threading, and error propagation.
- Implementing a **custom shimmer loading effect manually using Jetpack Compose** for the first time, without relying on third-party libraries.
- Integrating **Koin for dependency injection** for the first time.

---

## 📘 Experience

A detailed reflection on what I learned while building this project is available here:
👉 [Experience.md](./docs/Experience.md)

---

## 👤 Author

**Prince**
