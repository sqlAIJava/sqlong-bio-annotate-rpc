package org.sqlong.rpc;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.Map;

public class ProcessorHandler implements Runnable {

  private final Socket socket;
  private final Map<String, Object> handlerMap;

  public ProcessorHandler(Socket socket, Map<String, Object> handlerMap) {
    this.socket = socket;
    this.handlerMap = handlerMap;
  }

  @Override
  public void run() {
    // 得到连接，进行序列化处理 + 放射调用，返回结果
    try (
        ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream())
    ) {

      RPCRequest rpcRequest = (RPCRequest) inputStream.readObject();
      Object ret = invoke(rpcRequest);

      outputStream.writeObject(ret);
      outputStream.flush();

    }catch (IOException | ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException exception) {
      exception.printStackTrace();
    }
  }

  // 独立方法处理请求调用
  private Object invoke(RPCRequest rpcRequest) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Object service = handlerMap.get(rpcRequest.getClassName());

    // 确定参数
    Object[] args = rpcRequest.getParameters();
    Class<?>[] types = new Class[args.length];
    for (int i = 0; i <  args.length; i++ )
      types[i] = args[i].getClass();

    // 定位类、方法
    Class<?> clazz = Class.forName(rpcRequest.getClassName());
    Method method = clazz.getMethod(rpcRequest.getMethodName(), types);
    method.setAccessible(true);

    return method.invoke(service, args);
  }

}
