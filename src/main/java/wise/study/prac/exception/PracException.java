package wise.study.prac.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class PracException extends RuntimeException {

  private final ErrorCode errorCode;

  public PracException(ErrorCode errorCode) {
    super(errorCode.getMessage());
    this.errorCode = errorCode;
  }

  public HttpStatus getHttpStatus() {
    return errorCode.getHttpStatus();
  }
}
