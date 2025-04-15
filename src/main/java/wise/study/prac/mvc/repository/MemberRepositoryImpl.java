package wise.study.prac.mvc.repository;

import static wise.study.prac.mvc.repository.predicate.MemberPredicate.accountEq;
import static wise.study.prac.mvc.repository.predicate.MemberPredicate.emailEq;
import static wise.study.prac.mvc.repository.predicate.MemberPredicate.nameEq;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import wise.study.prac.mvc.entity.Member;
import wise.study.prac.mvc.entity.QMember;
import wise.study.prac.mvc.entity.QTeam;
import wise.study.prac.mvc.repository.params.MemberRepoParam;

@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom {

  private final JPAQueryFactory jpaQuery;
  private final QMember qMember = QMember.member;

  @Override
  public List<Member> findMemberOne(MemberRepoParam param) {
    return List.of();
  }

  @Override
  public Optional<Member> findMemberTeam(MemberRepoParam repoParam) {

    QTeam team = new QTeam("team");
    BooleanBuilder where = new BooleanBuilder();

    where.and(accountEq(repoParam.getAccount()))
        .and(nameEq(repoParam.getName()))
        .and(emailEq(repoParam.getEmail()));

    Member member = jpaQuery.selectFrom(qMember)
        .where(where)
        .leftJoin(qMember.team, team)
        .fetchJoin()
        .fetchOne();

    return Optional.ofNullable(member);
  }
}
