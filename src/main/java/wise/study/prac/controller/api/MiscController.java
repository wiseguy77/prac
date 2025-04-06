package wise.study.prac.controller.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/misc/")
public class MiscController {

  @GetMapping("status")
  public String status() {
    return "OK";
  }
}
