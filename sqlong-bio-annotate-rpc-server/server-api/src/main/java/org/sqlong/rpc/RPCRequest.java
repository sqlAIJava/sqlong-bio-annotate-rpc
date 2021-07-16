package org.sqlong.rpc;

import lombok.*;

import java.io.Serializable;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RPCRequest implements Serializable {

  // rpc调用 所需的 指向 / 传输协议
  private String className;
  private String methodName;
  private Object[] parameters;

}
