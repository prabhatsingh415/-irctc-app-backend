# IRCTC App Backend

## 🚀 Project Overview

This repository contains the backend implementation for an IRCTC clone, designed to handle core railway ticketing operations. Built using Java, Servlets, and JDBC, it provides a robust API for user management, train search, ticket booking, and cancellation.

## 🛠 Tech Stack

- **Java**
- **Servlets**
- **JDBC (MySQL)**
- **Apache Tomcat**

## 🔗 Main Features

- **User Login & Registration**: Secure authentication using hashed passwords (BCrypt).
- **Train Search**: Find available trains based on source and destination.
- **Ticket Booking**: Book tickets, generate ticket files, and automatically email tickets to users.
- **Ticket Cancellation**: Cancel tickets and update seat availability.
- **Automatic Ticket File Deletion**: Ticket files are scheduled to be deleted 2 days after creation for security and cleanup.

## 📦 Project Structure

```
app/
├── src/
│   └── main/
│       ├── java/
│       │   └── org/
│       │       └── example/
│       │           ├── App.java                # Tomcat server initialization
│       │           ├── entities/User.java      # User entity
│       │           ├── services/TicketServices.java # Booking/cancellation logic
│       │           └── utilities/Utilities.java     # Helper functions
│       └── webapp/
│           ├── index.html
│           └── hello.html
```

## 📥 Setup & Installation

1. **Clone the repository**
    ```bash
    git clone https://github.com/prabhatsingh415/-irctc-app-backend.git
    cd -irctc-app-backend
    ```

2. **Configure Database**
   - Set up your MySQL database and update the JDBC connection details in the code.

3. **Build the project**
   - Use your preferred Java build tool (e.g., Gradle or Maven) to build the project.

4. **Deploy on Apache Tomcat**
   - Ensure Tomcat is installed.
   - Deploy the built `.war` or configure using the provided `App.java` as an embedded server.

5. **Access the Application**
   - Go to `http://localhost:8080/yourappname` in your browser.

## 🧑‍💻 Usage

- Register a user and log in.
- Search for trains using the web interface.
- Book tickets; receive ticket confirmation via email.
- Cancel tickets if needed.

## 🔒 Security

- Passwords are encrypted using BCrypt for secure authentication.
- Ticket files are automatically deleted after 2 days to prevent misuse.

## 📝 Author

**Prabhat Singh**

---

> For queries or contributions, please open an issue or submit a pull request.
