package org.sqlong.rpc;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ContextProxyServer implements ApplicationContextAware, InitializingBean {

  private final Map<String, Object> handlerMap = new HashMap<>();

  private final int port;

  private final ExecutorService executorService = Executors.newCachedThreadPool();

  public ContextProxyServer(int port) {
    this.port = port;
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    // 设置通道
    ServerSocket serverSocket;
    try {
      serverSocket = new ServerSocket(port);
      while (true) {
        Socket socket = serverSocket.accept();
        executorService.execute(new ProcessorHandler(socket, handlerMap));
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    // 扫描注解类
    Map<String, Object> rpcServiceBeanMap = applicationContext.getBeansWithAnnotation(RpcService.class);
    if (!rpcServiceBeanMap.isEmpty()) {
      for (Object serviceBean : rpcServiceBeanMap.values()) {
        RpcService rpcService = serviceBean.getClass().getAnnotation(RpcService.class);
        String serviceName = rpcService.value().getName();
        handlerMap.put(serviceName, serviceBean);
      }
    }
  }

}
