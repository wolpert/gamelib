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

import com.codeheadsystems.gamelib.net.client.factory.ChannelFactory;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import java.util.concurrent.BlockingQueue;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class ClientManager {

  private static final Logger LOGGER = LoggerFactory.getLogger(ClientManager.class);

  private final ChannelFactory channelFactory;
  private final BlockingQueue<String> queue;
  private final EventLoopGroup eventLoopGroup; // TODO: This is attached to the connection, need to refactor it.

  private Channel channel;
  private Status status = Status.OFFLINE;

  @Inject
  public ClientManager(final ChannelFactory channelFactory,
                       final BlockingQueue<String> queue,
                       final EventLoopGroup eventLoopGroup) {
    this.channelFactory = channelFactory;
    this.queue = queue;
    this.eventLoopGroup = eventLoopGroup;
    LOGGER.info("ClientManager({},{},{}", channelFactory, queue, eventLoopGroup);
    setStatus(Status.OFFLINE);
  }

  public Status getStatus() {
    return status;
  }

  private void setStatus(Status status) {
    LOGGER.info("setStatus: {}->{}", this.status, status);
    this.status = status;
  }

  public void sendMessage(String msg) {
    channel.writeAndFlush(msg + "\r\n");
  }

  public boolean connect() {
    LOGGER.info("connect()");
    if (status.equals(Status.OFFLINE)) {
      setStatus(Status.CONNECTING);
      channel = channelFactory.instance();
      setStatus(Status.UNAUTH);
      return true;
    } else {
      return false;
    }
  }

  public boolean disconnect() {
    LOGGER.info("disconnect()");
    if (status != Status.OFFLINE && status != Status.STOPPING) {
      setStatus(Status.STOPPING);
      eventLoopGroup.shutdownGracefully();
      setStatus(Status.OFFLINE);
      return true;
    } else {
      return false;
    }
  }

  enum Status {OFFLINE, CONNECTING, UNAUTH, CONNECTED, STOPPING}
}
