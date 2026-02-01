package models;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * 统一 API 响应格式
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    
    private int code;
    private String message;
    private T data;
    private Long timestamp;
    
    // 状态码常量
    public static final int SUCCESS = 200;
    public static final int CREATED = 201;
    public static final int BAD_REQUEST = 400;
    public static final int NOT_FOUND = 404;
    public static final int SERVER_ERROR = 500;
    
    public ApiResponse() {
        this.timestamp = System.currentTimeMillis();
    }
    
    public ApiResponse(int code, String message) {
        this();
        this.code = code;
        this.message = message;
    }
    
    public ApiResponse(int code, String message, T data) {
        this(code, message);
        this.data = data;
    }
    
    // ================= 静态工厂方法 =================
    
    public static <T> ApiResponse<T> success() {
        return new ApiResponse<>(SUCCESS, "操作成功");
    }
    
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(SUCCESS, "操作成功", data);
    }
    
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(SUCCESS, message, data);
    }
    
    public static <T> ApiResponse<T> created(T data) {
        return new ApiResponse<>(CREATED, "创建成功", data);
    }
    
    public static <T> ApiResponse<T> created(String message, T data) {
        return new ApiResponse<>(CREATED, message, data);
    }
    
    public static <T> ApiResponse<T> error(int code, String message) {
        return new ApiResponse<>(code, message);
    }
    
    public static <T> ApiResponse<T> badRequest(String message) {
        return new ApiResponse<>(BAD_REQUEST, message);
    }
    
    public static <T> ApiResponse<T> notFound(String message) {
        return new ApiResponse<>(NOT_FOUND, message);
    }
    
    public static <T> ApiResponse<T> serverError(String message) {
        return new ApiResponse<>(SERVER_ERROR, message);
    }
    
    // ================= Getters & Setters =================
    
    public int getCode() {
        return code;
    }
    
    public void setCode(int code) {
        this.code = code;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public T getData() {
        return data;
    }
    
    public void setData(T data) {
        this.data = data;
    }
    
    public Long getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}

