# Exception Handling in Spring Boot

## What I learned
- Creating custom RuntimeException
- Using @RestControllerAdvice
- Using @ExceptionHandler
- Returning custom API error response

## Why exception handling is needed
- Avoid try-catch blocks in controllers
- Centralized error handling
- Clean REST API responses

## Flow
Client Request  
→ Controller  
→ Service  
→ Exception thrown  
→ GlobalExceptionHandler  
→ APIError  
→ HTTP Response  

## Key annotations
- @RestControllerAdvice
- @ExceptionHandler
- @Builder
- @Data

## Notes to future self
- Use RuntimeException for business logic
- Do not use checked exceptions for REST APIs
- Spring uses reflection to detect handlers

