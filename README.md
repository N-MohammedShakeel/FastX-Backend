# FastX - Bus Ticket Booking System

A comprehensive REST API backend for a bus ticket booking platform that handles passengers, bus operators, and admin functionalities.

## Features

### User Management
- **Passenger Registration & Login** - Register and authenticate passengers
- **Operator Registration & Login** - Register and authenticate bus operators
- **Google OAuth2 Integration** - Sign up/login using Google account
- **Profile Management** - Update user profiles
- **Password Management** - Change password and forgot password functionality
- **Wallet System** - Add money to wallet for booking tickets

### Bus Operations
- **Bus Management** - Add, update, delete buses
- **Route Management** - Add, update, delete routes with departure times
- **Seat Availability** - Check available seats for a specific bus
- **Bus Categories** - Support for AC, Sleeper, and various amenities (water bottle, blanket, TV, charging point)

### Booking System
- **Ticket Booking** - Book tickets with multiple seats
- **Booking History** - View all, active, and past bookings
- **Booking Confirmation** - Email notifications for successful bookings
- **Booking Cancellation** - Cancel bookings by operators

### Refund System
- **Refund Requests** - Passengers can request refunds
- **Refund Processing** - Operators can approve or reject refund requests
- **Wallet Updates** - Automatic wallet updates on refund approval

### Admin Features
- **User Management** - View and delete passengers and operators
- **Route Management** - View and delete routes
- **Booking Oversight** - View all bookings and delete if needed

## Tech Stack

- **Java 21**
- **Spring Boot 4.0.5**
- **Spring Data JPA** - Database operations
- **Spring Security** - Authentication and authorization
- **JWT (jjwt 0.12.6)** - Token-based authentication
- **Spring OAuth2 Client** - Google OAuth2 integration
- **MySQL** - Database
- **Lombok** - Reduce boilerplate code
- **SpringDoc OpenAPI 3.0.1** - API documentation
- **Spring Mail** - Email notifications
- **Thymeleaf** - Email templates
- **OpenHTMLtoPDF** - PDF generation

## Project Structure

```
src/main/java/com/example/FastX/
├── config/
│   └── AsyncConfig.java
├── constants/
│   ├── AuthProvider.java
│   ├── BookingStatus.java
│   ├── RefundStatus.java
│   └── Role.java
├── controller/
│   ├── AdminController.java
│   ├── AuthController.java
│   ├── BusOperatorController.java
│   └── PassengerController.java
├── dto/
│   ├── ApiResponseDTO.java
│   ├── BookingRequestDTO.java
│   ├── BookingsResponseDTO.java
│   ├── BusRequestDTO.java
│   ├── BusResponseDTO.java
│   ├── ErrorResponseDTO.java
│   ├── ForgotPasswordDTO.java
│   ├── LoginDTO.java
│   ├── OperatorDTO.java
│   ├── OperatorStatsDTO.java
│   ├── PassengerDTO.java
│   ├── PasswordDTO.java
│   ├── RefundRequestDTO.java
│   ├── RefundResponseDTO.java
│   ├── RouteRequestDTO.java
│   ├── RouteResponseDTO.java
│   ├── RouteSearchResponseDTO.java
│   ├── UserRegisterDTO.java
│   └── UserUpdateDTO.java
├── entity/
│   ├── BookedSeat.java
│   ├── Booking.java
│   ├── Bus.java
│   ├── Refund.java
│   ├── Route.java
│   └── User.java
├── exception/
│   ├── BadRequestException.java
│   ├── GlobalExceptionHandler.java
│   ├── ResourceNotFoundException.java
│   └── UnauthorizedException.java
├── repository/
│   ├── BookedSeatRepository.java
│   ├── BookingRepository.java
│   ├── BusRepository.java
│   ├── RefundRepository.java
│   ├── RouteRepository.java
│   └── UserRepository.java
├── security/
│   ├── config/
│   │   └── SecurityConfig.java
│   ├── jwt/
│   │   └── JwtUtil.java
│   ├── model/
│   │   └── UserPrincipal.java
│   └── service/
│       └── MyUserDetailsService.java
├── service/
│   ├── AdminService.java
│   ├── AuthService.java
│   ├── EmailService.java
│   ├── OperatorService.java
│   ├── PassengerService.java
│   └── Impl/
│       ├── AdminServiceImpl.java
│       ├── AuthServiceImpl.java
│       ├── OperatorServiceImpl.java
│       └── PassengerServiceImpl.java
├── util/
│   └── Mapper.java
└── FastXApplication.java
```

## Configuration

### Application Properties

Create or update `src/main/resources/application.properties` with the following configuration:

```properties
# Application Name
spring.application.name=FastX

# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/hexaware
# For Docker: spring.datasource.url=jdbc:mysql://host.docker.internal:3306/hexaware
spring.datasource.username=root
spring.datasource.password=your_mysql_password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect

# Security Configuration
spring.security.user.name=admin
spring.security.user.password=admin_password

# JWT Configuration
jwt.secret=your_jwt_secret_key_minimum_256_bits
jwt.expiration=3600000000

# Google OAuth2 Configuration
spring.security.oauth2.client.registration.google.client-id=your_google_client_id
spring.security.oauth2.client.registration.google.client-secret=your_google_client_secret
spring.security.oauth2.client.registration.google.scope=openid,profile,email

# Email Configuration (Gmail)
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your_email@gmail.com
spring.mail.password=your_app_specific_password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```

### Required Environment Variables

- **Database URL**: MySQL database connection string
- **Database Username**: MySQL username
- **Database Password**: MySQL password
- **JWT Secret**: Secret key for JWT token generation (minimum 256 bits)
- **Google Client ID**: OAuth2 client ID from Google Cloud Console
- **Google Client Secret**: OAuth2 client secret from Google Cloud Console
- **Email Username**: Gmail address for sending notifications
- **Email Password**: App-specific password for Gmail

## Docker Setup

### Dockerfile

```dockerfile
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

### Build and Run with Docker

1. **Build the JAR file:**
```bash
mvn clean package
```

2. **Build the Docker image:**
```bash
docker build -t fastx-backend .
```

3. **Run the container:**
```bash
docker run -p 8080:8080 -e SPRING_DATASOURCE_URL=jdbc:mysql://host.docker.internal:3306/hexaware --name fastx-backend fastx-backend
```

### Docker Environment Variables

You can override application properties using environment variables:
- `SPRING_DATASOURCE_URL` - Database connection URL
- `SPRING_DATASOURCE_USERNAME` - Database username
- `SPRING_DATASOURCE_PASSWORD` - Database password
- `JWT_SECRET` - JWT secret key
- `SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_GOOGLE_CLIENT_ID` - Google OAuth2 client ID
- `SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_GOOGLE_CLIENT_SECRET` - Google OAuth2 client secret

## API Endpoints

### Authentication
- `POST /api/auth/register/passenger` - Register a new passenger
- `POST /api/auth/register/operator` - Register a new operator
- `POST /api/auth/login` - User login
- `GET /api/auth/oauth2/start?role=PASSENGER|OPERATOR` - Start Google OAuth2
- `POST /api/auth/forgot-password` - Request password reset

### Passenger APIs
- `GET /api/passenger/profile` - Get passenger profile
- `PUT /api/passenger/profile` - Update passenger profile
- `GET /api/passenger/routes` - Get all routes
- `GET /api/passenger/routes/search` - Search routes by origin, destination, date
- `GET /api/passenger/bus/{busId}/seats` - Get available seats for a bus
- `POST /api/passenger/bookings` - Book a ticket
- `GET /api/passenger/bookings` - Get all bookings
- `GET /api/passenger/bookings/{id}` - Get booking by ID
- `GET /api/passenger/bookings/active` - Get active bookings
- `GET /api/passenger/bookings/past` - Get past bookings
- `POST /api/passenger/refunds/{bookingId}` - Request refund
- `PUT /api/passenger/wallet/add` - Add money to wallet
- `PUT /api/passenger/password` - Update password

### Operator APIs
- `GET /api/operator/profile` - Get operator profile
- `PUT /api/operator/profile` - Update operator profile
- `GET /api/operator/bus` - Get operator's buses
- `POST /api/operator/bus` - Add a new bus
- `PUT /api/operator/bus/{id}` - Update bus details
- `DELETE /api/operator/bus/{id}` - Delete a bus
- `GET /api/operator/routes` - Get operator's routes
- `POST /api/operator/routes` - Add a new route
- `PUT /api/operator/routes/{id}` - Update route details
- `DELETE /api/operator/routes/{id}` - Delete a route
- `GET /api/operator/bookings` - Get operator's bookings
- `GET /api/operator/bookings/{id}` - Get booking by ID
- `PUT /api/operator/bookings/{id}/cancel` - Cancel a booking
- `GET /api/operator/refunds` - Get refund requests
- `PUT /api/operator/refunds/{id}` - Process refund request
- `PUT /api/operator/password` - Update password
- `GET /api/operator/stats` - Get operator statistics

### Admin APIs
- `GET /api/admin/passengers` - Get all passengers
- `DELETE /api/admin/passengers/{id}` - Delete a passenger
- `GET /api/admin/operators` - Get all operators
- `DELETE /api/admin/operators/{id}` - Delete an operator
- `GET /api/admin/routes` - Get all routes
- `DELETE /api/admin/routes/{id}` - Delete a route
- `GET /api/admin/bookings` - Get all bookings
- `GET /api/admin/bookings/{id}` - Get booking by ID
- `DELETE /api/admin/bookings/{id}` - Delete a booking

## API Documentation

Once the application is running, access the Swagger UI documentation at:
```
http://localhost:8080/swagger-ui/index.html
```

## Database Schema

### Tables
- **users** - User information (passengers, operators, admins)
- **buses** - Bus details and amenities
- **routes** - Route information with departure times
- **bookings** - Booking records
- **booked_seats** - Seat bookings per booking
- **refunds** - Refund requests and status

### Enums
- **Role**: ADMIN, PASSENGER, OPERATOR
- **BookingStatus**: BOOKED, CONFIRMED, PROCESSING, CANCELLED, COMPLETED
- **RefundStatus**: PENDING, APPROVED, REJECTED
- **AuthProvider**: LOCAL, GOOGLE

## Security

- **JWT Authentication** - Token-based authentication for secured endpoints
- **Role-Based Access Control** - Different access levels for passengers, operators, and admins
- **Password Encryption** - BCrypt encryption for password storage
- **OAuth2 Integration** - Google OAuth2 for social login

## Email Features

- **Booking Confirmation** - Automatic email on successful booking
- **Password Reset** - Temporary password sent via email
- Uses Thymeleaf templates for HTML email generation

## Running the Application

### Prerequisites
- Java 21
- Maven 3.6+
- MySQL 8.0+
- Google OAuth2 credentials (for OAuth2 features)
- Gmail account with app-specific password (for email features)

### Local Development

1. **Clone the repository**
```bash
git clone <repository-url>
cd FastX-Backend
```

2. **Configure application.properties**
Update the configuration file with your database and OAuth2 credentials.

3. **Create MySQL database**
```sql
CREATE DATABASE hexaware;
```

4. **Run the application**
```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## 📝 License

This project is licensed under the Apache 2.0 License.

