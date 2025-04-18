package wise.study.prac.biz.repository.criteria.sort.resolver;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.PathBuilder;
import java.math.BigDecimal;
import org.springframework.data.domain.Sort;

public class BigDecimalSortResolver implements SortResolver<BigDecimal> {

  @Override
  public OrderSpecifier<?> resolve(PathBuilder<?> builder, String fieldName,
      Sort.Direction direction) {
    NumberPath<BigDecimal> path = builder.getNumber(fieldName, BigDecimal.class);
    return new OrderSpecifier<>(direction.isAscending() ? Order.ASC : Order.DESC, path);
  }
}
