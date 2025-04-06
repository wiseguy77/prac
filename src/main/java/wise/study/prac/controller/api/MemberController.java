package wise.study.prac.controller.api;

import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wise.study.prac.dto.MemberInfoAllResponse;
import wise.study.prac.dto.MemberInfoRequest;
import wise.study.prac.dto.MemberInfoResponse;
import wise.study.prac.dto.RegisterMemberRequest;
import wise.study.prac.service.MemberService;

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class MemberController {

  private final MemberService memberService;

  @PostMapping()
  public ResponseEntity<Void> register(@RequestBody RegisterMemberRequest request) {

    memberService.registerMember(request);

    return ResponseEntity.ok().build();
  }

  @GetMapping()
  public ResponseEntity<MemberInfoResponse> getMember(@RequestBody MemberInfoRequest request)
      throws NotFoundException {

    MemberInfoResponse response = memberService.getMemberInfo(request);

    return ResponseEntity.ok(response);
  }

  @GetMapping("/all")
  public ResponseEntity<MemberInfoAllResponse> getAllMember() {

    MemberInfoAllResponse response = memberService.getAllMemberInfo();

    return ResponseEntity.ok(response);
  }
}
