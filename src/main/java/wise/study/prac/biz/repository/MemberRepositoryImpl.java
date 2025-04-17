package wise.study.prac.biz.repository;

import static wise.study.prac.biz.repository.conditions.MemberPredicate.accountEq;
import static wise.study.prac.biz.repository.conditions.MemberPredicate.emailEq;
import static wise.study.prac.biz.repository.conditions.MemberPredicate.nameEq;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Path;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import wise.study.prac.biz.dto.Filter;
import wise.study.prac.biz.dto.Filter.LogicType;
import wise.study.prac.biz.dto.MemberFilterRequest;
import wise.study.prac.biz.entity.Member;
import wise.study.prac.biz.entity.QMember;
import wise.study.prac.biz.entity.QTeam;
import wise.study.prac.biz.repository.conditions.GenericPredicateBuilder;
import wise.study.prac.biz.repository.params.MemberRepoParam;

@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom {

  private final JPAQueryFactory jpaQuery;
  private final QMember qMember = QMember.member;
  private final GenericPredicateBuilder predicateBuilder;

  @Override
  public List<Member> findMemberTeamList(MemberRepoParam repoParam) {

    QTeam team = QTeam.team;

    BooleanBuilder builder = new BooleanBuilder();

    builder.and(accountEq(repoParam.getAccount()))
        .and(nameEq(repoParam.getName()))
        .and(emailEq(repoParam.getEmail()));

    return jpaQuery.selectFrom(qMember)
        .where(builder)
        .leftJoin(qMember.team, team)
        .fetchJoin()
        .fetch();
  }

  @Override
  public List<Member> filterMemberList(MemberFilterRequest repoParam) {

    BooleanBuilder builder = new BooleanBuilder();

    buildFilter(builder, qMember.account, repoParam.getAccount());
    buildFilter(builder, qMember.name, repoParam.getName());
    buildFilter(builder, qMember.email, repoParam.getEmail());

    return jpaQuery.selectFrom(qMember)
        .where(builder)
        .fetch();
  }

  private void buildFilter(BooleanBuilder builder, Path<?> path, Filter<?> filter) {

    if (Objects.isNull(filter)) {
      return;
    }

    if (filter.getLogicType() == LogicType.AND) {
      builder.and(predicateBuilder.build(path, filter));
    } else {
      builder.or(predicateBuilder.build(path, filter));
    }
  }
}
