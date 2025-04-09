package wise.study.prac.mvc.enums;

public enum MemberStatus {

  ACTIVE("정상"),      // 로그인 가능, 일반 사용자
  INACTIVE("비활성화"), // 추가 인증 미완료, 로그인 불가
  DORMANT("휴면"),     // 장기 미사용으로 자동 전환
  SUSPENDED("정지"),   // 관리자가 유예시킨 상태
  BLOCKED("영구정지"),  // 영구정지
  WITHDRAWN("탈퇴"),   // 탈퇴한 사용자 (로그인 불가, 데이터 보관용)
  DELETED("삭제됨");   // 완전히 삭제된 계정 (물리삭제 개념)

  final String description;

  MemberStatus(String description) {
    this.description = description;
  }
}
