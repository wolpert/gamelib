/*
 *   Copyright (c) 2023. Ned Wolpert <ned.wolpert@gmail.com>
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

import com.codeheadsystems.gamelib.net.model.ImmutableServerDetails;
import com.codeheadsystems.gamelib.net.model.ServerDetails;
import com.codeheadsystems.gamelib.net.server.model.NetServerConfiguration;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.ssl.SslHandler;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * The type Server details manager.
 */
@Singleton
public class ServerDetailsManager {

  private final NetServerConfiguration configuration;

  /**
   * Instantiates a new Server details manager.
   *
   * @param configuration the configuration
   */
  @Inject
  public ServerDetailsManager(final NetServerConfiguration configuration) {
    this.configuration = configuration;
  }

  /**
   * Server details server details.
   *
   * @param ctx the ctx
   * @return the server details
   */
  public ServerDetails serverDetails(final ChannelHandlerContext ctx) {
    return ImmutableServerDetails.builder()
        .buildNumber(configuration.buildNumber())
        .version(configuration.version())
        .crypto(ctx.pipeline().get(SslHandler.class).engine().getSession().getCipherSuite())
        .name(configuration.name())
        .build();
  }

}
