package wise.study.prac.biz.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
  /**
   * 500
   **/
  REGISTER_MEMBER_FAIL("REGISTER_MEMBER_FAIL", "사용자 가입에 실패햇습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
  KEY_GENERATION_FAIL("KEY_GENERATION_FAIL", "개인 서명 키 생성에 실패했습니다.",
      HttpStatus.INTERNAL_SERVER_ERROR),
  PASSWORD_ENCRYPTION_FAIL("PASSWORD_ENCRYPTION_FAIL", "비밀번호 암호화에 실패했습니다.",
      HttpStatus.INTERNAL_SERVER_ERROR),
  USER_NOT_FOUND("USER_NOT_FOUND", "사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
  USER_INACTIVE_STATUS("USER_INACTIVE", "사용자 계정가 비활성화 상태입니다.", HttpStatus.UNAUTHORIZED),
  JWT_EXPIRED("JWT_EXPIRED", "토큰 기간이 만료되었습니다.", HttpStatus.UNAUTHORIZED),
  JWT_INVALID("JWT_INVALID", "토큰 형식이 잘못되었습니다.", HttpStatus.UNAUTHORIZED),
  JWT_MISSING("JWT_MISSING", "토큰 정보를 찾을 수 없습니다.", HttpStatus.UNAUTHORIZED),
  UNAUTHORIZED("UNAUTHORIZED", "인증에 실패했습니다.", HttpStatus.UNAUTHORIZED),
  ACCESS_DENIED("ACCESS_DENIED", "접근 권한이 없습니다.", HttpStatus.FORBIDDEN),
  JSON_CONVERT("JSON_CONVERT_ERROR", "JSON (역)직렬화에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
  INTERNAL_SERVER_ERROR("INTERNAL_ERROR", "서버 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
  READ_REQUEST_BODY_FAIL("READ_REQUEST_BODY_FAIL", "사용자 요청 객체 정보를 얻는데 실패했습니다.",
      HttpStatus.INTERNAL_SERVER_ERROR),

  /**
   * 400
   **/
  ILLEGAL_ARGUMENTS("ILLEGAL_ARGUMENTS", "잘못된 요청 파라미터 보냈습니다.", HttpStatus.BAD_REQUEST),
  NO_RESOURCE_FOUND("NO_RESOURCE_FOUND", "요청한 경로를 찾을 수 없습니다", HttpStatus.NOT_FOUND),
  INVALID_REQUEST_BODY("INVALID_REQUEST_BODY", "요청 본문이 잘못되었습니다.", HttpStatus.BAD_REQUEST),
  BAD_REQUEST("BAD_REQUEST", "잘못된 요청입니다.", HttpStatus.BAD_REQUEST);

  private final String code;
  private final String message;
  private final HttpStatus httpStatus;

  ErrorCode(String code, String message, HttpStatus httpStatus) {
    this.code = code;
    this.message = message;
    this.httpStatus = httpStatus;
  }
}
