package ezypay.subscription.exception;

import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(chain = true)
public class BusinessException extends RuntimeException {
    private final String code;
    private final String message;

    public BusinessException(Integer code,String message) {
        super(String.valueOf(code));
        this.code = String.valueOf(code);
        this.message = message;
    }

}
