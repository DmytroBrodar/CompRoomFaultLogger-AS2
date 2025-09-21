# Computer Room Fault Logger

**A simple JavaFX app to log and track faults in computer rooms**

---

## What is this project?

This app lets users (like teachers or technicians) report problems (faults) in computer rooms.  
The app records details like which room, what the problem is, who reported it, and when.  
Different user roles can see and manage faults (open, assign to technician, close).  

---

## Technologies used

- Java + JavaFX for the user interface  
- MySQL database to store fault records  
- JDBC to connect Java ↔ MySQL  
- MVC pattern (Model-View-Controller) to organise code (models for data, controllers for logic, view for UI)  
- FXML for layout of screens  

---

## How the code is organised (classes / modules)

| Package / File | Purpose |
|----------------|---------|
| `Main.java` | Starts the app, loads the first screen (login) |
| `Fault` (model) | Data for a fault: room, description, status, reporting user, timestamps |
| `FaultDAO` | Data Access Object: methods to add, fetch, update fault records from database |
| `LoginController` | Handles user sign-in logic: check credentials, choose screen by role |
| `MainController` (or similar) | After login: show list of faults, allow adding, assigning, closing faults |
| `DBManager` | Utility for database connection (open / close connections) |
| Other controllers | Screens for technician/teacher roles, handling UI events (buttons, table views) |

---

## How to run it

1. Install Java (JDK 11 or higher recommended).  
2. Install MySQL and create a database, set up fault table (id, room, description, status, reported_by, assigned_to, created_at, updated_at).  
3. Change database URL, username, password in `DBManager.java` to match your MySQL setup.  
4. Build the project (in your IDE or using `javac` / build tool).  
5. Run `Main.java` → login screen appears → use UI to add, view, update faults.

---

## Things I would improve

- Add user authentication with hashed passwords instead of storing plain text.  
- Add unit tests for DAO methods to check database operations.  
- Use background threads (JavaFX `Task`) for database operations so UI doesn’t freeze.  
- Add pagination or filtering in the fault table if many records.  
- Maybe export reports (CSV or PDF) of faults.  

---

## What I learned

- How to work with JavaFX for UI and FXML layout.  
- How to connect Java to MySQL using JDBC.  
- How to separate responsibilities (UI, Data, Logic) using MVC.  
- Handling simple CRUD operations (create, read, update, delete).  

---

**Author:** Dmytro Brodar  
