package wise.study.prac.biz.repository.criteria.sort.resolver;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.DatePath;
import com.querydsl.core.types.dsl.PathBuilder;
import java.time.LocalDateTime;
import org.springframework.data.domain.Sort;

public class LocalDateTimeSortResolver implements SortResolver<LocalDateTime> {

  @Override
  public OrderSpecifier<?> resolve(PathBuilder<?> builder, String fieldName,
      Sort.Direction direction) {
    DatePath<LocalDateTime> path = builder.getDate(fieldName, LocalDateTime.class);
    return new OrderSpecifier<>(direction.isAscending() ? Order.ASC : Order.DESC, path);
  }
}
