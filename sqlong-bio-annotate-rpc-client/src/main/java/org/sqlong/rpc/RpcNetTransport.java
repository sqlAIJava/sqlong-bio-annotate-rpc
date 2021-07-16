package org.sqlong.rpc;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class RpcNetTransport {

  private final String host;
  private final int port;

  public RpcNetTransport(String host, int port) {
    this.host = host;
    this.port = port;
  }

  // 发起网络请求
  public Object sendRequest(RPCRequest request) {
    Object result = null;

    try (
        Socket socket = new Socket(host, port);
        ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream())
    ) {

      outputStream.writeObject(request);
      outputStream.flush();

      result = inputStream.readObject();
    } catch (IOException | ClassNotFoundException e) {
      e.printStackTrace();
    }

    return result;
  }

}
