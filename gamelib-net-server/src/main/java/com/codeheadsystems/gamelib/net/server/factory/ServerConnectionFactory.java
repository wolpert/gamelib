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

package com.codeheadsystems.gamelib.net.server.factory;

import com.codeheadsystems.gamelib.net.server.ChannelPipelineInitializer;
import com.codeheadsystems.gamelib.net.server.model.ImmutableServerConnection;
import com.codeheadsystems.gamelib.net.server.model.NetServerConfiguration;
import com.codeheadsystems.gamelib.net.server.model.ServerConnection;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The type Server connection factory.
 */
@Singleton
public class ServerConnectionFactory {


  private static final Logger LOGGER = LoggerFactory.getLogger(ServerConnectionFactory.class);

  private final NetServerConfiguration netServerConfiguration;
  private final ChannelPipelineInitializer initializer;

  /**
   * Instantiates a new Server connection factory.
   *
   * @param netServerConfiguration the net server configuration
   * @param initializer            the initializer
   */
  @Inject
  public ServerConnectionFactory(final NetServerConfiguration netServerConfiguration,
                                 final ChannelPipelineInitializer initializer) {
    this.netServerConfiguration = netServerConfiguration;
    this.initializer = initializer;
  }

  /**
   * Instance server connection.
   *
   * @return the server connection
   */
  public ServerConnection instance() {
    final EventLoopGroup bossGroup = new NioEventLoopGroup(1); // TODO: threads from config please
    final EventLoopGroup workerGroup = new NioEventLoopGroup(); // TODO: threads from config please
    ServerBootstrap serverBootstrap = new ServerBootstrap()
        .group(bossGroup, workerGroup)
        .channel(NioServerSocketChannel.class)
        .handler(new LoggingHandler(LogLevel.DEBUG))  // inject
        .childHandler(initializer);

    final ChannelFuture startupFuture = serverBootstrap.bind(netServerConfiguration.port()); //TODO: host? Optional?
    try {
      startupFuture.sync();
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
    final Channel channel = startupFuture.channel();
    return ImmutableServerConnection.builder()
        .bossGroup(bossGroup)
        .workerGroup(workerGroup)
        .channel(channel)
        .build();
  }
}
