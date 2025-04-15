package wise.study.prac.security.config;

import static wise.study.prac.security.enums.RoleType.ADMIN;

import jakarta.servlet.DispatcherType;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import wise.study.prac.security.filter.CleanUpFilter;
import wise.study.prac.security.filter.JwtFilter;
import wise.study.prac.security.filter.LogInFilter;
import wise.study.prac.security.filter.OtpFilter;
import wise.study.prac.security.handler.CustomAccessDeniedHandler;
import wise.study.prac.security.handler.CustomAuthenticationEntryPoint;
import wise.study.prac.security.provider.JwtAuthProvider;
import wise.study.prac.security.provider.LogInAuthProvider;
import wise.study.prac.security.provider.OtpAuthProvider;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity(/*debug = true*/)
public class SecurityCoreConfig {

  private final OtpAuthProvider otpAuthProvider;
  private final LogInAuthProvider logInAuthProvider;
  private final JwtAuthProvider jwtAuthProvider;
  private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
  private final CustomAccessDeniedHandler customAccessDeniedHandler;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

    AuthenticationManager authenticationManager = http.getSharedObject(
        AuthenticationManagerBuilder.class).build();

    http
        .authenticationManager(authenticationManager)
        .authenticationProvider(otpAuthProvider)
        .authenticationProvider(logInAuthProvider)
        .authenticationProvider(jwtAuthProvider)
        .csrf(AbstractHttpConfigurer::disable) // SSR 방식이면 활성화
        .cors(AbstractHttpConfigurer::disable)
        .formLogin(AbstractHttpConfigurer::disable)
        .exceptionHandling(eh -> eh
            .authenticationEntryPoint(customAuthenticationEntryPoint)
            .accessDeniedHandler(customAccessDeniedHandler))
        .addFilterBefore(new CleanUpFilter(), ChannelProcessingFilter.class)
        .addFilterBefore(new LogInFilter(authenticationManager),
            UsernamePasswordAuthenticationFilter.class)
        .addFilterAfter(new OtpFilter(authenticationManager),
            UsernamePasswordAuthenticationFilter.class)
        .addFilterBefore(new JwtFilter(authenticationManager),
            UsernamePasswordAuthenticationFilter.class)
        .authorizeHttpRequests(request -> request
            // 6.0부터 forward 에도 인증이 기본으로 변경되었기 때문에 예전처럼 동작하려면 설정 필요
            .dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll()
            .requestMatchers("/api/auth/otp"/*, "/api/member", "/api/misc/status"*/)
            .permitAll()
            .requestMatchers("/api/member/all").hasAuthority(ADMIN.getAuthority())
            .anyRequest().authenticated());

    return http.build();
  }

  @Bean
  public WebSecurityCustomizer webSecurityCustomizer() {
    return (web) -> web.ignoring()
        .requestMatchers(PathRequest.toStaticResources().atCommonLocations())
        .requestMatchers("/test", "/", "/api/misc/**")
        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html")
        .requestMatchers(HttpMethod.POST, "/api/member");
  }

  /* 만일 필터를 빈으로 정의해서 서블릿 필터에도 등록되는 것을 원하지 않을 때 사용 */
//  @Bean
//  public FilterRegistrationBean<JwtAuthenticationFilter> filterRegistrationBean(JwtAuthenticationFilter jwtAuthenticationFilter) {
//    FilterRegistrationBean<JwtAuthenticationFilter> filterRegistrationBean = new FilterRegistrationBean<>();
//    filterRegistrationBean.setFilter(jwtAuthenticationFilter);
//    filterRegistrationBean.setEnabled(false);
//    return filterRegistrationBean;
//  }
}
