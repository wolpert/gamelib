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

package com.codeheadsystems.gamelib.net.server.module;

import com.codeheadsystems.gamelib.net.module.NetCommonModule;
import com.codeheadsystems.gamelib.net.server.ChannelPipelineInitializer;
import com.codeheadsystems.gamelib.net.server.model.ImmutableNetServerConfiguration;
import com.codeheadsystems.gamelib.net.server.model.NetServerConfiguration;
import dagger.Module;
import dagger.Provides;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.GlobalEventExecutor;
import java.security.cert.CertificateException;
import java.util.List;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.net.ssl.SSLException;

@Module(includes = NetCommonModule.class)
public class NetServerModule {

  public static final String WORKER_GROUP = "workerGroup";
  public static final String BOSS_GROUP = "bossGroup";
  private final NetServerConfiguration netServerConfiguration;

  public NetServerModule() {
    this(ImmutableNetServerConfiguration.builder().build());
  }

  public NetServerModule(final ImmutableNetServerConfiguration netServerConfiguration) {
    this.netServerConfiguration = netServerConfiguration;
  }

  @Provides
  @Singleton
  public NetServerConfiguration netServerConfiguration() {
    return netServerConfiguration;
  }

  @Provides
  @Singleton
  public ChannelGroup channelGroup(final EventExecutor eventExecutor) {
    return new DefaultChannelGroup(eventExecutor);
  }

  @Provides
  @Singleton
  public EventExecutor eventExecutor() {
    // TODO: Get a real event executor
    return GlobalEventExecutor.INSTANCE;
  }

  @Provides
  @Singleton
  public SelfSignedCertificate selfSignedCertificate() {
    try {
      return new SelfSignedCertificate();
    } catch (CertificateException e) {
      throw new RuntimeException(e);
    }
  }

  @Provides
  @Singleton
  public SslContext sslContext(final SelfSignedCertificate ssc) {
    try {
      return SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey())
          .protocols("TLSv1.3")
          .ciphers(List.of("TLS_AES_256_GCM_SHA384"))
          .build();
    } catch (SSLException e) {
      throw new RuntimeException(e);
    }
  }

  @Provides
  @Singleton
  @Named(BOSS_GROUP)
  public EventLoopGroup bossGroup() {
    return new NioEventLoopGroup(1);
  }

  @Provides
  @Singleton
  @Named(WORKER_GROUP)
  public EventLoopGroup workerGroup() {
    return new NioEventLoopGroup();
  }

  @Provides
  @Singleton
  public ServerBootstrap serverBootstrap(@Named(BOSS_GROUP) final EventLoopGroup bossGroup,
                                         @Named(WORKER_GROUP) final EventLoopGroup workerGroup,
                                         final ChannelPipelineInitializer initializer) {
    ServerBootstrap b = new ServerBootstrap();
    b.group(bossGroup, workerGroup)
        .channel(NioServerSocketChannel.class)
        .handler(new LoggingHandler(LogLevel.DEBUG))
        .childHandler(initializer);
    return b;
  }

}