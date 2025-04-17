# 🚗 Car Rental System

A Java-based Car Rental System built with **Java Swing** for the GUI and **JDBC** for database connectivity. This application allows users to register, login, book cars, process payments, and view booking history — all integrated in one smooth user experience.

---

## 📌 Features

- **User Authentication**
  - Secure login and registration
  - Validates user credentials with Oracle DB

- **Car Booking**
  - View available cars
  - Book a car for a selected rental period
  - Booking stored in the database

- **Payment Processing**
  - Supports multiple payment methods
  - Updates booking status after successful payment

- **Booking Management**
  - View current and past bookings
  - Retrieve booking history with filtering

---

## 🛠️ Technologies Used

- **Java**
- **Java Swing** (GUI)
- **JDBC** (Database Connectivity)
- **Oracle Database**

---

## 🏗️ System Architecture

- **Presentation Layer**: Java Swing (`LoginFrame`, `HomeFrame`, etc.)
- **Business Logic Layer**: Handles validations and flow control
- **Data Access Layer**: JDBC connectivity via `DBConnection.java`

---

## 🗃️ Database Tables

1. **Users** — Stores user info (`email`, `username`, `password`)
2. **Cars** — Car details (`car_id`, `make`, `model`, `year`, `rental_price_per_day`)
3. **Bookings** — Booking info (`booking_id`, `user_email`, `car_id`, dates, `status`)
4. **Payments** — Payment data (`payment_date`, `amount`, `method`, `status`)

---

## 📸 Screenshots

> Add your application screenshots here once available!

---

## 🚀 Getting Started

### Prerequisites

- Java (JDK 8 or higher)
- Oracle Database
- JDBC Driver
- Java IDE (e.g., Eclipse or IntelliJ)

### Setup

1. Clone the repository:
   ```bash
   git clone https://github.com/your-username/car-rental-system.git
   cd car-rental-system
