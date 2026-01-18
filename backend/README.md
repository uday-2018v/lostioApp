# LostioApp Backend

Spring Boot REST API backend for the Lost and Found mobile application with MongoDB database.

## Features

- **Authentication & Authorization**: JWT-based authentication with role-based access control
- **User Management**: User registration, login, profile management, and avatar uploads
- **Report Management**: Create, read, update, and delete lost/found item reports with search functionality
- **Claim Management**: Users can claim lost items with approval workflow
- **Chat System**: Real-time messaging between users using WebSocket
- **File Upload**: Image upload support via Cloudinary integration
- **API Documentation**: Swagger/OpenAPI documentation
- **Security**: BCrypt password encryption, JWT token validation, CORS configuration

## Technology Stack

- **Spring Boot 3.2.1**
- **Spring Security** with JWT authentication
- **Spring Data MongoDB** for database operations
- **Spring WebSocket** for real-time chat
- **MongoDB** as the database
- **Cloudinary** for image storage
- **Lombok** for reducing boilerplate code
- **ModelMapper** for DTO conversions
- **Swagger/OpenAPI** for API documentation
- **Maven** for dependency management

## Prerequisites

- Java 17 or higher
- Maven 3.6+
- MongoDB 4.4+ (local or MongoDB Atlas)
- (Optional) Cloudinary account for image uploads

## MongoDB Setup

### Option 1: Local MongoDB Installation

1. **Install MongoDB** (for Ubuntu/Debian):
   ```bash
   sudo apt-get update
   sudo apt-get install -y mongodb
   sudo systemctl start mongodb
   sudo systemctl enable mongodb
   ```

2. **Verify MongoDB is running**:
   ```bash
   mongo --version
   sudo systemctl status mongodb
   ```

3. **Create database** (optional, will be created automatically):
   ```bash
   mongo
   > use lostioapp
   > exit
   ```

### Option 2: MongoDB Atlas (Cloud)

1. **Create free account** at [MongoDB Atlas](https://www.mongodb.com/cloud/atlas)

2. **Create a cluster**:
   - Choose free tier (M0)
   - Select your preferred cloud provider and region
   - Click "Create Cluster"

3. **Create database user**:
   - Go to "Database Access"
   - Add new database user with username and password
   - Grant "Read and write to any database" privilege

4. **Whitelist IP address**:
   - Go to "Network Access"
   - Add IP Address
   - Use "Allow access from anywhere" (0.0.0.0/0) for development

5. **Get connection string**:
   - Click "Connect" on your cluster
   - Choose "Connect your application"
   - Copy the connection string
   - Replace `<password>` with your database user password
   - Example: `mongodb+srv://username:password@cluster0.xxxxx.mongodb.net/lostioapp`

## Configuration

### 1. Configure application.properties

Edit `src/main/resources/application.properties`:

#### For Local MongoDB:
```properties
spring.data.mongodb.uri=mongodb://localhost:27017/lostioapp
spring.data.mongodb.database=lostioapp
```

#### For MongoDB Atlas:
```properties
spring.data.mongodb.uri=mongodb+srv://username:password@cluster0.xxxxx.mongodb.net/lostioapp
spring.data.mongodb.database=lostioapp
```

### 2. JWT Secret Configuration

⚠️ **Important**: Change the JWT secret in production!

```properties
jwt.secret=your-very-secure-secret-key-at-least-256-bits-long
jwt.expiration=86400000
```

### 3. Cloudinary Configuration (Optional)

If you want to enable image uploads:

1. Create account at [Cloudinary](https://cloudinary.com/)
2. Get your credentials from dashboard
3. Set environment variables or update `application.properties`:

```properties
cloudinary.cloud-name=your-cloud-name
cloudinary.api-key=your-api-key
cloudinary.api-secret=your-api-secret
```

Or set as environment variables:
```bash
export CLOUDINARY_CLOUD_NAME=your-cloud-name
export CLOUDINARY_API_KEY=your-api-key
export CLOUDINARY_API_SECRET=your-api-secret
```

If Cloudinary is not configured, the API will return placeholder URLs for image uploads.

## Installation & Running

### 1. Clone and navigate to backend directory:
```bash
cd backend
```

### 2. Install dependencies:
```bash
mvn clean install
```

### 3. Run the application:
```bash
mvn spring-boot:run
```

Or run with development profile:
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

### 4. Build for production:
```bash
mvn clean package
java -jar target/lostio-backend-1.0.0.jar
```

The application will start on `http://localhost:8080`

## API Documentation

Once the application is running, access the Swagger UI documentation at:

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/v3/api-docs

## API Endpoints

### Authentication
- `POST /api/auth/register` - Register new user
- `POST /api/auth/login` - Login and get JWT token
- `POST /api/auth/forgot-password` - Request password reset
- `POST /api/auth/reset-password` - Reset password with token
- `GET /api/auth/me` - Get current user profile

### Users
- `GET /api/users/{id}` - Get user by ID
- `PUT /api/users/{id}` - Update user profile
- `GET /api/users` - Get all users
- `POST /api/users/{id}/avatar` - Upload user avatar

### Reports
- `POST /api/reports` - Create new report
- `GET /api/reports` - Get all reports (with filters)
- `GET /api/reports/{id}` - Get report by ID
- `PUT /api/reports/{id}` - Update report
- `DELETE /api/reports/{id}` - Delete report
- `GET /api/reports/search?keyword=` - Search reports
- `POST /api/reports/{id}/photo` - Upload report photo

### Claims
- `POST /api/claims` - Create new claim
- `GET /api/claims` - Get all claims
- `GET /api/claims/{id}` - Get claim by ID
- `GET /api/claims/report/{reportId}` - Get claims for report
- `PUT /api/claims/{id}/status` - Update claim status
- `DELETE /api/claims/{id}` - Delete claim

### Chat
- `POST /api/chat` - Send message
- `GET /api/chat?senderId=&receiverId=` - Get messages between users
- `GET /api/chat/conversations/{userId}` - Get user conversations
- `PUT /api/chat/{messageId}/read` - Mark message as read
- `GET /api/chat/unread/{userId}` - Get unread message count
- `WebSocket: /ws/chat` - Real-time chat connection

### Health Check
- `GET /actuator/health` - Application health status

## Authentication

All endpoints except `/api/auth/**`, `/swagger-ui/**`, `/v3/api-docs/**`, and `/actuator/**` require JWT authentication.

### How to authenticate:

1. **Register or login** to get JWT token:
   ```bash
   curl -X POST http://localhost:8080/api/auth/login \
     -H "Content-Type: application/json" \
     -d '{"email": "user@example.com", "password": "password123"}'
   ```

2. **Include token in subsequent requests**:
   ```bash
   curl -X GET http://localhost:8080/api/reports \
     -H "Authorization: Bearer YOUR_JWT_TOKEN"
   ```

## Testing with Swagger

1. Open Swagger UI: http://localhost:8080/swagger-ui.html
2. Click "Authorize" button
3. Enter: `Bearer YOUR_JWT_TOKEN`
4. Click "Authorize"
5. Now you can test all endpoints

## Database Indexes

The application automatically creates the following indexes:

- **Users**: Unique index on `email`
- **Reports**: Indexes on `userId`, `status`, `location`
- **Claims**: Indexes on `reportId`, `claimerId`, `status`
- **Messages**: Compound indexes on `senderId + receiverId + timestamp`

## CORS Configuration

Default allowed origins (can be changed in `application.properties`):
```properties
cors.allowed-origins=http://localhost:19000,http://localhost:19001,http://localhost:19002,http://localhost:8081
```

For development, you can allow all origins:
```properties
cors.allowed-origins=*
```

## Environment Variables

You can override configuration using environment variables:

```bash
export SPRING_DATA_MONGODB_URI=mongodb://localhost:27017/lostioapp
export JWT_SECRET=your-secret-key
export JWT_EXPIRATION=86400000
export CLOUDINARY_CLOUD_NAME=your-cloud-name
export CLOUDINARY_API_KEY=your-api-key
export CLOUDINARY_API_SECRET=your-api-secret
```

## Troubleshooting

### MongoDB Connection Issues

1. **Check if MongoDB is running**:
   ```bash
   sudo systemctl status mongodb
   ```

2. **Check connection string** in `application.properties`

3. **For MongoDB Atlas**:
   - Verify IP whitelist
   - Check username/password in connection string
   - Ensure database user has proper permissions

### Port Already in Use

Change the port in `application.properties`:
```properties
server.port=8081
```

### Cloudinary Upload Fails

- Verify credentials are correct
- Check internet connectivity
- Review logs for specific error messages
- If not configured, API returns placeholder URLs

## Development

### Run with hot reload:
```bash
mvn spring-boot:run -Dspring-boot.run.jvmArguments="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005"
```

### Run tests:
```bash
mvn test
```

### Check code coverage:
```bash
mvn clean test jacoco:report
```

## Project Structure

```
backend/
├── src/
│   ├── main/
│   │   ├── java/com/lostio/
│   │   │   ├── LostioApplication.java
│   │   │   ├── config/          # Configuration classes
│   │   │   ├── controller/      # REST controllers
│   │   │   ├── dto/             # Data Transfer Objects
│   │   │   ├── document/        # MongoDB documents
│   │   │   ├── repository/      # MongoDB repositories
│   │   │   ├── service/         # Business logic
│   │   │   ├── exception/       # Custom exceptions
│   │   │   └── util/            # Utility classes
│   │   └── resources/
│   │       └── application.properties
│   └── test/
├── pom.xml
└── README.md
```

## Production Deployment

### 1. Update configuration for production:
   - Change JWT secret to a strong random string
   - Use production MongoDB instance
   - Configure proper CORS origins
   - Enable HTTPS

### 2. Build production JAR:
```bash
mvn clean package -DskipTests
```

### 3. Run with production profile:
```bash
java -jar target/lostio-backend-1.0.0.jar --spring.profiles.active=prod
```

### 4. Use environment variables for sensitive data:
```bash
export JWT_SECRET=<strong-random-secret>
export SPRING_DATA_MONGODB_URI=<production-mongodb-uri>
export CLOUDINARY_API_SECRET=<cloudinary-secret>
```

## License

MIT License

## Support

For issues and questions, please open an issue on GitHub or contact support@lostioapp.com
