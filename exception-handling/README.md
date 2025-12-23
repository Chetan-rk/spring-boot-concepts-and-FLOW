2️⃣ Why do we write
extends RuntimeException

Meaning in plain English

“My ResourceNotFoundException IS A type of RuntimeException.”

This gives your class all the powers of RuntimeException.

❓ Why not extend Exception directly?

Because of checked vs unchecked exceptions.

Checked Exception (Exception)

Compiler forces you to handle it

You must write throws or try-catch

Example:

public void getEmployee() throws Exception {
}


👎 Bad for REST APIs (too much boilerplate)

Unchecked Exception (RuntimeException)

Compiler does NOT force handling

Can be thrown anywhere

Perfect for APIs

Example:

throw new ResourceNotFoundException("Not found");


✅ Clean
✅ Simple
✅ Standard Spring Boot practice

That’s why 99% Spring Boot custom exceptions extend RuntimeException.

3️⃣ Now the MOST CONFUSING PART 😄
This constructor:
public ResourceNotFoundException(String message) {
    super(message);
}


Let’s break this line by line.

4️⃣ What is a constructor?
public ResourceNotFoundException(String message)


This is a constructor, not a method.

It is called when you create the object:

new ResourceNotFoundException("Employee not found");


So Java does:

Create object

Call constructor

Pass "Employee not found" into message

5️⃣ What is super(message)?

This is the KEY 🔑

super means:

“Call the constructor of the parent class.”

Parent class here = RuntimeException

Let’s see RuntimeException’s constructor (simplified)

Inside Java:

public class RuntimeException extends Exception {
    public RuntimeException(String message) {
        super(message);
    }
}


And inside Exception:

public class Exception {
    private String message;

    public Exception(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}

6️⃣ What actually happens step-by-step

When this runs:

throw new ResourceNotFoundException("Employee not found with id 5");

Step 1
ResourceNotFoundException(String message)


message = "Employee not found with id 5"

Step 2
super(message);


Calls:

RuntimeException(String message)

Step 3

RuntimeException calls:

Exception(String message)

Step 4

Exception stores:

this.message = "Employee not found with id 5";

Step 5

Later, when you do:

ex.getMessage();


Java returns:

"Employee not found with id 5"


💥 THAT is why your GlobalExceptionHandler can access the message.
2️⃣ What exactly does @Data give us?
@Data


Generates:

getters

setters

toString()

equals()

hashCode()

So Spring can read fields safely when returning JSON.

3️⃣ Why @Builder in APIError?

Your understanding 👇

Builder is used to build the APIError object in a fast and clean manner

✅ 100% CORRECT

Instead of:

APIError apiError = new APIError();
apiError.setStatus(HttpStatus.NOT_FOUND);
apiError.setMessage(ex.getMessage());


We write:

APIError apiError = APIError.builder()
        .status(HttpStatus.NOT_FOUND)
        .message(ex.getMessage())
        .build();

Why Builder is better

Cleaner

No setters chaining

Easy to extend later

Immutable-friendly design

👉 Very professional API style

4️⃣ ex.getMessage() — where does it come from?

You said:

ex.getMessage is provided by the exception class mechanism

✅ EXACTLY RIGHT

Flow recap:

You passed message using super(message)

Java’s Exception class stores it

getMessage() is already defined in Throwable

So:

ex.getMessage()


→ returns what you passed while throwing exception
FLOW OF EXECUTION
7️⃣ COMPLETE FLOW (Very Important)
🧠 Remember this flow

Client calls API

Controller → Service

Data NOT found

orElseThrow() throws ResourceNotFoundException

Spring finds @RestControllerAdvice

Finds matching @ExceptionHandler

Builds APIError

Returns ResponseEntity

Client receives proper error response
