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

package com.codeheadsystems.gamelib.net.server;

import com.codeheadsystems.gamelib.net.manager.JsonManager;
import com.codeheadsystems.gamelib.net.model.Disconnect;
import com.codeheadsystems.gamelib.net.model.ImmutableAuthenticated;
import com.codeheadsystems.gamelib.net.model.ImmutableDisconnect;
import com.codeheadsystems.gamelib.net.model.ServerDetails;
import com.codeheadsystems.gamelib.net.server.factory.AuthenticationManagerFactory;
import com.codeheadsystems.gamelib.net.server.manager.AuthenticationManager;
import com.codeheadsystems.gamelib.net.server.manager.ServerDetailsManager;
import dagger.assisted.AssistedInject;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.ssl.SslHandler;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The type Net client handler.
 */
public class NetClientHandler extends SimpleChannelInboundHandler<String> {

  private static final Logger LOGGER = LoggerFactory.getLogger(NetClientHandler.class);
  private final ChannelGroup channels;
  private final JsonManager jsonManager;
  private final AuthenticationManager authenticationManager;
  private final ServerDetailsManager serverDetailsManager;
  private final GameListener gameListener;

  private Channel channel;
  private Status status;
  private Optional<MessageHandler> messageHandler;

  /**
   * Instantiates a new Net client handler.
   *
   * @param channels                     the channels
   * @param jsonManager                  the json manager
   * @param authenticationManagerFactory the authentication manager factory
   * @param serverDetailsManager         the server details manager
   * @param gameListener                 the game listener
   */
  @AssistedInject
  public NetClientHandler(final ChannelGroup channels,
                          final JsonManager jsonManager,
                          final AuthenticationManagerFactory authenticationManagerFactory,
                          final ServerDetailsManager serverDetailsManager,
                          final GameListener gameListener) {
    LOGGER.info("GameClientChannelHandler({},{})", channels, jsonManager);
    this.channels = channels;
    this.jsonManager = jsonManager;
    this.serverDetailsManager = serverDetailsManager;
    this.gameListener = gameListener;
    this.authenticationManager = authenticationManagerFactory.instance(this);
    this.setStatus(Status.OFFLINE);
  }

  /**
   * Gets status.
   *
   * @return the status
   */
  public Status getStatus() {
    return status;
  }

  private void setStatus(final Status status) {
    LOGGER.info("State change for {}, {}->{}", (channel != null ? channel.id() : "null"), this.status, status);
    this.status = status;
    if (status.equals(Status.AVAILABLE)) {
      messageHandler = Optional.of(gameListener.availableMessageHandler());
    } else if (status.equals(Status.AUTHENTICATED)) {
      messageHandler = Optional.of(gameListener.authenticatedMessageHandler());
    } else {
      messageHandler = Optional.empty();
    }
  }

  @Override
  public void channelActive(final ChannelHandlerContext ctx) {
    LOGGER.info("channelActive({})", ctx);
    channel = ctx.channel();
    this.setStatus(Status.UNAUTH);
    // Once session is secured, send a greeting and register the channel to the global channel
    // list so the channel received the messages from others.
    ctx.pipeline().get(SslHandler.class).handshakeFuture().addListener(
        (GenericFutureListener<Future<Channel>>) future -> {
          LOGGER.info("New Channel {}", channel.remoteAddress());
          final ServerDetails serverDetails = serverDetailsManager.serverDetails(ctx);
          writeMessage(jsonManager.toJson(serverDetails));
          this.setStatus(Status.AUTH_REQUEST);
          authenticationManager.initialized();
        });
    ctx.channel().closeFuture().addListener(future -> setStatus(Status.OFFLINE));
  }

  /**
   * Write message channel future.
   *
   * @param message the message
   * @return the channel future
   */
  public ChannelFuture writeMessage(final String message) {
    return channel.writeAndFlush(message + "\r\n");
  }

  /**
   * Authenticated.
   */
  public void authenticated() {
    LOGGER.info("authenticated({},{})", channel.id(), status);
    if (status.equals(Status.AUTH_REQUEST)) {
      this.setStatus(Status.AUTHENTICATED);
      writeMessage(jsonManager.toJson(ImmutableAuthenticated.builder().build()));
      channels.add(channel);
    } else {
      LOGGER.warn("Request to set status to authenticated when we are {}", status);
    }
  }

  /**
   * Shutdown.
   *
   * @param reason the reason
   */
  public void shutdown(final String reason) {
    LOGGER.info("shutdown({})", reason);
    this.setStatus(Status.STOPPING);
    if (channel == null) {
      return;
    }
    final Disconnect disconnect = ImmutableDisconnect.builder().reason(reason).build();
    final String message = jsonManager.toJson(disconnect);
    writeMessage(message).addListener(future -> {
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
      messageHandler.ifPresentOrElse(
          mh -> mh.handleMessage(msg, this),
          () -> LOGGER.warn("Unable to handle message: {}:{}", status, msg));
    } else {
      LOGGER.warn("Message out of order: {}", msg);
    }
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
    LOGGER.error(cause.getMessage() + ":" + ctx.channel().remoteAddress(), cause);
    ctx.close();
    this.setStatus(Status.OFFLINE);
  }

  /**
   * The enum Status.
   */
  public enum Status {
    /**
     * Offline status.
     */
    OFFLINE,
    /**
     * Unauth status.
     */
    UNAUTH,
    /**
     * Auth request status.
     */
    AUTH_REQUEST,
    /**
     * Authenticated status.
     */
    AUTHENTICATED(true),
    /**
     * Available status.
     */
    AVAILABLE(true),
    /**
     * Stopping status.
     */
    STOPPING;

    private final boolean communicable;

    Status() {
      this(false);
    }

    Status(final boolean communicable) {
      this.communicable = communicable;
    }

  }
}
