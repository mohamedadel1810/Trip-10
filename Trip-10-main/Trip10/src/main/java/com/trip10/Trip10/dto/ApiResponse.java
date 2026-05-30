package com.trip10.Trip10.dto;

public class ApiResponse <T>{
   private int status;
    private String message;
    private T data;

private ApiResponse(int status, String message, T data) {
    this.status = status;
    this.message = message;
    this.data = data;
}
public static <T> ApiResponse<T> success(String message,T data) {return new ApiResponse<>(200,message,data);}


    public static <T> ApiResponse<T> created(String message, T data) {
        return new ApiResponse<>(201, message, data);
    }

    public static <T> ApiResponse<T> notFound(String message) {
        return new ApiResponse<>(404, message, null);
    }

    public static <T> ApiResponse<T> badRequest(String message) {
        return new ApiResponse<>(400, message, null);
    }

    public static <T> ApiResponse<T> badRequest(String message, T data) {
        return new ApiResponse<>(400, message, data);
    }

    public static <T> ApiResponse<T> conflict(String message) {
        return new ApiResponse<>(409, message, null);

}
    public static <T> ApiResponse<T> forbidden(String message) {
        return new ApiResponse<>(403, message, null);
    }

    public int getStatus() { return status; }
    public String getMessage() { return message; }
    public T getData() { return data; }
}