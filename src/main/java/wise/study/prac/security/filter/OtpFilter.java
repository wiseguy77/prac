package wise.study.prac.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import wise.study.prac.biz.dto.MfaRequest;
import wise.study.prac.security.token.OtpAuthToken;
import wise.study.prac.security.wrapper.RequestWrapper;

@RequiredArgsConstructor
public class OtpFilter extends OncePerRequestFilter {

  private final ObjectMapper objectMapper = new ObjectMapper();

  private final AuthenticationManager authenticationManager;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {

    RequestWrapper wrapper = new RequestWrapper(request);

    MfaRequest mfa = objectMapper.readValue(wrapper.getBody(), MfaRequest.class);
    String account = mfa.getAccount();
    String otpCode = mfa.getOtpCode();

    var authentication = authenticationManager.authenticate(
        new OtpAuthToken(account, otpCode));

    SecurityContextHolder.getContext().setAuthentication(authentication);

    filterChain.doFilter(wrapper, response);
  }

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
    return !(request.getServletPath().equals("/api/auth/otp"));
  }
}
