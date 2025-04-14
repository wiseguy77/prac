package wise.study.prac.mvc.dto;

import lombok.Data;
import wise.study.prac.mvc.repository.params.MemberTeamRepoParam;
import wise.study.prac.mvc.service.params.MemberTeamSvcParam;

@Data
public class MemberTeamRequest implements MemberTeamSvcParam, MemberTeamRepoParam {

  String account;
  String name;
  String email;
}
