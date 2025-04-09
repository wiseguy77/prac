package wise.study.prac.mvc.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class IssuedOtpResponse {

  String account;
  String otpCode;
}
