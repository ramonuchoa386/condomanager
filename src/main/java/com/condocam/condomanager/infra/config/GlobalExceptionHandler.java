package com.condocam.condomanager.infra.config;

import org.springframework.core.MethodParameter;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.condocam.condomanager.infra.config.classes.APIResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler implements ResponseBodyAdvice<Object> {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<APIResponse<Object>> handleValidationExceptions(MethodArgumentNotValidException exception, WebRequest request) {
        Map<String, Object> response = new HashMap<>();
        List<Map<String, String>> errors = new ArrayList<>();

        response.put("request_data:", request.getDescription(false));
        response.put("request:", request.toString());
        response.put("request_context_path:", request.getContextPath());

        for (FieldError error : exception.getBindingResult().getFieldErrors()) {
            Map<String, String> errorDetail = new HashMap<>();
            errorDetail.put("field", error.getField());
            errorDetail.put("error", error.getDefaultMessage());
            errors.add(errorDetail);
        }

        response.put("errors", errors);
        APIResponse<Object> customResponse = new APIResponse<>(HttpStatus.BAD_REQUEST, "Validation failed", response);

        return new ResponseEntity<>(customResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<APIResponse<Object>> handleHttpMessageNotReadableException(HttpMessageNotReadableException exception, WebRequest request) {
        Map<String, Object> response = new HashMap<>();
        List<Map<String, String>> errors = new ArrayList<>();

        response.put("request_data:", request.getDescription(false));
        response.put("request:", request.toString());
        response.put("request_context_path:", request.getContextPath());

        String errorMessage = "Erro de parse do JSON: " + exception.getMessage();
        Map<String, String> errorDetail = new HashMap<>();
        errorDetail.put("error", errorMessage);
        errors.add(errorDetail);

        response.put("errors", errors);
        APIResponse<Object> customResponse = new APIResponse<>(HttpStatus.BAD_REQUEST, "JSON parse error", response);

        return new ResponseEntity<>(customResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<APIResponse<Object>> handleDataIntegrityViolationException(DataIntegrityViolationException exception, WebRequest request) {
        Map<String, Object> response = new HashMap<>();
        List<Map<String, String>> errors = new ArrayList<>();

        response.put("request_data:", request.getDescription(false));
        response.put("request:", request.toString());
        response.put("request_context_path:", request.getContextPath());

        String errorMessage = "Erro de violação de integridade de dados: " + exception.getMessage();
        Map<String, String> errorDetail = new HashMap<>();
        errorDetail.put("error", errorMessage);
        errors.add(errorDetail);

        response.put("errors", errors);
        APIResponse<Object> customResponse = new APIResponse<>(HttpStatus.BAD_REQUEST, "Data integrity violation", response);

        return new ResponseEntity<>(customResponse, HttpStatus.BAD_REQUEST);
    }

    @Override
    public boolean supports(@NonNull MethodParameter returnType, @NonNull Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(@Nullable Object body, @NonNull MethodParameter returnType, @NonNull MediaType selectedContentType, @NonNull Class<? extends HttpMessageConverter<?>> selectedConverterType, @NonNull ServerHttpRequest request, @NonNull ServerHttpResponse response) {
        if (body instanceof APIResponse) {
            return body;
        }

        HttpStatus status = HttpStatus.OK;
        if (response instanceof ResponseEntity) {
            status = (HttpStatus) ((ResponseEntity<?>) response).getStatusCode();
        }

        return new APIResponse<>(status, "Success", body);
    }
}
