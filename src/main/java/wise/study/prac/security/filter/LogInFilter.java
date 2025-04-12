package wise.study.prac.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import wise.study.prac.mvc.enums.MdcKeys;
import wise.study.prac.security.token.LogInAuthToken;
import wise.study.prac.security.wrapper.RequestWrapper;

@Slf4j
@RequiredArgsConstructor
public class LogInFilter extends OncePerRequestFilter {

  private final ObjectMapper objectMapper = new ObjectMapper();

  private final AuthenticationManager authenticationManager;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {

    RequestWrapper wrapper = new RequestWrapper(request);

    String account = wrapper.getParameter("account");;
    String password = wrapper.getParameter("password");

    var authentication = authenticationManager.authenticate(new LogInAuthToken(account, password));
    SecurityContextHolder.getContext().setAuthentication(authentication);

    MDC.put(MdcKeys.ACCOUNT.getMdcKey(), account);

    filterChain.doFilter(wrapper, response);
  }

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
    return !(request.getServletPath().equals("/api/auth/login"));

  }
}
