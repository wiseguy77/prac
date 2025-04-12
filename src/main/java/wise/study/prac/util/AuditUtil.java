package wise.study.prac.util;

import jakarta.servlet.http.HttpServletRequest;

public class AuditUtil {

  public static String getClientIp(HttpServletRequest request) {
    String clientIp = request.getHeader("X-Forwarded-For");
    if (clientIp != null && !clientIp.isEmpty() && !"unknown".equalsIgnoreCase(clientIp)) {
      return clientIp.split(",")[0].trim();
    }

    clientIp = request.getHeader("Proxy-Client-IP");
    if (clientIp != null && !"unknown".equalsIgnoreCase(clientIp)) {
      return clientIp;
    }

    clientIp = request.getHeader("WL-Proxy-Client-IP");
    if (clientIp != null && !"unknown".equalsIgnoreCase(clientIp)) {
      return clientIp;
    }

    return request.getRemoteAddr();
  }
}
