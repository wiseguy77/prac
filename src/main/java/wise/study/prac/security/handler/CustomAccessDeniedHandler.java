package wise.study.prac.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import wise.study.prac.mvc.dto.CommonResponse;
import wise.study.prac.mvc.exception.ErrorCode;
import wise.study.prac.security.exception.PracAccessDeniedException;

@Component
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

  private final ObjectMapper objectMapper;

  @Override
  public void handle(HttpServletRequest request, HttpServletResponse response,
      AccessDeniedException accessDeniedException) throws IOException {

//    response.setStatus(HttpStatus.FORBIDDEN.value());
//    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
//    response.setCharacterEncoding(StandardCharsets.UTF_8.name());
//
//    CommonResponse<?> error = CommonResponse.fail(ErrorCode.ACCESS_DENIED);
//    response.getWriter().write(objectMapper.writeValueAsString(error));

    // 기본 값
    ErrorCode errorCode = ErrorCode.ACCESS_DENIED;
    int status = HttpStatus.FORBIDDEN.value();

    // PracAuthenticationException 일 경우 내부 errorCode 사용
    if (accessDeniedException instanceof PracAccessDeniedException pracEx) {
      errorCode = pracEx.getErrorCode();
      status = pracEx.getHttpStatus().value();
    }

    response.setStatus(status);
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setCharacterEncoding(StandardCharsets.UTF_8.name());

    CommonResponse<?> error = CommonResponse.fail(errorCode);
    response.getWriter().write(objectMapper.writeValueAsString(error));
  }
}

