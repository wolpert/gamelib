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

package com.codeheadsystems.gamelib.net.client.factory;

import com.codeheadsystems.gamelib.net.client.Initializer;
import com.codeheadsystems.gamelib.net.client.model.ClientConnection;
import com.codeheadsystems.gamelib.net.client.model.ImmutableClientConnection;
import com.codeheadsystems.gamelib.net.client.model.NetClientConfiguration;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ClientConnectionFactory {

  private final NetClientConfiguration netClientConfiguration;
  private final Initializer initializer;

  @Inject
  public ClientConnectionFactory(final NetClientConfiguration netClientConfiguration,
                                 final Initializer initializer) {
    this.netClientConfiguration = netClientConfiguration;
    this.initializer = initializer;
  }

  public ClientConnection instance() {
    final EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
    final Bootstrap bootstrap = new Bootstrap()
        .group(eventLoopGroup)
        .channel(NioSocketChannel.class)
        .handler(initializer);
    try {
      final Channel channel = bootstrap
          .connect(netClientConfiguration.host(), netClientConfiguration.port())
          .sync().channel();
      return ImmutableClientConnection.builder().channel(channel).eventLoopGroup(eventLoopGroup).build();
    } catch (InterruptedException e) {
      throw new IllegalStateException(e);
    }
  }

}
