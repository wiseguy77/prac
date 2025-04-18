package wise.study.prac.biz.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class PracException extends RuntimeException {

  private final ErrorCode errorCode;

  public PracException(ErrorCode errorCode) {
    super(errorCode.getMessage());
    this.errorCode = errorCode;
  }

  public PracException(ErrorCode errorCode, String detailMessage) {
    super(errorCode.getMessage() + ":" + detailMessage);
    this.errorCode = errorCode;
  }

  public PracException(ErrorCode errorCode, Throwable e) {
    super(errorCode.getMessage(), e);
    this.errorCode = errorCode;
  }

  public HttpStatus getHttpStatus() {
    return errorCode.getHttpStatus();
  }
}