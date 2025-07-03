# 🚕 QuickPick - Ridesharing Android App

QuickPick is a simple ridesharing Android application that connects **riders** with available **drivers** based on destination preferences. 
Built with **Java**, **XML**, and **Firebase**, the app provides a real-time, minimal, and intuitive experience for both riders and drivers.

---

## 📱 Features

### 👤 User Registration & Login
- Role-based authentication for **Riders** and **Drivers**
- Firebase Authentication used for secure login and signup
- User data stored in Firebase Realtime Database

### 🧭 Rider Module
- Rider's **current location** is automatically fetched using GPS
- Riders manually enter their destination
- After submission, riders see a list of available drivers going to the same destination
- Displays driver details: **Name**, **Contact Number**, **Vehicle Number**
- Option to confirm the ride

### 🚗 Driver Module
- Drivers enter a preferred destination during registration
- On first login, they are prompted to enter:
  - Full Name
  - Contact Number
  - Vehicle Number
- Driver details are stored and shown to matching riders

### 🔐 Tech Stack
- Android (Java, XML)
- Firebase Authentication
- Firebase Realtime Database
- Google Maps SDK (Planned for future)
