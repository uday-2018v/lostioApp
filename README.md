# LostioApp - Lost and Found Mobile Application

A complete Lost and Found mobile application with React Native frontend and Spring Boot backend.

## 📱 Project Structure

This repository contains:
- **React Native Frontend** - Mobile app for iOS and Android
- **Spring Boot Backend** - REST API server with MongoDB

## 🚀 Quick Start

### Frontend (React Native)
```bash
npm install
npm start
```

### Backend (Spring Boot)
```bash
cd backend
mvn spring-boot:run
```

For detailed setup instructions, see:
- Frontend: See root directory files
- Backend: [backend/README.md](backend/README.md)

## 🔧 Prerequisites

### Frontend
- Node.js 16+
- npm or yarn
- Expo CLI

### Backend
- Java 17+
- Maven 3.6+
- MongoDB (local or remote)

## 📝 Features

- User authentication with JWT
- Report lost items
- Search for found items
- Claim items
- In-app messaging
- Photo uploads

## 🏗️ Tech Stack

### Frontend
- React Native
- Expo
- React Navigation
- Axios

### Backend
- Spring Boot 3.2.5
- Spring Data MongoDB
- Spring Security
- JWT Authentication
- Lombok

## 📖 API Documentation

Backend API runs on `http://localhost:8080`

Key endpoints:
- `POST /api/auth/register` - Register new user
- `POST /api/auth/login` - Login user
- `GET /api/reports` - Get all reports
- `POST /api/reports` - Create new report
- `POST /api/claims` - Create claim
- `POST /api/chat` - Send message

See [backend/README.md](backend/README.md) for detailed API documentation.

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Open a pull request

## 📄 License

MIT License

---

**Built with ❤️ for the community**
