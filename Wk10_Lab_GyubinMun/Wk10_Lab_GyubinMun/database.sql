CREATE DATABASE IF NOT EXISTS employment_db;
USE employment_db;
CREATE TABLE IF NOT EXISTS applications (
    application_id INT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    current_address VARCHAR(255) NOT NULL,
    contact_number VARCHAR(30) NOT NULL,
    email VARCHAR(100) NOT NULL,
    education VARCHAR(100) NOT NULL,
    gender VARCHAR(20) NOT NULL,
    date_available DATE NOT NULL,
    desired_position VARCHAR(100) NOT NULL,
    desired_salary DECIMAL(12,2) NOT NULL,
    authorized_to_work VARCHAR(3) NOT NULL,
    has_relatives VARCHAR(3) NOT NULL,
    explanation VARCHAR(255),
    signature VARCHAR(100) NOT NULL
);
