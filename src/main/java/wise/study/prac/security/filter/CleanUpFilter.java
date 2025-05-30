package wise.study.prac.security.filter;

import static wise.study.prac.biz.enums.MdcKeys.REQUEST_IP;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import wise.study.prac.common.util.AuditUtil;

@Slf4j
public class CleanUpFilter extends OncePerRequestFilter {

  @Override
  protected void doFilterInternal(HttpServletRequest request,
      HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    try {
      String clientIp = AuditUtil.getClientIp(request);
      MDC.put(REQUEST_IP.getMdcKey(), clientIp);
      // (선택) 요청 전 처리
      filterChain.doFilter(request, response);
    } finally {
      SecurityContextHolder.clearContext();
      MDC.clear();
      log.info("Clean up complete.");
    }
  }
}
