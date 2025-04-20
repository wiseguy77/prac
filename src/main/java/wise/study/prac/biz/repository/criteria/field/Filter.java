package wise.study.prac.biz.repository.criteria.field;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import wise.study.prac.biz.dto.FieldFilter;
import wise.study.prac.biz.dto.GroupFilter;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type"
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = FieldFilter.class, name = "field"),
    @JsonSubTypes.Type(value = GroupFilter.class, name = "group")
})
public interface Filter {

}
