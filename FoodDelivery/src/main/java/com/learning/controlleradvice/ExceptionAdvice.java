package com.learning.controlleradvice;

import java.util.HashMap;
import java.util.Map;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.learning.exception.AlreadyExistsException;
import com.learning.exception.IdNotFoundException;
import com.learning.exception.apierror.ApiError;

@RestControllerAdvice
public class ExceptionAdvice extends ResponseEntityExceptionHandler {
	
	@ExceptionHandler(AlreadyExistsException.class)
	public ResponseEntity<?> alreadyExistsExceptionHandler(AlreadyExistsException e) {
		Map<String, String> hashMap = new HashMap<String, String>();
		hashMap.put("message", e.getMessage());
		return ResponseEntity.badRequest().body(hashMap);
	}
	
	@ExceptionHandler(IdNotFoundException.class)
	public ResponseEntity<?> idNotFoundExceptionHandler(IdNotFoundException e) {
		Map<String, String> hashMap = new HashMap<String, String>();
		hashMap.put("message", e.getMessage());
		return ResponseEntity.badRequest().body(hashMap);
	}
	
//	@ExceptionHandler(Exception.class)
//	public ResponseEntity<?> exceptionHandler(Exception e) {
//		Map<String, String> hashMap = new HashMap<String, String>();
//		hashMap.put("message", "Unknown Exception " + e.getMessage());
//		return ResponseEntity.badRequest().body(hashMap);
//	}
	
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		// TODO Auto-generated method stub
		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST);
		apiError.setMessage("Validation Error");
		apiError.addValidationErrors(ex.getBindingResult().getFieldErrors());
		apiError.addValidationError(ex.getBindingResult().getGlobalErrors());
		return buildResponseEntity(apiError);
	}
	
	private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
		return new ResponseEntity<>(apiError, apiError.getHttpStatus());
	}
	
	@ExceptionHandler(ConstraintViolationException.class)
	protected ResponseEntity<?> handleConstraintViolation() {
		return null;
	}

}
