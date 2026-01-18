# LostioApp - Spring Boot Backend

Simple and easy-to-understand Spring Boot backend with MongoDB for Lost and Found mobile application.

## 🚀 Technology Stack

- **Spring Boot 3.2.5** - Latest stable version
- **Spring Data MongoDB** - Database integration
- **Spring Security** - JWT authentication
- **Lombok** - Reduce boilerplate code
- **Maven** - Build tool

## 📋 Prerequisites

Before running the application, make sure you have:

- **Java 17** or higher installed
- **Maven 3.6+** installed
- **MongoDB** installed and running

## 🛠️ Setup Instructions

### 1. Install MongoDB

**For macOS:**
```bash
brew install mongodb-community
brew services start mongodb-community
```

**For Ubuntu/Debian:**
```bash
sudo apt update
sudo apt install mongodb
sudo systemctl start mongodb
```

**For Windows:**
Download and install from [MongoDB Official Website](https://www.mongodb.com/try/download/community)

### 2. Verify MongoDB is Running
```bash
# Check if MongoDB is running
mongosh
# or
mongo
```

### 3. Navigate to Backend Directory
```bash
cd backend
```

### 4. Run the Application
```bash
mvn spring-boot:run
```

The server will start on `http://localhost:8080`

## 🌐 API Endpoints

### Authentication (Public)

#### Register User
```http
POST /api/auth/register
Content-Type: application/json

{
  "name": "John Doe",
  "email": "john@example.com",
  "password": "password123",
  "phone": "1234567890"
}
```

#### Login User
```http
POST /api/auth/login
Content-Type: application/json

{
  "email": "john@example.com",
  "password": "password123"
}
```

**Response:**
```json
{
  "success": true,
  "message": "Login successful",
  "data": {
    "user": {
      "id": "...",
      "name": "John Doe",
      "email": "john@example.com",
      "phone": "1234567890"
    },
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
  }
}
```

### Reports (Protected - Requires JWT Token)

All protected endpoints require `Authorization: Bearer <token>` header.

#### Create Report
```http
POST /api/reports
Authorization: Bearer <token>
Content-Type: application/json

{
  "title": "Lost iPhone 13",
  "description": "Lost at the park",
  "location": "Central Park, NY",
  "photoUrl": "https://example.com/photo.jpg",
  "userId": "user123",
  "userName": "John Doe",
  "status": "LOST"
}
```

#### Get All Reports
```http
GET /api/reports
Authorization: Bearer <token>
```

#### Get Single Report
```http
GET /api/reports/{id}
Authorization: Bearer <token>
```

#### Update Report
```http
PUT /api/reports/{id}
Authorization: Bearer <token>
Content-Type: application/json

{
  "title": "Updated Title",
  "description": "Updated Description",
  "location": "New Location",
  "photoUrl": "https://example.com/new-photo.jpg",
  "status": "FOUND"
}
```

#### Delete Report
```http
DELETE /api/reports/{id}
Authorization: Bearer <token>
```

#### Search Reports
```http
GET /api/reports/search?query=iPhone
Authorization: Bearer <token>
```

### Claims (Protected)

#### Create Claim
```http
POST /api/claims
Authorization: Bearer <token>
Content-Type: application/json

{
  "reportId": "report123",
  "claimantId": "user456",
  "claimantName": "Jane Smith",
  "description": "This is my phone, I can prove it"
}
```

#### Get Claims for a Report
```http
GET /api/claims/report/{reportId}
Authorization: Bearer <token>
```

#### Get Claims by User
```http
GET /api/claims/user/{claimantId}
Authorization: Bearer <token>
```

### Chat (Protected)

#### Send Message
```http
POST /api/chat
Authorization: Bearer <token>
Content-Type: application/json

{
  "senderId": "user123",
  "receiverId": "user456",
  "content": "Hello, is this still available?"
}
```

#### Get Messages
```http
GET /api/chat?senderId=user123&receiverId=user456
Authorization: Bearer <token>
```

## 📁 Project Structure

```
backend/
├── src/main/java/com/lostio/
│   ├── LostioApplication.java       # Main application class
│   ├── model/
│   │   ├── User.java                # User model
│   │   ├── Report.java              # Report model
│   │   ├── Claim.java               # Claim model
│   │   └── Message.java             # Message model
│   ├── repository/
│   │   ├── UserRepository.java      # User repository
│   │   ├── ReportRepository.java    # Report repository
│   │   ├── ClaimRepository.java     # Claim repository
│   │   └── MessageRepository.java   # Message repository
│   ├── controller/
│   │   ├── AuthController.java      # Authentication endpoints
│   │   ├── ReportController.java    # Report endpoints
│   │   ├── ClaimController.java     # Claim endpoints
│   │   └── ChatController.java      # Chat endpoints
│   ├── service/
│   │   ├── UserService.java         # User service
│   │   ├── ReportService.java       # Report service
│   │   └── JwtService.java          # JWT service
│   └── config/
│       └── SecurityConfig.java      # Security configuration
├── src/main/resources/
│   └── application.properties       # Application configuration
└── pom.xml                          # Maven dependencies
```

## 🔧 Configuration

Edit `src/main/resources/application.properties`:

```properties
# Server Port
server.port=8080

# MongoDB URI (change if using remote MongoDB)
spring.data.mongodb.uri=mongodb://localhost:27017/lostioapp

# JWT Secret (change in production!)
jwt.secret=mySecretKey123456789abcdefghijklmnopqrstuvwxyz
jwt.expiration=86400000

# File Upload Size
spring.servlet.multipart.max-file-size=5MB
```

## 🧪 Testing with cURL

### Register
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"name":"John Doe","email":"john@example.com","password":"pass123","phone":"1234567890"}'
```

### Login
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"john@example.com","password":"pass123"}'
```

### Get Reports (with token)
```bash
curl -X GET http://localhost:8080/api/reports \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"
```

## 🐛 Troubleshooting

### MongoDB Connection Issues
- Ensure MongoDB is running: `brew services list` (macOS) or `systemctl status mongodb` (Linux)
- Check MongoDB connection string in `application.properties`
- Verify port 27017 is not blocked

### Port Already in Use
- Change port in `application.properties`: `server.port=8081`

### Build Errors
```bash
# Clean and rebuild
mvn clean install

# Skip tests if needed
mvn spring-boot:run -DskipTests
```

## 📝 Features

- ✅ Simple JWT authentication
- ✅ User registration and login
- ✅ CRUD operations for reports
- ✅ Search functionality
- ✅ Claim management
- ✅ Simple chat/messaging
- ✅ MongoDB integration
- ✅ BCrypt password encryption
- ✅ CORS enabled for React Native frontend

## 🔐 Security

- All endpoints except `/api/auth/**` require JWT token
- Passwords are encrypted using BCrypt
- JWT tokens expire after 24 hours
- Stateless session management

## 📱 Frontend Integration

This backend works with the React Native frontend. Make sure to:

1. Update API URLs in frontend to point to `http://localhost:8080`
2. Store JWT token after login
3. Include token in Authorization header for all protected requests

## 🤝 Contributing

This is a simple educational project. Feel free to:
- Add more features
- Improve error handling
- Add validation
- Enhance security

## 📄 License

MIT License - Feel free to use for learning!

---

**Happy Coding! 🚀**
