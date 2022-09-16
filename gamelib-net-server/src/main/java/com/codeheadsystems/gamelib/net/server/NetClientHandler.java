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

package com.codeheadsystems.gamelib.net.server;

import com.codeheadsystems.gamelib.net.manager.JsonManager;
import com.codeheadsystems.gamelib.net.model.Disconnect;
import com.codeheadsystems.gamelib.net.model.ImmutableDisconnect;
import com.codeheadsystems.gamelib.net.model.ImmutableServerDetails;
import com.codeheadsystems.gamelib.net.model.ServerDetails;
import com.codeheadsystems.gamelib.net.server.factory.AuthenticationManagerFactory;
import com.codeheadsystems.gamelib.net.server.manager.AuthenticationManager;
import dagger.assisted.AssistedInject;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.ssl.SslHandler;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import java.net.InetAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NetClientHandler extends SimpleChannelInboundHandler<String> {

  public static final int TIMEOUT_MILLIS = 1000;
  private static final Logger LOGGER = LoggerFactory.getLogger(NetClientHandler.class);
  private final ChannelGroup channels;
  private final JsonManager jsonManager;
  private final AuthenticationManager authenticationManager;

  private Channel channel;
  private Status status;

  @AssistedInject
  public NetClientHandler(final ChannelGroup channels,
                          final JsonManager jsonManager,
                          final AuthenticationManagerFactory authenticationManagerFactory) {
    LOGGER.info("GameClientChannelHandler({},{})", channels, jsonManager);
    this.channels = channels;
    this.jsonManager = jsonManager;
    this.authenticationManager = authenticationManagerFactory.instance(this);
    status = Status.OFFLINE;
  }

  @Override
  public void channelActive(final ChannelHandlerContext ctx) {
    LOGGER.info("channelActive({})", ctx);
    status = Status.UNAUTH;
    // Once session is secured, send a greeting and register the channel to the global channel
    // list so the channel received the messages from others.
    ctx.pipeline().get(SslHandler.class).handshakeFuture().addListener(
        (GenericFutureListener<Future<Channel>>) future -> {
          channel = ctx.channel();
          LOGGER.info("New Channel {}", channel.remoteAddress());
          final ServerDetails serverDetails = ImmutableServerDetails.builder()
              .buildNumber(1)
              .version(2)
              .crypto(ctx.pipeline().get(SslHandler.class).engine().getSession().getCipherSuite())
              .name(InetAddress.getLocalHost().getHostName())
              .build();
          ctx.writeAndFlush(jsonManager.toJson(serverDetails));
          channels.add(channel);
          status = Status.AUTH_REQUEST;
          authenticationManager.initialized();
        });
  }

  public void authenticated() {
    if (status.equals(Status.AUTH_REQUEST)) {
      status = Status.AUTHENTICATED;
    } else {
      LOGGER.warn("Request to set status to authenticated when we are {}", status);
    }
  }

  public void shutdown(final String reason) {
    LOGGER.info("shutdown({})", reason);
    status = Status.STOPPING;
    if (channel == null) {
      return;
    }
    final Disconnect disconnect = ImmutableDisconnect.builder().reason(reason).build();
    final String message = jsonManager.toJson(disconnect);
    channel.writeAndFlush(message).addListener(future -> {
      status = Status.STOPPED;
      channel.close();
    });
  }

  @Override
  public void channelRead0(final ChannelHandlerContext ctx,
                           final String msg) throws Exception {
    LOGGER.debug("channelRead0({},{})", ctx.channel().remoteAddress(), msg);
    if (status.equals(Status.AUTH_REQUEST)) {
      authenticationManager.authenticate(msg);
    } else if (status.communicable) {
      // Send the received message to all channels but the current one.
      for (Channel c : channels) {
        c.writeAndFlush(msg);
      }

      // Close the connection if the client has sent 'bye'.
      if ("bye".equalsIgnoreCase(msg)) {
        shutdown("Disconnected");
      }
    } else {
      LOGGER.warn("Message out of order: {}", msg);
    }
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
    LOGGER.error(cause.getMessage() + ":" + ctx.channel().remoteAddress(), cause);
    ctx.close();
    status = Status.STOPPED;
  }

  public enum Status {
    OFFLINE, UNAUTH, AUTH_REQUEST, AUTHENTICATED(true), AVAILABLE(true), STOPPING, STOPPED;

    private boolean communicable;

    Status() {
      this(false);
    }

    Status(final boolean communicable) {
      this.communicable = communicable;
    }

  }
}
