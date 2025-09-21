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
