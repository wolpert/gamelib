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
import com.codeheadsystems.gamelib.net.model.Identity;
import io.netty.channel.ChannelFuture;
import io.netty.util.concurrent.Future;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class ClientManager {

  private static final Logger LOGGER = LoggerFactory.getLogger(ClientManager.class);

  private final ClientConnectionFactory clientConnectionFactory;
  private final BlockingQueue<String> queue;

  private ClientConnection client;
  private Status status = Status.OFFLINE;

  @Inject
  public ClientManager(final ClientConnectionFactory clientConnectionFactory,
                       final BlockingQueue<String> queue) {
    this.clientConnectionFactory = clientConnectionFactory;
    this.queue = queue;
    LOGGER.info("ClientManager({},{}", clientConnectionFactory, queue);
    setStatus(Status.OFFLINE);
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

  public boolean connect() {
    LOGGER.info("connect()");
    if (status.equals(Status.OFFLINE)) {
      setStatus(Status.CONNECTING);
      client = clientConnectionFactory.instance();
      setStatus(Status.UNAUTH);
      closeFuture().addListener((f)->setStatus(Status.OFFLINE));
      return true;
    } else {
      return false;
    }
  }

  public boolean authenticated(final Identity identity) {
    return false; // TODO: implement
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
