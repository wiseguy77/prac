package wise.study.prac.security.filter;

import static wise.study.prac.biz.exception.ErrorCode.JWT_EXPIRED;
import static wise.study.prac.biz.exception.ErrorCode.JWT_INVALID;
import static wise.study.prac.biz.exception.ErrorCode.UNAUTHORIZED;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.InvalidClaimException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import wise.study.prac.biz.exception.PracException;
import wise.study.prac.security.exception.PracAuthenticationException;
import wise.study.prac.security.token.JwtAuthToken;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

  private final AuthenticationManager authenticationManager;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {

    String access = request.getHeader("Authorization");
    String refresh = request.getHeader("Refresh-Token");

    try {
      var authentication = authenticationManager.authenticate(new JwtAuthToken(access, refresh));

      SecurityContextHolder.getContext().setAuthentication(authentication);

      filterChain.doFilter(request, response);
    } catch (ExpiredJwtException e) {
      throw new PracAuthenticationException(JWT_EXPIRED);
    } catch (InvalidClaimException e) {
      throw new PracAuthenticationException(JWT_INVALID);
    } catch (PracException e) {
      throw new PracAuthenticationException(e);
    } catch (Exception e) {
      throw new PracAuthenticationException(UNAUTHORIZED, e);
    }
  }

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
    return request.getServletPath().equals("/api/auth/login") ||
        request.getServletPath().equals("/api/auth/otp");
  }
}