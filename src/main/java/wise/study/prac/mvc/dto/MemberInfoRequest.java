package wise.study.prac.mvc.dto;

import lombok.Data;
import wise.study.prac.mvc.service.params.MemberInfoSvcParam;

@Data
public class MemberInfoRequest implements MemberInfoSvcParam {

  String account;
}
