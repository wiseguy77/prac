package wise.study.prac.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import wise.study.prac.biz.dto.CommonResponse;
import wise.study.prac.biz.exception.ErrorCode;
import wise.study.prac.security.exception.PracAuthenticationException;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

  private final ObjectMapper objectMapper;

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException authException) throws IOException {

    // 기본 값
    ErrorCode errorCode = ErrorCode.UNAUTHORIZED;
    int status = HttpStatus.UNAUTHORIZED.value();

    // PracAuthenticationException 일 경우 내부 errorCode 사용
    if (authException instanceof PracAuthenticationException pracEx) {
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

