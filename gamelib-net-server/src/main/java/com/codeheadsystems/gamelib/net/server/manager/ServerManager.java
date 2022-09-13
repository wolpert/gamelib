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

package com.codeheadsystems.gamelib.net.server.manager;

import static com.codeheadsystems.gamelib.net.server.module.NetServerModule.BOSS_GROUP;
import static com.codeheadsystems.gamelib.net.server.module.NetServerModule.WORKER_GROUP;

import com.codeheadsystems.gamelib.net.server.model.NetServerConfiguration;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class ServerManager {
  private static final Logger LOGGER = LoggerFactory.getLogger(ServerManager.class);

  private final NetServerConfiguration netServerConfiguration;
  private final ServerBootstrap serverBootstrap;
  private final EventLoopGroup bossGroup;
  private final EventLoopGroup workerGroup;

  @Inject
  public ServerManager(final NetServerConfiguration netServerConfiguration,
                       final ServerBootstrap serverBootstrap,
                       @Named(BOSS_GROUP) final EventLoopGroup bossGroup,
                       @Named(WORKER_GROUP) final EventLoopGroup workerGroup) {
    LOGGER.info("ServerManager({},{},{},{})", netServerConfiguration, serverBootstrap, bossGroup, workerGroup);
    this.netServerConfiguration = netServerConfiguration;
    this.serverBootstrap = serverBootstrap;
    this.bossGroup = bossGroup;
    this.workerGroup = workerGroup;
  }

  public void executeServer() {
    LOGGER.info("executeServer()");
    try {
      serverBootstrap.bind(netServerConfiguration.port()).sync().channel().closeFuture().sync();
    } catch (InterruptedException e) {
      LOGGER.error("Interrupted", e);
      throw new RuntimeException(e);
    } finally {
      LOGGER.info("Closing");
      bossGroup.shutdownGracefully();
      workerGroup.shutdownGracefully();
    }
  }

}