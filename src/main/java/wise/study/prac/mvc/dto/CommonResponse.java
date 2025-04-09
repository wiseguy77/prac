package wise.study.prac.mvc.dto;

import java.time.LocalDateTime;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import wise.study.prac.mvc.exception.ErrorCode;

@Getter
public class CommonResponse<T> {

  private final boolean success;
  private final String code;
  private final String message;
  private final T data;
  private final Integer status;
  private final LocalDateTime timestamp;

  public CommonResponse(boolean success, String code, String message, T data, Integer status) {
    this.success = success;
    this.code = code;
    this.message = message;
    this.data = data;
    this.status = status;
    this.timestamp = LocalDateTime.now();
  }

  public static <T> CommonResponse<T> success(T data) {
    return new CommonResponse<>(true, "SUCCESS", "요청이 성공했습니다.", data, HttpStatus.OK.value());
  }

  public static CommonResponse<?> fail(ErrorCode errorCode) {
    return new CommonResponse<>(false,
        errorCode.getCode(),
        errorCode.getMessage(),
        null,
        errorCode.getHttpStatus().value());
  }
}
