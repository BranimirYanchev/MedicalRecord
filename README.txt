Medical Report System (Spring Boot)

How to run
1) Start the database (MySQL)
- Install and open XAMPP
- Start: MySQL
- Open phpMyAdmin
- Create a new database named:
  medical_db
That’s it. No tables needed — the app will create everything automatically.

2) Run the Spring Boot app
- Open the project in IntelliJ (or another IDE)
- Run the main class:
  src/main/java/com/medicalreport/project/MedicalReportApplication.java
- The app starts on:
  http://localhost:8080

Notes
- On first startup, the app auto-creates tables and seeds required data (roles + admin user).

Default admin credentials
- Username: admin
- Password: admin123

Change admin credentials
- Open:
  src/main/java/com/medicalreport/project/config/DataInitializer.java
- Change:
  adminUsername
  adminPass
- Restart the application

Project structure (important parts)
src/main/java/com/medicalreport/project
- MedicalReportApplication.java  (entry point)
- config/
  - DataInitializer.java         (seed roles/admin)
  - SecurityConfig.java          (security rules)
- controller/                    (REST endpoints)
- service/
- repository/
- model/
- dto/
