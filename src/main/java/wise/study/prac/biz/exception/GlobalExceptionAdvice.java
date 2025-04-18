package wise.study.prac.biz.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import wise.study.prac.biz.dto.CommonResponse;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionAdvice {

  @ExceptionHandler(PracException.class)
  public ResponseEntity<CommonResponse<?>> handleCustomException(PracException e) {

    log.error("[Exception] : ", e);

    return ResponseEntity
        .status(e.getErrorCode().getHttpStatus())
        .body(CommonResponse.fail(e.getErrorCode()));
  }

  @ExceptionHandler({
      NoResourceFoundException.class, HttpMessageNotReadableException.class,
      MethodArgumentNotValidException.class})
  public ResponseEntity<CommonResponse<?>> handleGenericException(Exception e) {

    log.error("[Exception] : ", e);
    ErrorCode errorCode = ErrorCode.BAD_REQUEST;

    if (e instanceof NoResourceFoundException) {
      errorCode = ErrorCode.NO_RESOURCE_FOUND;
    } else if (e instanceof HttpMessageNotReadableException
        || e instanceof MethodArgumentNotValidException) {
      errorCode = ErrorCode.INVALID_REQUEST_BODY;
    }

    return ResponseEntity
        .status(errorCode.getHttpStatus())
        .body(CommonResponse.fail(errorCode));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<CommonResponse<?>> handleGenericException(Exception e,
      HttpServletRequest request) {

    if (request.getRequestURI().startsWith("/v3/api-docs") ||
        request.getRequestURI().startsWith("/swagger") ||
        request.getRequestURI().startsWith("/swagger-ui")) {
      return null; // Spring 기본 처리로 넘기기
    }

    log.error("[Exception] : ", e);

    return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(CommonResponse.fail(ErrorCode.INTERNAL_SERVER_ERROR));
  }
}
