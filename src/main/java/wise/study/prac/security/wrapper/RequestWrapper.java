package wise.study.prac.security.wrapper;

import static java.nio.charset.StandardCharsets.UTF_8;
import static wise.study.prac.biz.exception.ErrorCode.READ_REQUEST_BODY_FAIL;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;
import org.springframework.http.MediaType;
import org.springframework.util.StreamUtils;
import org.thymeleaf.util.StringUtils;
import wise.study.prac.security.exception.PracAuthenticationException;

public class RequestWrapper extends HttpServletRequestWrapper {

  private byte[] cachedBodyBytes;
  private String cachedBodyString = "";

  public RequestWrapper(HttpServletRequest request) {
    super(request);

    cacheRequestBody(request);
  }

  @Override
  public ServletInputStream getInputStream() {
    final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(cachedBodyBytes);
    return new ServletInputStream() {
      @Override
      public boolean isFinished() {
        return byteArrayInputStream.available() == 0;
      }

      @Override
      public boolean isReady() {
        return true;
      }

      @Override
      public void setReadListener(ReadListener readListener) {
        throw new PracAuthenticationException(READ_REQUEST_BODY_FAIL);
      }

      public int read() {
        return byteArrayInputStream.read();
      }
    };
  }

  @Override
  public BufferedReader getReader() {
    return new BufferedReader(new InputStreamReader(this.getInputStream()));
  }

  private void cacheRequestBody(HttpServletRequest request) {

    String contentType = request.getContentType();

    try {
      if (StringUtils.equalsIgnoreCase(contentType, MediaType.APPLICATION_FORM_URLENCODED_VALUE)) {
        request.getParameterMap();
        this.cachedBodyBytes = new byte[0];
      } else {
        cachedBodyBytes = StreamUtils.copyToByteArray(request.getInputStream());
      }
    } catch (IOException e) {
      throw new PracAuthenticationException(READ_REQUEST_BODY_FAIL, e);
    }
  }

  public String getBody() {

    if (Objects.isNull(cachedBodyBytes) || cachedBodyBytes.length == 0) {
      return "";
    }

    if (!Objects.isNull(cachedBodyString)) {
      this.cachedBodyString = new String(this.cachedBodyBytes, UTF_8);
    }

    return this.cachedBodyString;
  }
}