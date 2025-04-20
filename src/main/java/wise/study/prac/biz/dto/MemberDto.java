package wise.study.prac.biz.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import wise.study.prac.biz.dto.FieldFilter.MatchType;
import wise.study.prac.biz.repository.criteria.field.Filter;
import wise.study.prac.biz.service.params.MemberSvcParam;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberDto implements SimpleAndFilterDto, MemberSvcParam {

  String account;
  String name;
  String email;

  @Override
  public GroupFilterDto toGroupFilterDto() {

    Filter account = FieldFilter.builder()
        .field("account")
        .value(this.account)
        .matchType(MatchType.EQUALS).build();
    Filter name = FieldFilter.builder()
        .field("name")
        .matchType(MatchType.EQUALS).build();
    Filter email = FieldFilter.builder()
        .field("email")
        .matchType(MatchType.EQUALS).build();

    Filter groupFilter = GroupFilter.builder()
        .logicType(this.getLogicType())
        .filters(List.of(account, name, email)).build();

    return new SearchGroupDto(groupFilter);
  }
}
