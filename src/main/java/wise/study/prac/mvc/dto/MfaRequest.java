package wise.study.prac.mvc.dto;

import lombok.Data;

@Data
public class MfaRequest {

  private String account;
  private String password;
  private String otpCode;
}
