# Java Coding Challenge

## Objective

Implement a RESTful API for a fictional online bookstore using a database of your choice. The API should provide functionality for user signup, login, book management, and search.

## Requirements

### 1. User Management
- **Signup:** Implement an endpoint for user registration. Users should provide their name, email, and password. Ensure that the email is unique.
- **Login:** Implement an endpoint for user login. Use JWT (JSON Web Tokens) for authentication. The login should return a token that must be used to access protected endpoints.

### 2. Book Management
- **GET API Operations:** Implement endpoints to get all books. Each book should have a title, author, description, publication date, and genre.
- **Rate Limiting:** Implement rate limiting to restrict the number of requests a user can make to the book management endpoints. For example, allow only 60 requests per minute.

### 3. Request Throttling
- Implement request throttling to handle high traffic. Ensure that the system can gracefully handle an influx of requests without crashing. Provide a meaningful error message when the rate limit is exceeded.

### 4. Unit and Integration Tests
- Write unit tests for the user signup, login, and book management functionalities.

## Bonus Marks if following features are implemented (Not mandatory)

### 1. Book Reviews and Ratings
- Implement all CRUD APIs for book management.
- Implement endpoints for users to add reviews and ratings to books. Each review should have a rating (1-5), a comment, and a timestamp.
- Ensure that users can only review a book once, and they can update their review if needed.

### 2. Data Validation and Error Handling
- Implement comprehensive data validation and error handling for all endpoints. Ensure that invalid data is properly handled and meaningful error messages are returned.

### 3. Documentation
- Provide API documentation using Swagger or a similar tool. The documentation should include details about each endpoint, the request and response formats, and any authentication requirements.

## Instructions

1. Use any modern Java framework and database of your choice.
2. Ensure that your code is modular and follows best practices for code organization.
3. Provide a README file with instructions on how to set up and run the project.
4. Ensure that your solution is secure and does not expose any sensitive information.
5. Submit your code as a GitHub repository link.

## Evaluation Criteria

- Code quality and organization.
- Correctness and completeness of the implementation.
- Security and robustness of the application.
- Quality and coverage of tests.
- Quality of API documentation.
- Handling of edge cases and error conditions.
