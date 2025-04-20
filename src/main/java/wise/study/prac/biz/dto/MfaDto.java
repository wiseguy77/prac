package wise.study.prac.biz.dto;

import lombok.Data;

@Data
public class MfaDto {

  private String account;
  private String password;
  private String otpCode;
}
