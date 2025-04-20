package wise.study.prac.biz.dto;

import static wise.study.prac.biz.dto.GroupFilter.LogicType.AND;

import wise.study.prac.biz.dto.GroupFilter.LogicType;

public interface SimpleAndFilterDto extends FilterDto {

  default LogicType getLogicType() {
    return AND;
  }

  ;

  GroupFilterDto toGroupFilterDto();
}
