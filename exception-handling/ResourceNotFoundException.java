package com.codingshuttle.springbootwebtutorial.springbootwebtutorial.exceptions;

public class ResourceNotFoundException extends RuntimeException{
    public ResourceNotFoundException(String message) {
        super(message);

    }
}
//this is a custom exception that we created by extending RuntimeException class
//we are using the constructor of that class to pass the actual message to the exception by using super(message) what it does
//it send this message to the runtime exception constructor and its constructor to exception class where the message is stored and we can access by getmessag emethod
//and the above object is created at the contoaller when and exception occur
