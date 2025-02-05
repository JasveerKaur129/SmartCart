# Smart Cart E-Commerce Microservices

This project implements an e-commerce platform using a microservices architecture. The system is designed to manage products, orders and notifications, with key features such as user management, product handling, and order processing.

### Features

- **API Gateway**: Centralized entry point for all requests, routing them to the appropriate microservice.
- **Discovery Server (Eureka)**: Manages service discovery, allowing services to locate each other dynamically.
- **User Service**: Handles user registration, login, authentication, and profile management.
- **Product Service**: Manages the product catalog, allowing admins to add, update, or remove products.
- **Inventory Service**: Manages the order lifecycle, handling order placement and updating product availability.
- **Notification Service**: Sends notifications (email or SMS) to users after a successful order placement.

## Microservices Breakdown

- **API Gateway**:
    - Acts as a reverse proxy, routing requests from users or admins to the corresponding microservices.
    - Provides a centralized entry point for client applications, making it easier to manage and scale.
  
- **Discovery Server (Eureka)**:
    - Enables automatic discovery of services within the system.
    - Services register themselves with Eureka, and the API Gateway uses Eureka to discover and route requests.
  
- **User Service**:
    - Provides functionality for user registration, login, and profile management.
    - Handles user authentication and authorization via JWT.
  
- **Product Service**:
    - Manages products in the catalog.
    - Admins can create, update, and delete product information.
    - Users can browse and view product details.

- **Order Service**:
    - Handles the creation of orders when a user selects products.
    - Reduces product stock upon order placement.
    - Tracks the order status and updates users accordingly.
    - 
- **Inventory Service**:
    - Manages the stock levels of products within the system.
    - Handles restocking processes, ensuring the inventory stays up-to-date and in sync with the product catalog.
    - Provides an API for other services (e.g., Order Service) to check product availability before processing an order.
      
- **Notification Service**:
    - Sends notifications to users after successful order creation.

## Tech Stack

- **Spring Boot**: Framework for building microservices.
- **Spring Cloud**: For service discovery (Eureka) and API Gateway (Netflix).
- **Docker**: For containerization of the microservices.
- **MySQL**: For database storage (user data, product info, orders, etc.).
- **Kafka** : For message queues between services for async communication.
- **JWT**: For securing and authenticating user sessions.

## Setup Instructions

### Prerequisites:

- **Java 17** or higher
- **Maven**: For building and running Spring Boot applications
- **Docker** for containerized setup
- **Postman**: for testing API endpoints.

### Running the Services:

1. Clone the repository:
    ```bash
    git clone https://github.com/JasveerKaur129/SmartCart.git"
    cd SmartCart
    ```

2. Build and run the services:

    - Navigate to each service's folder (e.g., `api-gateway`, `user-service`, etc.)
    - Run the following command to start each service:
      ```bash
      mvn spring-boot:run
      ```

    - **Eureka Discovery Server** will be running on `localhost:8761`.

3. All services are configured to run locally on different ports. Check `application.properties` to modify these if necessary.

###Docker Setup

1. Run the services using Docker:
    ```bash
    docker run -d -p 8080:8080 service-name
    ```

2. Use **Docker Compose** to orchestrate all services together.

### Testing:

- Use **Postman** to test API endpoints.

## Directory Structure

- **api-gateway**: Routes incoming requests to microservices.
- **discovery-server**: Eureka-based service discovery.
- **user-service**: Handles user-related actions like registration and login.
- **product-service**: Manages product information and catalog.
- **order-service**: Manages order lifecycle and product stock.
- **inventory-service**:Manages inventory and stock levels for products.
- **notification-service**: Sends notifications to users.

