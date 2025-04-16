package wise.study.prac.security.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import wise.study.prac.biz.exception.ErrorCode;
import wise.study.prac.biz.exception.PracException;

@Getter
public class PracAuthenticationException extends AuthenticationException {

  private final ErrorCode errorCode;

  public PracAuthenticationException(ErrorCode errorCode) {
    super(errorCode.getMessage());
    this.errorCode = errorCode;
  }

  public PracAuthenticationException(ErrorCode errorCode, Throwable cause) {
    super(errorCode.getMessage(), cause);
    this.errorCode = errorCode;
  }

  public PracAuthenticationException(PracException pracEx) {
    this(pracEx.getErrorCode());
  }


  public HttpStatus getHttpStatus() {
    return errorCode.getHttpStatus();
  }

}
