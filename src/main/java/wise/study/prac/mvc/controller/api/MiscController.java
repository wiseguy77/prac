package wise.study.prac.mvc.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wise.study.prac.mvc.dto.CommonResponse;
import wise.study.prac.mvc.dto.EmptyResponse;

@RestController
@RequestMapping("/api/misc/")
@Tag(name = "기타 API", description = "CommonResponse 래핑 객체 구조, 헬스 체크 등 기타 설정 관련 API")
public class MiscController {

  @Operation(summary = "공통 응답 래퍼 객체 스키마 조회", description = "공통 응답 래퍼 객체의 구조를 응답하고 각 api는 문서에서 body 속성에 들어갈 값만을 문서화합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "공통 응답 객체 구조 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommonResponse.class))),
  })
  @GetMapping("response-structure")
  public CommonResponse<EmptyResponse> getResponseWrapperStructure() {

    return CommonResponse.success(new EmptyResponse());
  }

  @Operation(summary = "서버 상태 조회", description = "서버가 동작한다면 정상적으로 응답합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "서버 상태 정상", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommonResponse.class))),
  })
  @GetMapping("status")
  public CommonResponse<EmptyResponse> status() {

    return CommonResponse.success(new EmptyResponse());
  }
}
