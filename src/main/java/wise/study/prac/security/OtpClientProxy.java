package wise.study.prac.security;

import org.springframework.stereotype.Component;

@Component
public class OtpClientProxy {

  public String sendOtp(String phoneNumber) {

    return "123456";
  }
}
