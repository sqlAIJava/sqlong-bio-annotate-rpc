package org.sqlong.rpc;

@RpcService(ISayHelloService.class)
public class SayHelloServiceImpl implements ISayHelloService {

  @Override
  public String say(String request) {
    System.out.println("server receive request : " + request);
    return "server return ok by say";
  }

  @Override
  public String saveUser(UserDTO user) {
    System.out.println("server receive user : " + user);
    return "server return ok by save user";
  }

}
