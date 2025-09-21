# Computer Room Fault Logger

**A JavaFX application for logging and tracking computer faults in college computer rooms**

---

## About This Project

This application helps manage computer problems in college computer labs. When something breaks (like a computer won't start or a mouse stops working), users can log the issue through this app instead of using paper forms or emails.

The system handles different types of users - teachers can report problems, technicians can see what needs fixing, and administrators can oversee everything. Each fault record includes details like which room it happened in, what the problem is, who reported it, and when it occurred.

---

## Technologies Used

- **Java + JavaFX** - For creating the desktop user interface
- **MySQL** - Database to store all fault records and user information
- **JDBC** - Connects the Java application to the MySQL database
- **MVC Architecture** - Organized code using Model-View-Controller pattern
  - Models handle the data
  - Controllers manage the logic
  - Views handle the user interface
- **FXML** - Used for designing the screen layouts

---

## Key Features

- **Multi-user system** with different access levels (Teacher, Technician, Administrator)
- **Fault logging** with details like room number, equipment type, and problem description
- **Status tracking** - faults can be opened, assigned to technicians, and marked as resolved
- **Room management** - handles multiple computer rooms across the college
- **Search and filter** options to find specific faults quickly

---

## How It Works

1. **Login** - Users authenticate with their credentials
2. **Report Faults** - Teachers/staff can log new computer problems
3. **Assign Work** - Administrators can assign faults to specific technicians
4. **Track Progress** - Everyone can see the status of reported issues
5. **Close Faults** - Technicians mark problems as resolved when fixed

---

## What I Learned Building This

- How to design and implement a desktop GUI using JavaFX and FXML
- Database design and management with MySQL
- Connecting Java applications to databases using JDBC
- Implementing the MVC architectural pattern for better code organization
- Managing different user roles and permissions in an application
- Handling CRUD operations (Create, Read, Update, Delete) for data management

---

## Areas for Future Improvement

If I were to continue developing this project, I would focus on:

- **Better Security** - Implement password hashing instead of plain text storage
- **Performance** - Add background threading using JavaFX `Task` to prevent UI freezing during database operations
- **Testing** - Write unit tests for DAO (Data Access Object) methods to ensure reliable database operations
- **Scalability** - Add pagination and advanced filtering for large numbers of fault records
- **Reporting** - Include export functionality for generating CSV or PDF reports
- **Notifications** - Email alerts when faults are assigned or resolved

---

## Technical Challenges Solved

- Designed a relational database schema to efficiently store fault and user data
- Implemented secure user authentication and role-based access control
- Created a responsive UI that handles real-time data updates
- Managed database connections properly to prevent memory leaks
- Organized complex application logic using the MVC pattern

---

**Author:** Dmytro Brodar  
**Project Type:** College Assignment  
**Year:** 2024
