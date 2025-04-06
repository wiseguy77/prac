package wise.study.prac.security.wrapper;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import lombok.Getter;
import org.apache.coyote.BadRequestException;

@Getter
public class RequestWrapper extends HttpServletRequestWrapper {

  //Use this method to read the request body N times
  private final String body;

  public RequestWrapper(HttpServletRequest request) throws BadRequestException {
    super(request);

    String contentType = request.getContentType();

    // form 요청이면 먼저 getParameterMap 호출 (파라미터 캐시를 생성)
    if (contentType != null && contentType.startsWith("application/x-www-form-urlencoded")) {
      request.getParameterMap();
      body = "";
    } else {
      StringBuilder stringBuilder = new StringBuilder();
      try (BufferedReader bufferedReader = request.getReader()) {
        char[] charBuffer = new char[128];
        int bytesRead;
        while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
          stringBuilder.append(charBuffer, 0, bytesRead);
        }
        body = stringBuilder.toString();
      } catch (IOException e) {
        throw new BadRequestException();
      }
    }
  }

  @Override
  public ServletInputStream getInputStream() {
    final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(body.getBytes());
    return new ServletInputStream() {
      @Override
      public boolean isFinished() {
        return false;
      }

      @Override
      public boolean isReady() {
        return false;
      }

      @Override
      public void setReadListener(ReadListener readListener) {
        throw new UnsupportedOperationException();
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
}