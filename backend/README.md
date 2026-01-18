# LostioApp Backend

A comprehensive Spring Boot backend for the Lost and Found mobile application.

## Features

- **JWT Authentication & Authorization** - Secure user authentication with role-based access control
- **User Management** - Complete user profile management with avatar uploads
- **Lost/Found Reports** - Create, update, search, and manage lost/found item reports
- **Claims Management** - Allow users to claim items with approval workflow
- **Real-time Chat** - WebSocket-based messaging between users
- **File Uploads** - Cloudinary integration for image uploads
- **API Documentation** - Interactive Swagger/OpenAPI documentation
- **Exception Handling** - Standardized error responses
- **Pagination & Filtering** - Efficient data retrieval

## Technology Stack

- **Spring Boot 3.2.5** - Application framework
- **Spring Security** - Authentication and authorization
- **Spring Data JPA** - Data persistence
- **PostgreSQL** - Database
- **JWT (jjwt 0.11.5)** - Token-based authentication
- **Cloudinary** - Cloud-based image storage
- **Lombok** - Reduce boilerplate code
- **ModelMapper** - DTO conversions
- **SpringDoc OpenAPI** - API documentation
- **Maven** - Dependency management

## Prerequisites

- Java 17 or higher
- Maven 3.6+
- PostgreSQL 12+
- Cloudinary account (optional, for image uploads)

## Getting Started

### 1. Clone the repository

```bash
git clone https://github.com/uday-2018v/lostioApp.git
cd lostioApp/backend
```

### 2. Configure Database

Create a PostgreSQL database:

```sql
CREATE DATABASE lostioapp;
```

Update `src/main/resources/application.properties` with your database credentials:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/lostioapp
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### 3. Configure Cloudinary (Optional)

Set environment variables for Cloudinary:

```bash
export CLOUDINARY_CLOUD_NAME=your_cloud_name
export CLOUDINARY_API_KEY=your_api_key
export CLOUDINARY_API_SECRET=your_api_secret
```

Or update `application.properties`:

```properties
cloudinary.cloud-name=your_cloud_name
cloudinary.api-key=your_api_key
cloudinary.api-secret=your_api_secret
```

### 4. Build the application

```bash
mvn clean install
```

### 5. Run the application

```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## API Documentation

Once the application is running, access the interactive API documentation at:

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/v3/api-docs

## API Endpoints

### Authentication

- `POST /api/auth/register` - Register new user
- `POST /api/auth/login` - Login user (returns JWT token)
- `GET /api/auth/me` - Get current user profile
- `POST /api/auth/forgot-password` - Initiate password reset
- `POST /api/auth/reset-password` - Reset password with token

### Users

- `GET /api/users` - Get all users
- `GET /api/users/{id}` - Get user by ID
- `PUT /api/users/{id}` - Update user profile
- `POST /api/users/{id}/avatar` - Upload user avatar

### Reports

- `POST /api/reports` - Create new report
- `GET /api/reports` - Get all reports (paginated)
- `GET /api/reports/{id}` - Get report by ID
- `PUT /api/reports/{id}` - Update report
- `DELETE /api/reports/{id}` - Delete report
- `GET /api/reports/search` - Search reports by keyword
- `POST /api/reports/{id}/photo` - Upload report photo

### Claims

- `POST /api/claims` - Create new claim
- `GET /api/claims` - Get all claims (paginated)
- `GET /api/claims/{id}` - Get claim by ID
- `GET /api/claims/report/{reportId}` - Get claims for specific report
- `PUT /api/claims/{id}/status` - Update claim status
- `DELETE /api/claims/{id}` - Delete claim

### Chat

- `POST /api/chat` - Send message
- `GET /api/chat` - Get messages between two users
- `GET /api/chat/conversations/{userId}` - Get all conversations for user
- `WS /ws/chat` - WebSocket endpoint for real-time chat

## Authentication

Most endpoints require JWT authentication. Include the token in the Authorization header:

```
Authorization: Bearer <your_jwt_token>
```

### Example Login Flow

1. Register a new user:
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "email": "john@example.com",
    "password": "password123",
    "phone": "1234567890",
    "location": "New York"
  }'
```

2. Login to get JWT token:
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john@example.com",
    "password": "password123"
  }'
```

3. Use the token for authenticated requests:
```bash
curl -X GET http://localhost:8080/api/reports \
  -H "Authorization: Bearer <your_jwt_token>"
```

## Database Schema

### Users
- id, email, password, name, phone, location, avatarUrl, role, createdAt, updatedAt

### Reports
- id, title, description, location, photoUrl, userId, userName, status, time, createdAt, updatedAt

### Claims
- id, reportId, claimerId, claimerName, claimDescription, status, createdAt, updatedAt

### Messages
- id, senderId, receiverId, message, timestamp, isRead

## Error Handling

All errors are returned in a standardized format:

```json
{
  "success": false,
  "message": "Error message",
  "error": "Detailed error description",
  "timestamp": "2026-01-18T10:00:00Z"
}
```

## Security

- Passwords are encrypted using BCrypt
- JWT tokens expire after 24 hours (configurable)
- CORS is configured for React Native frontend
- Role-based access control (USER, ADMIN)

## Testing

Run tests with:

```bash
mvn test
```

## Health Check

Check application health:

```
GET /actuator/health
```

## Environment Variables

| Variable | Description | Default |
|----------|-------------|---------|
| `CLOUDINARY_CLOUD_NAME` | Cloudinary cloud name | - |
| `CLOUDINARY_API_KEY` | Cloudinary API key | - |
| `CLOUDINARY_API_SECRET` | Cloudinary API secret | - |

## Production Deployment

1. Update `application.properties` for production:
   - Change `jwt.secret` to a strong random key
   - Set `spring.jpa.hibernate.ddl-auto=validate`
   - Configure proper logging levels
   - Use environment variables for sensitive data

2. Build production JAR:
```bash
mvn clean package -DskipTests
```

3. Run the JAR:
```bash
java -jar target/lostioapp-backend-1.0.0.jar
```

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License

This project is licensed under the Apache License 2.0.

## Support

For issues and questions, please open an issue on GitHub or contact support@lostioapp.com.
