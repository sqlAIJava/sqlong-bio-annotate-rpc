package org.sqlong.rpc;

import lombok.*;

import java.io.Serializable;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDTO implements Serializable {

  private String userName;

}
