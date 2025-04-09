package wise.study.prac.mvc.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import wise.study.prac.mvc.dto.CommonResponse;

@RestControllerAdvice
public class GlobalExceptionAdvice {

  @ExceptionHandler(PracException.class)
  public ResponseEntity<CommonResponse<?>> handleCustomException(PracException ex) {
    ErrorCode errorCode = ex.getErrorCode();
    return ResponseEntity
        .status(errorCode.getHttpStatus())
        .body(CommonResponse.fail(errorCode));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<CommonResponse<?>> handleGenericException(Exception e) {
    return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(CommonResponse.fail(ErrorCode.INTERNAL_SERVER_ERROR));
  }
}
