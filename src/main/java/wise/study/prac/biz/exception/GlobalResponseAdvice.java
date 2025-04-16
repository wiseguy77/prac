package wise.study.prac.biz.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import wise.study.prac.biz.dto.CommonResponse;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalResponseAdvice implements ResponseBodyAdvice<Object> {

  private final ObjectMapper objectMapper;

  @Override
  public boolean supports(MethodParameter returnType,
      Class<? extends HttpMessageConverter<?>> converterType) {

    String path = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
        .getRequest().getRequestURI();

    return !path.startsWith("/v3/api-docs") &&
        !path.startsWith("/swagger") &&
        !path.startsWith("/swagger-ui");
  }

  @Override
  public Object beforeBodyWrite(Object body, MethodParameter returnType,
      MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType,
      ServerHttpRequest request, ServerHttpResponse response) {

    if (body instanceof CommonResponse) {
      return body;
    }

    if (body instanceof String) {
      try {
        return objectMapper.writeValueAsString(CommonResponse.success(body));
      } catch (JsonProcessingException e) {
        throw new RuntimeException("응답 직렬화 실패", e);
      }
    }

    return CommonResponse.success(body);
  }
}
