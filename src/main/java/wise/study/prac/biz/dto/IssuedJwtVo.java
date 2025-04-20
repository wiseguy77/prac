package wise.study.prac.biz.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class IssuedJwtVo {

  String accessToken;
  String refreshToken;
}
