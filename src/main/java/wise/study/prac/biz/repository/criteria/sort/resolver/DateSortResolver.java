package wise.study.prac.biz.repository.criteria.sort.resolver;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.DatePath;
import com.querydsl.core.types.dsl.PathBuilder;
import java.util.Date;
import org.springframework.data.domain.Sort;

public class DateSortResolver implements SortResolver<Date> {

  @Override
  public OrderSpecifier<?> resolve(PathBuilder<?> builder, String fieldName,
      Sort.Direction direction) {
    DatePath<Date> path = builder.getDate(fieldName, Date.class);
    return new OrderSpecifier<>(direction.isAscending() ? Order.ASC : Order.DESC, path);
  }
}
