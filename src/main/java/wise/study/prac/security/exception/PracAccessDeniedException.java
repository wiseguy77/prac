package wise.study.prac.security.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import wise.study.prac.biz.exception.ErrorCode;
import wise.study.prac.biz.exception.PracException;

@Getter
public class PracAccessDeniedException extends AccessDeniedException {

  private final ErrorCode errorCode;

  public PracAccessDeniedException(ErrorCode errorCode) {
    super(errorCode.getMessage());
    this.errorCode = errorCode;
  }

  public PracAccessDeniedException(ErrorCode errorCode, Throwable cause) {
    super(errorCode.getMessage(), cause);
    this.errorCode = errorCode;
  }

  public PracAccessDeniedException(PracException pracEx) {
    this(pracEx.getErrorCode());
  }

  public HttpStatus getHttpStatus() {
    return errorCode.getHttpStatus();
  }
}
