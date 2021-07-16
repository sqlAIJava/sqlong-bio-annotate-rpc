package org.sqlong.rpc;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class RemoteInvocationHandler implements InvocationHandler {

  private final String host;
  private final int port;

  public RemoteInvocationHandler(String host, int port) {
    this.host = host;
    this.port = port;
  }

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    // 请求会被代理到这里，为请求做准备
    RPCRequest rpcRequest = RPCRequest.builder()
        .className(method.getDeclaringClass().getName())
        .methodName(method.getName())
        .parameters(args)
        .build();

    RpcNetTransport rpcNetTransport = new RpcNetTransport(host, port);
    return rpcNetTransport.sendRequest(rpcRequest);
  }

}
