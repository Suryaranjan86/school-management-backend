# Architecture Overview - Database Configuration

## System Architecture

```
┌─────────────────────────────────────────────────────────────────────┐
│                     Spring Boot Application                          │
│                                                                       │
│  ┌──────────────────────────────────────────────────────────────┐   │
│  │              Application.properties                          │   │
│  │  - spring.profiles.active = dev (default) or prod          │   │
│  │  - spring.sql.init.mode = always                            │   │
│  │  - spring.sql.init.data-locations = classpath:data.sql     │   │
│  └──────────────────────────────────────────────────────────────┘   │
│                            │                                         │
│                            ▼                                         │
│  ┌──────────────────────────────────────────────────────────────┐   │
│  │         Profile Selection (dev or prod)                     │   │
│  │                                                              │   │
│  │  Dev Profile              │        Prod Profile             │   │
│  │  ─────────────────────    │    ──────────────────────       │   │
│  │  application-dev.props    │    application-prod.props       │   │
│  │  - H2: mem:testdb         │    - PostgreSQL localhost:5432  │   │
│  │  - ddl-auto: create-drop  │    - ddl-auto: validate         │   │
│  │  - H2 console: enabled    │    - Connection pool: enabled   │   │
│  └──────────────────────────────────────────────────────────────┘   │
│       │                                        │                    │
│       ▼                                        ▼                    │
│  ┌──────────────────┐                  ┌──────────────────┐         │
│  │ H2 In-Memory DB  │                  │ PostgreSQL DB    │         │
│  │ (Development)    │                  │ (Production)     │         │
│  └──────────────────┘                  └──────────────────┘         │
│       │                                        │                    │
│       ▼                                        ▼                    │
│  ┌──────────────────────────────────────────────────────────────┐   │
│  │  SQL Script Initialization                                  │   │
│  │  ─────────────────────────────────────────                  │   │
│  │  1. Load schema.sql   → Create tables (students, teachers)  │   │
│  │  2. Load data.sql     → Insert sample data                  │   │
│  └──────────────────────────────────────────────────────────────┘   │
│       │                                        │                    │
│       ▼                                        ▼                    │
│  ┌──────────────────┐                  ┌──────────────────┐         │
│  │  Ready for API   │                  │  Ready for API   │         │
│  │  Testing         │                  │  Production      │         │
│  │ http://localhost │                  │ http://localhost │         │
│  │     :8080/       │                  │     :8080/       │         │
│  └──────────────────┘                  └──────────────────┘         │
│                                                                      │
└─────────────────────────────────────────────────────────────────────┘
```

## Startup Flow Diagram

### Development Mode (H2) - DEFAULT
```
START mvn spring-boot:run
        │
        ▼
Load application.properties
        │
        ├─ spring.profiles.active = dev ✓
        │
        ▼
Load application-dev.properties
        │
        ├─ spring.datasource.url = jdbc:h2:mem:testdb
        ├─ spring.jpa.hibernate.ddl-auto = create-drop
        ├─ spring.h2.console.enabled = true
        │
        ▼
Initialize H2 Database
        │
        ├─ Drop existing tables (create-drop)
        │
        ▼
Execute schema.sql
        │
        ├─ CREATE TABLE students
        ├─ CREATE TABLE teachers
        ├─ CREATE TABLE fees
        │
        ▼
Execute data.sql
        │
        ├─ INSERT INTO students (3 records)
        ├─ INSERT INTO teachers (3 records)
        ├─ INSERT INTO fees (3 records)
        │
        ▼
Verify tables & data
        │
        ├─ Hibernate validates schema
        │
        ▼
Application READY ✅
        │
        ├─ API available at http://localhost:8080
        ├─ H2 Console at http://localhost:8080/h2-console
```

### Production Mode (PostgreSQL)
```
START mvn spring-boot:run 
      -Dspring-boot.run.arguments="--spring.profiles.active=prod"
        │
        ▼
Load application.properties
        │
        ├─ spring.profiles.active = prod (override)
        │
        ▼
Load application-prod.properties
        │
        ├─ spring.datasource.url = jdbc:postgresql://localhost:5432/school_db
        ├─ spring.jpa.hibernate.ddl-auto = validate
        ├─ Connection pool configured
        │
        ▼
Connect to PostgreSQL
        │
        ├─ Establish connection to DB server
        ├─ Select database: school_db
        │
        ▼
Execute schema.sql
        │
        ├─ CREATE TABLE IF NOT EXISTS students
        ├─ CREATE TABLE IF NOT EXISTS teachers
        ├─ CREATE TABLE IF NOT EXISTS fees
        │
        ▼
Execute data.sql
        │
        ├─ INSERT INTO students (new records if not exist)
        ├─ INSERT INTO teachers (new records if not exist)
        ├─ INSERT INTO fees (new records if not exist)
        │
        ▼
Validate Schema
        │
        ├─ Hibernate validates entities vs. DB schema
        │
        ▼
Application READY ✅
        │
        ├─ API available at http://localhost:8080
        ├─ Data persists across restarts
```

## File Structure

```
school/
│
├── pom.xml
│   └── Dependencies:
│       ├── spring-boot-h2console
│       ├── spring-boot-starter-data-jpa
│       ├── spring-boot-starter-webmvc
│       ├── h2 (runtime)
│       └── postgresql (runtime) ← NEW
│
├── src/main/resources/
│   ├── application.properties ← UPDATED
│   │   └── Enables: SQL init mode, data loading
│   │
│   ├── application-dev.properties ← NEW
│   │   └── H2 configuration
│   │
│   ├── application-prod.properties ← NEW
│   │   └── PostgreSQL configuration
│   │
│   ├── schema.sql ← NEW
│   │   └── CREATE TABLE statements
│   │
│   └── data.sql ← NEW
│       └── INSERT statements
│
├── src/main/java/com/srs/school/
│   ├── entity/
│   │   ├── Student.java
│   │   ├── Teacher.java
│   │   └── Fee.java
│   ├── controller/
│   ├── service/
│   ├── repository/
│   └── config/
│
├── SETUP_COMPLETE.md ← This file
├── DATABASE_CONFIGURATION.md ← Detailed docs
└── QUICK_START.md ← Quick reference

```

## Database Schema

```
H2 / PostgreSQL Databases
│
├── students
│   ├── id (PK, Identity)
│   ├── name
│   ├── email
│   ├── contact_number
│   ├── course_enrolled
│   ├── enrollment_date
│   └── status
│
├── teachers
│   ├── id (PK, Identity)
│   ├── name
│   ├── email
│   ├── contact_number
│   ├── highest_qualification
│   ├── other_qualification
│   ├── subject
│   ├── joining_date
│   ├── permanent_address
│   ├── permanent_state
│   ├── permanent_district
│   ├── permanent_pin
│   ├── current_address
│   ├── current_state
│   ├── current_district
│   ├── current_pin
│   └── status
│
└── fees
    ├── id (PK, Identity)
    ├── student_id (FK → students.id)
    ├── amount
    ├── payment_date
    ├── type
    └── description
```

## Configuration Comparison Matrix

```
┌─────────────────────┬──────────────────┬─────────────────────┐
│ Setting             │ Development (H2) │ Production (PostgreSQL)
├─────────────────────┼──────────────────┼─────────────────────┤
│ Profile Name        │ dev              │ prod                │
│ Database Type       │ In-Memory        │ Persistent          │
│ URL                 │ jdbc:h2:mem:...  │ jdbc:postgresql://..│
│ DDL Auto            │ create-drop      │ validate            │
│ Schema Persistence  │ No (recreated)   │ Yes (persists)      │
│ Data Persistence    │ No (lost)        │ Yes (persists)      │
│ Connection Pool     │ Default          │ HikariCP (tuned)    │
│ Console Access      │ http://:.../h2   │ None                │
│ Use Case            │ Testing/Dev      │ Production          │
│ Startup Time        │ Fast (in-memory) │ Depends on DB size  │
│ Security            │ Not needed       │ Required            │
└─────────────────────┴──────────────────┴─────────────────────┘
```

## SQL Script Loading Sequence

```
Spring Boot Startup
│
├─ Initialize DataSource
│  └─ Load application-{profile}.properties
│
├─ Initialize Schema
│  ├─ Check spring.sql.init.mode
│  └─ Execute Hibernate DDL (based on ddl-auto)
│
├─ Load SQL Scripts (spring.sql.init.mode=always)
│  ├─ schema.sql or schema-{platform}.sql
│  └─ data.sql or data-{platform}.sql
│
├─ Verify Database
│  ├─ Validate tables exist
│  └─ Validate data inserted
│
└─ Application Ready
   └─ All SQL scripts loaded, ready for API calls
```

## Quick Command Reference

```bash
# Development (H2) - DEFAULT
mvn clean spring-boot:run

# Production (PostgreSQL)
mvn clean spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=prod"

# With Maven settings
mvn clean spring-boot:run -Dspring.profiles.active=prod

# With environment variable
export SPRING_PROFILES_ACTIVE=prod
mvn clean spring-boot:run

# Direct JAR execution
java -jar target/school-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
```

## Integration Points

```
┌──────────────────────────────────────────┐
│  REST API Controller                     │
│  (StudentController, TeacherController)  │
└──────────────────────────────────────────┘
                      │
                      ▼
┌──────────────────────────────────────────┐
│  Service Layer                           │
│  (StudentService, TeacherService)        │
└──────────────────────────────────────────┘
                      │
                      ▼
┌──────────────────────────────────────────┐
│  Repository Layer (Spring Data JPA)      │
│  (StudentRepository, TeacherRepository)  │
└──────────────────────────────────────────┘
                      │
                      ▼
┌──────────────────────────────────────────┐
│  JPA/Hibernate                           │
│  (Entity Mapping)                        │
└──────────────────────────────────────────┘
                      │
                      ▼
┌──────────────────────────────────────────┐
│  Database                                │
│  ├─ H2 (Development)                     │
│  └─ PostgreSQL (Production)              │
│                                          │
│  Tables populated by:                    │
│  ├─ schema.sql (CREATE TABLE)            │
│  └─ data.sql (INSERT data)               │
└──────────────────────────────────────────┘
```

---

This architecture ensures that:
✅ SQL scripts are automatically loaded on startup
✅ Both development and production databases are supported
✅ Schema is created and data is populated without manual intervention
✅ Different profiles use appropriate database settings
✅ Easy switching between H2 and PostgreSQL

