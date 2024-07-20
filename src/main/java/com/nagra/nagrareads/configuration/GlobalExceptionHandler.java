package com.nagra.nagrareads.configuration;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleSecurityException(Exception exception) throws URISyntaxException {
        ProblemDetail errorDetail = null;

        // TODO send this stack trace to an logging tool
        exception.printStackTrace();

        if (exception instanceof BadCredentialsException) {
            errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(401), exception.getMessage());
            errorDetail.setType(new URI("https://www.nagrareads.com/platform/developers/common-errors#bad-credentials"));
            errorDetail.setProperty("description", "The username or password is incorrect");

            return errorDetail;
        }

        if (exception instanceof AccountStatusException) {
            errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(403), exception.getMessage());
            errorDetail.setType(new URI("https://www.nagrareads.com/platform/developers/common-errors#account-locked"));
            errorDetail.setProperty("description", "The account is locked");
        }

        if (exception instanceof AccessDeniedException) {
            errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(401), exception.getMessage());
            errorDetail.setType(new URI("https://www.nagrareads.com/platform/developers/common-errors#access-denied"));
            errorDetail.setProperty("description", "You are not authorized to access this resource");
        }

        if (exception instanceof SignatureException) {
            errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(403), exception.getMessage());
            errorDetail.setType(new URI("https://www.nagrareads.com/platform/developers/common-errors#invalid-jwt-signature"));
            errorDetail.setProperty("description", "The JWT signature is invalid");
        }

        if (exception instanceof ExpiredJwtException) {
            errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(403), exception.getMessage());
            errorDetail.setType(new URI("https://www.nagrareads.com/platform/developers/common-errors#jwt-expired"));
            errorDetail.setProperty("description", "The JWT token has expired");
        }

        if (exception instanceof MethodArgumentNotValidException) {
            errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(400), "Bad Request. Please check the request body");
            errorDetail.setType(new URI("https://www.nagrareads.com/platform/developers/common-errors#invalid-request"));
            errorDetail.setProperty("description", "Please check the request body");
        }

        if (exception instanceof HttpMessageNotReadableException) {
            errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(400), "Bad Request. Please check the data types like date, number etc.");
            errorDetail.setType(new URI("https://www.nagrareads.com/platform/developers/common-errors#invalid-request"));
            errorDetail.setProperty("description", "Please check the data types like date, number etc.");
        }

        if (exception instanceof NoResourceFoundException) {
            errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(404), "Resource not found. Please check the request URL.");
            errorDetail.setType(new URI("https://www.nagrareads.com/platform/developers/common-errors#resource-not-found"));
            errorDetail.setProperty("description", "Resource not found. Please check the request URL.");
        }

        if (errorDetail == null) {
            errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(500), "Internal Server Error. Please raise a ticket to the support team with details.");
            errorDetail.setType(new URI("https://www.nagrareads.com/platform/developers/common-errors#internal-server-error"));
            errorDetail.setProperty("description", "Unknown internal server error.");
        }

        return errorDetail;
    }
}