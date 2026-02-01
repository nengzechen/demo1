package exceptions;

/**
 * 资源已存在异常
 */
public class ResourceAlreadyExistsException extends RuntimeException {

    public ResourceAlreadyExistsException(String message) {
        super(message);
    }

    public ResourceAlreadyExistsException(String resource, String field, Object value) {
        super(String.format("%s已存在: %s = %s", resource, field, value));
    }
}
