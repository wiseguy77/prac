package wise.study.prac.mvc.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import wise.study.prac.mvc.service.params.MemberSvcParam;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberRequest implements MemberSvcParam {

  String account;
  String name;
  String email;
}
