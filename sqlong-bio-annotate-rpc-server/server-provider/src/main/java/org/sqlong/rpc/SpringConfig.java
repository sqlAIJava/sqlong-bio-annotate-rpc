package org.sqlong.rpc;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "org.sqlong.rpc")
public class SpringConfig {

  @Bean(name = "contextProxyServer")
  public ContextProxyServer getContextProxyServer() {
    return new ContextProxyServer(9090);
  }

}