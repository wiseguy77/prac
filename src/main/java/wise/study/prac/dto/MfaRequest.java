package wise.study.prac.dto;

import lombok.Data;

@Data
public class MfaRequest {

  private String account;
  private String password;
  private String otpCode;
}
