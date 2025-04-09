package wise.study.prac.security.otp;

import org.springframework.stereotype.Component;

@Component
public class OtpClientProxy {

  public String sendOtp(String phoneNumber) {

    return "123456";
  }
}
