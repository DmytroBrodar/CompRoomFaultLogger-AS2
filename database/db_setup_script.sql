-- CompRoomFaultLogger Database Setup
-- Run this script to create the database structure and sample data

CREATE DATABASE IF NOT EXISTS computer_fault_logger;
USE computer_fault_logger;

-- Drop existing tables if they exist
DROP TABLE IF EXISTS DBLogComputer;
DROP TABLE IF EXISTS DBLogRooms;
DROP TABLE IF EXISTS DBLogUsers;

-- Create tables
CREATE TABLE DBLogRooms (
    roomNo VARCHAR(50) PRIMARY KEY, 
    totalnumofpc INT
);

CREATE TABLE DBLogComputer (
    pcId INT PRIMARY KEY AUTO_INCREMENT, 
    roomNo VARCHAR(50), 
    pcNo VARCHAR(50), 
    serialNo VARCHAR(50), 
    model VARCHAR(50), 
    manufacturer VARCHAR(50), 
    FOREIGN KEY (roomNo) REFERENCES DBLogRooms(roomNo)
);

CREATE TABLE DBLogUsers (
    username VARCHAR(50) PRIMARY KEY, 
    password VARCHAR(100), 
    fname VARCHAR(50), 
    sname VARCHAR(50), 
    role VARCHAR(20)
);

-- Insert sample data
INSERT INTO DBLogRooms (roomNo, totalnumofpc) VALUES 
('CL-101', 25), 
('CL-102', 20), 
('IT-201', 15), 
('LIB-301', 30), 
('SSL-105', 12);

INSERT INTO DBLogComputer (roomNo, pcNo, serialNo, model, manufacturer) VALUES 
('CL-101', 'PC-01', 'SN001', 'Dell OptiPlex', 'Dell'), 
('CL-101', 'PC-02', 'SN002', 'HP Compaq', 'HP'), 
('CL-102', 'PC-01', 'SN003', 'Lenovo ThinkCentre', 'Lenovo'), 
('IT-201', 'PC-01', 'SN004', 'Dell Inspiron', 'Dell');

INSERT INTO DBLogUsers (username, password, fname, sname, role) VALUES 
('admin', 'admin123', 'System', 'Administrator', 'Admin'), 
('tech1', 'tech123', 'John', 'Smith', 'Tech'), 
('teacher1', 'teach123', 'Mary', 'Wilson', 'Teacher');

-- Show confirmation
SELECT 'Database setup completed successfully!' as Status;
SELECT COUNT(*) as 'Total Rooms' FROM DBLogRooms;
SELECT COUNT(*) as 'Total Computers' FROM DBLogComputer;
SELECT COUNT(*) as 'Total Users' FROM DBLogUsers;