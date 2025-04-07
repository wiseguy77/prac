package wise.study.prac.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import wise.study.prac.security.enums.JwtTokenType;
import wise.study.prac.security.handler.CustomAuthenticationEntryPoint;
import wise.study.prac.security.token.JwtAuthToken;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

  private final AuthenticationManager authenticationManager;
  private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {

    HttpMethod httpMethod = HttpMethod.valueOf(request.getMethod());
    String path = request.getServletPath();
    JwtTokenType jwtTokenType = JwtTokenType.findExpectedTokenType(httpMethod, path);
    String jwt = request.getHeader("Authorization");

    try {
      var authentication = authenticationManager.authenticate(
          new JwtAuthToken(jwtTokenType, null, jwt));

      SecurityContextHolder.getContext().setAuthentication(authentication);

      filterChain.doFilter(request, response);
    } catch (Exception e) {
      customAuthenticationEntryPoint.commence(request, response,
          new InsufficientAuthenticationException(e.getMessage(), e));
    }
  }

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
    return request.getServletPath().equals("/api/auth/login") ||
        request.getServletPath().equals("/api/auth/otp");
  }
}