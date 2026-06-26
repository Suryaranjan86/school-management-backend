-- sequences
CREATE SEQUENCE IF NOT EXISTS school_seq START WITH 1;
CREATE SEQUENCE IF NOT EXISTS user_seq START WITH 1;
CREATE SEQUENCE IF NOT EXISTS cls_seq START WITH 1;
CREATE SEQUENCE IF NOT EXISTS stu_seq START WITH 1;
CREATE SEQUENCE IF NOT EXISTS fee_seq START WITH 1;
CREATE SEQUENCE IF NOT EXISTS receipt_seq START 1;
CREATE SEQUENCE IF NOT EXISTS stu_aca_rec_seq START 1;

-- tables

CREATE TABLE IF NOT EXISTS school  (
    id VARCHAR(50) PRIMARY KEY DEFAULT 'sch-' || nextval('school_seq'),
    name VARCHAR(255) NOT NULL,
    address VARCHAR(500),
    state VARCHAR(100),
    district VARCHAR(100),
    pin VARCHAR(10),
    phone VARCHAR(20),
    payment_mode VARCHAR(10) CHECK (payment_mode IN ('HALF_YEARLY', 'QUARTERLY', 'MONTHLY', 'YEARLY')),
    session_start_month VARCHAR(20) NOT NULL,
    session_end_month VARCHAR(20) NOT NULL,
    logo_url TEXT
);

CREATE TABLE IF NOT EXISTS users (
    id VARCHAR(50) PRIMARY KEY DEFAULT 'usr-' || nextval('user_seq'),
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255),
    username VARCHAR(100),
    password VARCHAR(100),
    role VARCHAR(10) CHECK (role IN ('admin', 'user')),
    school_id VARCHAR(50) NOT NULL,
    FOREIGN KEY (school_id) REFERENCES school(id)
);

-- renamed from "class"
CREATE TABLE IF NOT EXISTS classes (
    id VARCHAR(50) PRIMARY KEY DEFAULT 'cls-' || nextval('cls_seq'),
    name VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS student (
    id VARCHAR(50) PRIMARY KEY ,
    name VARCHAR(255) NOT NULL,
    father_name VARCHAR(255) NOT NULL,
    mother_name VARCHAR(255) NOT NULL,
    date_of_birth timestamp(6) NOT NULL,
    address VARCHAR(500) NOT NULL,
    email VARCHAR(255),
    gender VARCHAR(20),
    school_id VARCHAR(50) NOT NULL,
    is_deleted VARCHAR(1) CHECK (is_deleted IN ('Y', 'N')) DEFAULT 'N',
    FOREIGN KEY (school_id) REFERENCES school(id)
);
CREATE TABLE IF NOT EXISTS student_academic_record (
    id VARCHAR(50) PRIMARY KEY ,
    academic_year VARCHAR(20),
    cls_id VARCHAR(50) NOT NULL,
    student_id VARCHAR(50) NOT NULL,
    class_roll_no VARCHAR(20) NOT NULL,
    is_current_academic_year VARCHAR(1) CHECK (is_current_academic_year IN ('Y', 'N')),
    FOREIGN KEY (cls_id) REFERENCES classes(id),
    FOREIGN KEY (student_id) REFERENCES student(id),
    CONSTRAINT uk_student_roll
    UNIQUE (academic_year, cls_id, student_id,class_roll_no)
);
CREATE TABLE IF NOT EXISTS fee_payment (
    id VARCHAR(50) PRIMARY KEY ,
    stu_aca_rec_id VARCHAR(50) NOT NULL,
    pay_month VARCHAR(20) NOT NULL,
    pay_year VARCHAR(50) NOT NULL,
    amount float(53) NOT NULL,
    receipt_no VARCHAR(50) UNIQUE,
    payment_date timestamp(6) NOT NULL,
    transaction_id VARCHAR(255) UNIQUE,
    offline_slip_no VARCHAR(255) UNIQUE,
    mode_of_pay VARCHAR(50) CHECK (mode_of_pay IN ('ONLINE', 'OFFLINE')),
    FOREIGN KEY (stu_aca_rec_id) REFERENCES student_academic_record(id)
);
