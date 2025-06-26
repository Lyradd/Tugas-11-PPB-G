# Tugas-11-PPB-G
# Starbucks Rewards App Clone (Jetpack Compose)

![Starbucks App Banner](https://github.com/user-attachments/assets/08dbcddc-1b3e-4131-8b86-b8ac73da3b1e)

A concept clone of the Starbucks mobile application, built entirely with **Jetpack Compose** for the Android platform. This project demonstrates a modern Android architecture using Kotlin, Coroutines, and the MVVM pattern to create a feature-rich, interactive, and visually appealing user interface.

## 🌟 Features

This application simulates the core functionalities of the Starbucks Rewards program, offering a seamless user experience.

- **🏠 Home Screen**: A dynamic dashboard displaying a welcome message, user's membership card with animated star count, quick actions, featured offers, and recent orders.
- **☕ Order Menu**: A searchable list of all available products, including store-specific specials.
- **🛍️ Product Details & Customization**: Users can view product details and customize their drinks by selecting different sizes and milk options before adding them to the cart.
- **🛒 Shopping Cart**: A fully functional cart where users can review items, adjust quantities, see the total price, and proceed to checkout.
- **💳 Payment & Balance**: Simulates payment by deducting from a user's balance. Includes a "Top-Up" feature with multiple payment method options.
- **⭐ Rewards System**: A dedicated screen to view available rewards. Users can redeem their rewards, which updates their status.
- **❤️ Favorites**: Users can mark products as favorites for quick access later from their profile or a dedicated favorites screen.
- **📜 Order History**: A comprehensive list of all past orders, showing items, total price, and date.
- **📍 Store Locator**: A list of nearby stores, displaying their status (Open/Closed) and unique special menu items.
- **👤 Profile Management**: A central hub for accessing payment methods, order history, favorites, settings, and help.
- **📷 QR Code Scanner**:
    - Displays a user's QR code for in-store scanning.
    - Integrates **CameraX** and **ML Kit** to open a live camera feed and scan QR codes for payments.
    - Handles camera permissions gracefully.
- **🎨 Dual-Theme Support**: Seamlessly switch between a sleek **Dark Mode** and a classic **Light Mode** from the settings screen.
- **🔔 Notifications**: A dedicated screen to view all app-related notifications and promotions.

---

## 📸 Screenshots

*(Here you can add your own screenshots or GIFs of the app in action)*

| Home Screen (Dark) | Order Screen | Product Detail |
| :---: |:---:|:---:|
| <img src="https://github.com/user-attachments/assets/08dbcddc-1b3e-4131-8b86-b8ac73da3b1e" alt="Home Screen" width="250"/> | <img src="https://github.com/user-attachments/assets/192cbc63-b955-4f1e-922d-75d50ea493df" alt="Order Screen" width="250"/> | <img src="https://github.com/user-attachments/assets/d1155f8e-df50-4c3c-aa99-0c8cea86d156" alt="Product Detail" width="250"/> |
| **Cart Screen** | **QR Scanner** | **Profile Screen** |
| <img src="https://github.com/user-attachments/assets/1ee24a22-d60a-494c-a21b-d8dd554d54ec" alt="Cart Screen" width="250"/> | <img src="https://github.com/user-attachments/assets/28fed553-d995-4271-b4bf-68d74bfea5ca" alt="QR Scanner" width="250"/> | <img src="https://github.com/user-attachments/assets/f410e3b2-b7c2-43fa-b298-d9d16f178dc8" alt="Profile Screen" width="250"/> |

---

## 🛠️ Tech Stack & Architecture

This project is built with a modern Android technology stack, emphasizing simplicity, scalability, and testability.

- **Language**: [**Kotlin**](https://kotlinlang.org/)
- **UI Framework**: [**Jetpack Compose**](https://developer.android.com/jetpack/compose) for a declarative and reactive UI.
- **Architecture**: **MVVM (Model-View-ViewModel)**
  - **View**: Composable functions in `MainActivity.kt` that observe state.
  - **ViewModel**: `StarbucksViewModel.kt` holds all business logic and exposes UI state using `State<T>`, `mutableStateListOf`, and `derivedStateOf`.
  - **Model**: Data classes (`User`, `Product`, `Order`, etc.) located in the `data` package.
- **State Management**: Uses Compose's native state holders (`remember`, `mutableStateOf`) and ViewModel state exposure patterns.
- **Asynchronous Operations**: [**Kotlin Coroutines**](https://kotlinlang.org/docs/coroutines-overview.html) and [**Flow**](https://kotlinlang.org/docs/flow.html) for managing background tasks, with a `Channel` for one-off events like showing a Snackbar.
- **Camera**: [**CameraX**](https://developer.android.com/training/camerax) for a lifecycle-aware camera API.
- **Machine Learning**: [**Google ML Kit Vision**](https://developers.google.com/ml-kit/vision/barcode-scanning) for fast and reliable QR code scanning.
- **UI Components**: [**Material 3**](https://m3.material.io/) for modern UI components, icons, and theming (`lightColorScheme`, `darkColorScheme`).
- **Animations**: Utilizes Compose's built-in Animation APIs (`AnimatedVisibility`, `animateIntAsState`, `AnimatedContent`) for a fluid user experience.

---

## 📂 Code Structure

The code is organized following standard MVVM practices.

```
.
├── 📄 com.example.starbuckmembership
│
├── 🏠 data             # Data models (Product, User, Order, etc.)
│
├── 🧠 viewmodel
│   └── StarbucksViewModel.kt  # Handles all logic and state management
│
└── 🎨 MainActivity.kt      # Main entry point, permission handling, and all UI Composables
```
- **`MainActivity.kt`**: The single activity that hosts the entire Jetpack Compose application. It is responsible for requesting camera permissions and setting up the main `StarbucksApp` composable, which acts as the navigation host. All screens and UI components are defined here as `@Composable` functions.
- **`StarbucksViewModel.kt`**: The core of the application's logic. It manages the state for every feature—from user data and product lists to the contents of the shopping cart. All user actions (e.g., adding to cart, placing an order, toggling a theme) are handled by functions within this ViewModel.
- **`data` package**: Contains all the Kotlin `data class` definitions that model the application's data, such as `Product`, `User`, `Order`, `Reward`, and `Store`.

---

## 🚀 How to Run

To get this project running on your local machine, follow these steps:

1.  **Clone the repository:**
    ```bash
    git clone [https://github.com/your-username/your-repository-name.git](https://github.com/your-username/your-repository-name.git)
    ```
2.  **Open in Android Studio:**
    -   Open Android Studio (preferably the latest stable version).
    -   Click on `File > Open` and select the cloned project folder.
3.  **Build the Project:**
    -   Let Android Studio sync the Gradle files.
    -   Build the project by clicking `Build > Make Project` or using the hammer icon.
4.  **Run the App:**
    -   Select an emulator or a physical device.
    -   Click the `Run` button (green play icon).
    -   **Note**: The QR code scanner feature requires camera access. You will be prompted to grant this permission when you first try to use the camera.

---

## 📄 License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

```
Copyright (c) 2024 [Your Name]

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:
...
