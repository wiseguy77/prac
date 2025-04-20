package wise.study.prac.biz.repository.params;

import wise.study.prac.biz.dto.FieldFilter;

public interface MemberRepoFilterParam {

  FieldFilter<String> getAccount();

  FieldFilter<String> getName();

  FieldFilter<String> getEmail();
}
