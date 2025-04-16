package wise.study.prac.biz.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class IssuedJwtResponse {

  String accessToken;
  String refreshToken;
}
