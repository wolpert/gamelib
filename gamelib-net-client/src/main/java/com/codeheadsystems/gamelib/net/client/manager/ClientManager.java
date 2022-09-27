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

package com.codeheadsystems.gamelib.net.client.manager;

import com.codeheadsystems.gamelib.net.client.factory.ClientConnectionFactory;
import com.codeheadsystems.gamelib.net.client.model.ClientConnection;
import com.codeheadsystems.gamelib.net.manager.JsonManager;
import com.codeheadsystems.gamelib.net.model.Authenticated;
import com.codeheadsystems.gamelib.net.model.Identity;
import com.codeheadsystems.gamelib.net.model.ServerDetails;
import io.netty.channel.ChannelFuture;
import io.netty.util.concurrent.Future;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class ClientManager {

  private static final Logger LOGGER = LoggerFactory.getLogger(ClientManager.class);

  private final ClientConnectionFactory clientConnectionFactory;
  private final JsonManager jsonManager;
  private final BlockingQueue<String> queue;

  private ClientConnection client;
  private Status status = Status.OFFLINE;
  private Identity identity;
  private CompletableFuture<ServerDetails> serverDetailsFuture;
  private CompletableFuture<Authenticated> authenticatedFuture;

  @Inject
  public ClientManager(final ClientConnectionFactory clientConnectionFactory,
                       final JsonManager jsonManager,
                       final BlockingQueue<String> queue) {
    this.clientConnectionFactory = clientConnectionFactory;
    this.jsonManager = jsonManager;
    this.queue = queue;
    LOGGER.info("ClientManager({},{}", clientConnectionFactory, queue);
    setStatus(Status.OFFLINE);
    serverDetailsFuture = new CompletableFuture<>();
    authenticatedFuture = new CompletableFuture<>();
  }

  public java.util.concurrent.Future<ServerDetails> getServerDetailsFuture() {
    return serverDetailsFuture;
  }

  public java.util.concurrent.Future<Authenticated> getAuthenticatedFuture() {
    return authenticatedFuture;
  }

  public Status getStatus() {
    return status;
  }

  private void setStatus(Status status) {
    LOGGER.info("setStatus: {}->{}", this.status, status);
    this.status = status;
  }

  public ChannelFuture sendMessage(String msg) {
    if (status.equals(Status.OFFLINE)) {
      return null;
    }
    return client.channel().writeAndFlush(msg + "\r\n");
  }

  public boolean connect(final Identity identity) {
    LOGGER.info("connect({})", identity.id());
    if (status.equals(Status.OFFLINE)) {
      setStatus(Status.CONNECTING);
      this.identity = identity;
      client = clientConnectionFactory.instance();
      closeFuture().addListener(this::closed);
      return true;
    } else {
      return false;
    }
  }

  /**
   * Closed the connection.
   *
   * @param f the future that closed.
   */
  private void closed(Future<? super Void> f) {
    setStatus(Status.OFFLINE);
    if (!serverDetailsFuture.isDone()) {
      serverDetailsFuture.cancel(true);
    }
    if (!authenticatedFuture.isDone()) {
      authenticatedFuture.cancel(true);
    }
    serverDetailsFuture = new CompletableFuture<>();
    authenticatedFuture = new CompletableFuture<>();
  }

  public void serverDetails(final ServerDetails serverDetails) {
    LOGGER.info("serverDetails({})", serverDetails);
    setStatus(Status.UNAUTH);
    sendMessage(jsonManager.toJson(identity));
    serverDetailsFuture.complete(serverDetails);
  }

  public void authenticated(final Authenticated authenticated) {
    LOGGER.info("authenticated({})", authenticated);
    setStatus(Status.CONNECTED);
    authenticatedFuture.complete(authenticated);
  }

  public Optional<Future<?>> disconnect() {
    LOGGER.info("disconnect()");
    if (status != Status.OFFLINE && status != Status.STOPPING) {
      setStatus(Status.STOPPING);
      return Optional.of(client.eventLoopGroup().shutdownGracefully());
    } else {
      return Optional.empty();
    }
  }

  public ChannelFuture closeFuture() {
    return client.channel().closeFuture();
  }

  public enum Status {OFFLINE, CONNECTING, UNAUTH, CONNECTED, STOPPING}
}
