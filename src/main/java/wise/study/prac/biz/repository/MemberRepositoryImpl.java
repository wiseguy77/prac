package wise.study.prac.biz.repository;

import static wise.study.prac.biz.repository.conditions.MemberPredicate.accountEq;
import static wise.study.prac.biz.repository.conditions.MemberPredicate.emailEq;
import static wise.study.prac.biz.repository.conditions.MemberPredicate.nameEq;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import wise.study.prac.biz.entity.Member;
import wise.study.prac.biz.entity.QMember;
import wise.study.prac.biz.entity.QTeam;
import wise.study.prac.biz.repository.conditions.GenericPredicateBuilder;
import wise.study.prac.biz.repository.params.MemberRepoFilterParam;
import wise.study.prac.biz.repository.params.MemberRepoParam;

@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom {

  private final JPAQueryFactory jpaQuery;
  private final QMember qMember = QMember.member;
  private final GenericPredicateBuilder predicateBuilder;

  @Override
  public List<Member> findMemberTeamList(MemberRepoParam repoParam) {

    QTeam team = new QTeam("team");
    BooleanBuilder where = new BooleanBuilder();

    where.and(accountEq(repoParam.getAccount()))
        .and(nameEq(repoParam.getName()))
        .and(emailEq(repoParam.getEmail()));

    return jpaQuery.selectFrom(qMember)
        .where(where)
        .leftJoin(qMember.team, team)
        .fetchJoin()
        .fetch();
  }

  @Override
  public List<Member> filterMemberList(MemberRepoFilterParam filterParam) {

    BooleanBuilder where = new BooleanBuilder();

    BooleanExpression account = predicateBuilder.build(qMember.account, filterParam.getAccount());
    BooleanExpression name = predicateBuilder.build(qMember.name, filterParam.getName());
    BooleanExpression email = predicateBuilder.build(qMember.email, filterParam.getEmail());

    where.and(account)
        .and(name)
        .and(email);

    return jpaQuery.selectFrom(qMember)
        .where(where)
        .fetch();
  }
}
