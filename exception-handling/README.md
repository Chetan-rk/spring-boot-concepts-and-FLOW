# Spring Boot Custom Exception Handling (Clean Notes)

> **Goal of these notes:**
> By just reading this document, you should clearly understand **WHY** we create custom exceptions, **HOW** messages flow from exception → handler → response, and **WHAT** each annotation/class is doing in Spring Boot.

---

## 1️⃣ Why Exception Handling is Important in Spring Boot

Exception handling helps us:

* Prevent application crashes
* Return **clean and meaningful error responses**
* Avoid messy `try-catch` blocks everywhere
* Keep error handling **centralized and professional**

👉 In REST APIs, we never want raw stack traces or unclear errors.

---

## 2️⃣ Custom Exception – `ResourceNotFoundException`

### What we create

* A package: `exceptions`
* A class: `ResourceNotFoundException`

```java
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
```

---

## 3️⃣ Why `extends RuntimeException`?

### Meaning in simple words

> **“My `ResourceNotFoundException` IS A type of `RuntimeException`.**

### Checked vs Unchecked Exception

#### ❌ Checked Exception (`Exception`)

* Compiler **forces handling**
* Requires `throws` or `try-catch`
* Adds boilerplate

```java
public void getEmployee() throws Exception {}
```

👉 Not ideal for REST APIs

#### ✅ Unchecked Exception (`RuntimeException`)

* Compiler does **NOT** force handling
* Can be thrown anywhere
* Perfect for APIs

```java
throw new ResourceNotFoundException("Employee not found");
```

✔ Clean
✔ Simple
✔ Industry standard

👉 That’s why **99% Spring Boot custom exceptions extend `RuntimeException`**

---

## 4️⃣ Constructor – Why It Exists

```java
public ResourceNotFoundException(String message)
```

### What is a constructor?

* Called when object is created
* Used to initialize data

```java
new ResourceNotFoundException("Employee not found with id 5");
```

Without constructor ❌

* You cannot pass dynamic messages
* No flexibility

With constructor ✅

* Dynamic messages
* Clean error description

---

## 5️⃣ `super(message)` – MOST IMPORTANT PART 🔑

```java
super(message);
```

### What does `super` mean?

> Call the **parent class constructor**

Parent class = `RuntimeException`

---

### Internal Java Flow (Simplified)

```java
RuntimeException(String message) {
    super(message);
}
```

```java
Exception(String message) {
    this.message = message;
}
```

### Step-by-step Flow

```java
throw new ResourceNotFoundException("Employee not found with id 5");
```

1️⃣ Constructor receives message
2️⃣ `super(message)` calls RuntimeException
3️⃣ RuntimeException calls Exception
4️⃣ Exception stores message internally
5️⃣ `getMessage()` returns it later

💥 **That’s why `ex.getMessage()` works**

---

## 6️⃣ Why We DON’T Create Our Own `message` Variable

❌ Bad Practice

```java
private String message;
```

Why?

* Java already provides `message`
* `getMessage()` already exists
* Stack trace & cause handling already built-in

👉 Reusing Java’s exception system = **best practice**

---

## 7️⃣ Throwing Exception Using `orElseThrow()`

```java
employeeRepository.findById(id)
    .orElseThrow(() -> new ResourceNotFoundException(
        "Employee not found with id " + id));
```

### What happens here?

* `findById()` returns `Optional`
* If value exists → return object
* If NOT exists → throw exception

🔥 Controller does NOT handle it
🔥 Spring handles it globally

---

## 8️⃣ APIError Class (Error Response Structure)

### Why APIError?

Instead of random JSON, we want:

```json
{
  "status": "NOT_FOUND",
  "message": "Employee not found with id 5"
}
```

### APIError Class

```java
@Data
@Builder
public class APIError {
    private HttpStatus status;
    private String message;
}
```

---

## 9️⃣ Why `@Data` is Needed (Important)

### You thought:

> We are not calling getters/setters, so @Data is not required

### You are **conceptually right** 👍

### BUT… Spring uses getters internally

When returning:

```java
return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
```

Spring:

* Converts object → JSON
* Uses getters via reflection

❗ Without getters:

* Empty JSON
* Or serialization failure

👉 `@Data` generates:

* Getters
* Setters
* `toString()`

---

## 🔟 Why `@Builder` is Used

### Without Builder ❌

```java
APIError apiError = new APIError();
apiError.setStatus(HttpStatus.NOT_FOUND);
apiError.setMessage(ex.getMessage());
```

### With Builder ✅

```java
APIError apiError = APIError.builder()
    .status(HttpStatus.NOT_FOUND)
    .message(ex.getMessage())
    .build();
```

✔ Clean
✔ Readable
✔ Easy to extend
✔ Professional

---

## 1️⃣1️⃣ Global Exception Handler

### Annotation Used

```java
@RestControllerAdvice
```

### What it does

* Listens to all exceptions
* Applies to all controllers
* Central error handling

---

## 1️⃣2️⃣ `@ExceptionHandler`

```java
@ExceptionHandler(ResourceNotFoundException.class)
public ResponseEntity<APIError> handleResourceNotFoundException(
        ResourceNotFoundException ex) {

    APIError apiError = APIError.builder()
        .status(HttpStatus.NOT_FOUND)
        .message(ex.getMessage())
        .build();

    return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
}
```

### Human Language Meaning

> “Whenever `ResourceNotFoundException` occurs anywhere, call this method.”

---

## 1️⃣3️⃣ Complete End-to-End Flow 🧠

1️⃣ Client calls API
2️⃣ Controller → Service
3️⃣ Data not found
4️⃣ `orElseThrow()` throws exception
5️⃣ Spring finds `@RestControllerAdvice`
6️⃣ Matching `@ExceptionHandler` runs
7️⃣ APIError is built
8️⃣ ResponseEntity returned
9️⃣ Client gets clean error response

---

## 1️⃣4️⃣ One-Line Exam / Interview Summary

> We extend `RuntimeException` to create an unchecked custom exception suitable for REST APIs. The constructor accepts a message and passes it to the parent class using `super(message)`, which stores the message inside Java’s exception system. This message is later retrieved using `getMessage()` inside a global exception handler to generate a clean API error response.

---

✅ **These notes are copy-paste ready**
✅ **Readable & revision-friendly**
✅ **Interview + practical clarity guaranteed**
