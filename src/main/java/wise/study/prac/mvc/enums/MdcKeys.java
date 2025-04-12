package wise.study.prac.mvc.enums;

import lombok.Getter;

@Getter
public enum MdcKeys {

  ACCOUNT("account"),
  REQUEST_ID("requestId"),
  REQUEST_IP("requestIp");

  final String mdcKey;

  MdcKeys(String key) {
    this.mdcKey = key;
  }
}
