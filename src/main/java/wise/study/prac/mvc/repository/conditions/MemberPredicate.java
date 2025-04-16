package wise.study.prac.mvc.repository.conditions;

import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.util.StringUtils;
import wise.study.prac.mvc.entity.QMember;

public class MemberPredicate {

  private static final QMember member = QMember.member;

  public static BooleanExpression accountEq(String account) {
    return hasValue(account) ? member.account.eq(account) : null;
  }

  public static BooleanExpression nameEq(String name) {
    return hasValue(name) ? member.name.eq(name) : null;
  }

  public static BooleanExpression emailEq(String email) {
    return hasValue(email) ? member.email.eq(email) : null;
  }

  private static boolean hasValue(String value) {
    return StringUtils.hasText(value);
  }
}
