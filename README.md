Computer Room Fault Logger
A JavaFX application for logging and tracking computer faults in college computer rooms
Setup Instructions
Requirements

Java 11 or higher
JavaFX 21
MySQL Server 5.7+

Database Setup

Install and start MySQL Server
Run the database setup script:
mysql -u root -p < database/database_setup.sql

Running the Application

Configure JavaFX module path in your IDE
Add VM options: --module-path /path/to/javafx/lib --add-modules javafx.controls,javafx.fxml
Run the Main class

Login Credentials

Admin: admin / admin123
Technician: tech1 / tech123
Teacher: teacher1 / teach123

Key Features

Multi-user system with role-based access (Teacher, Technician, Administrator)
Fault logging with room details and problem descriptions
Status tracking (open, in progress, resolved)
Room and computer management
MySQL database integration

Technologies Used

Java + JavaFX - Desktop GUI
MySQL + JDBC - Database connectivity
FXML - UI layouts
MVC Architecture - Code organization

Database Structure

DBLogUsers - User authentication and roles
DBLogRooms - Computer room information
DBLogComputer - Individual computer details

Technical Challenges Solved

JavaFX module configuration with newer Java versions
Database connection management and JDBC integration
Role-based authentication system
Relational database design for fault tracking

Author: Dmytro Brodar
