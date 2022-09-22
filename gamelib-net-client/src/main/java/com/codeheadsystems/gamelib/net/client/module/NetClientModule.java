/*
 *   Copyright (c) 2022. Ned Wolpert <ned.wolpert@gmail.com>
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *
 */

package com.codeheadsystems.gamelib.net.client.module;

import com.codeheadsystems.gamelib.net.client.Initializer;
import com.codeheadsystems.gamelib.net.client.model.ImmutableNetClientConfiguration;
import com.codeheadsystems.gamelib.net.client.model.NetClientConfiguration;
import com.codeheadsystems.gamelib.net.module.NetCommonModule;
import dagger.Module;
import dagger.Provides;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import javax.inject.Singleton;
import javax.net.ssl.SSLException;

@Module(includes = NetCommonModule.class)
public class NetClientModule {

  private final NetClientConfiguration netClientConfiguration;

  public NetClientModule() {
    this(ImmutableNetClientConfiguration.builder().build());
  }

  public NetClientModule(final NetClientConfiguration netClientConfiguration) {
    this.netClientConfiguration = netClientConfiguration;
  }

  @Provides
  @Singleton
  public BlockingQueue<String> queue() {
    return new LinkedBlockingQueue<>();
  }

  @Provides
  @Singleton
  public NetClientConfiguration netClientConfiguration() {
    return netClientConfiguration;
  }

  @Provides
  @Singleton
  public SslContext sslContext() {
    try {
      return SslContextBuilder.forClient()
          .protocols("TLSv1.3")
          .ciphers(List.of("TLS_AES_256_GCM_SHA384"))
          .trustManager(InsecureTrustManagerFactory.INSTANCE).build();
    } catch (SSLException e) {
      throw new RuntimeException(e);
    }
  }

}
