package com.trip10.Trip10.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

public class ApiResponse <T>{

    @Getter
    private boolean status;
    @Getter
    private String message;
    @Getter
    private T data;

    @JsonIgnore
    private final int httpStatusCode;

private ApiResponse(boolean status, String message, T data, int httpStatusCode) {
    this.status = status;
    this.message = message;
    this.data = data;
    this.httpStatusCode = httpStatusCode;
}
public static <T> ApiResponse<T> success(String message,T data) {return new ApiResponse<>(true,message,data,200);}


    public static <T> ApiResponse<T> created(String message, T data) {
        return new ApiResponse<>(true, message, data, 201);
    }

    public static <T> ApiResponse<T> notFound(String message) {
        return new ApiResponse<>(false, message, null, 404);
    }

    public static <T> ApiResponse<T> badRequest(String message) {
        return new ApiResponse<>(false, message, null, 400);
    }

    public static <T> ApiResponse<T> unauthorized(String message) {
        return new ApiResponse<>(false, message, null, 401);
    }

    public static <T> ApiResponse<T> badRequest(String message, T data) {
        return new ApiResponse<>(false, message, data, 400);
    }

    public static <T> ApiResponse<T> conflict(String message) {
        return new ApiResponse<>(false, message, null, 409);

}
    public static <T> ApiResponse<T> forbidden(String message) {
        return new ApiResponse<>(false, message, null, 403);
    }

    public ResponseEntity<ApiResponse<T>> toResponseEntity() {
        return ResponseEntity.status(httpStatusCode).body(this);
    }
}
