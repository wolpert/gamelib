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

package com.codeheadsystems.gamelib.net.client;

import com.codeheadsystems.gamelib.net.client.manager.ClientManager;
import com.codeheadsystems.gamelib.net.manager.JsonManager;
import com.codeheadsystems.gamelib.net.model.Authenticated;
import com.codeheadsystems.gamelib.net.model.Message;
import com.codeheadsystems.gamelib.net.model.ServerDetails;
import com.codeheadsystems.gamelib.net.model.TransferObject;
import dagger.assisted.AssistedInject;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import java.util.concurrent.BlockingQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientHandler extends SimpleChannelInboundHandler<String> {

  private static final Logger LOGGER = LoggerFactory.getLogger(ClientHandler.class);

  private final BlockingQueue<String> queue;
  private final ClientManager clientManager;
  private final JsonManager jsonManager;

  @AssistedInject
  public ClientHandler(final BlockingQueue<String> queue,
                       final ClientManager clientManager,
                       final JsonManager jsonManager) {
    LOGGER.info("ClientHandler({},{},{})", queue, clientManager, jsonManager);
    this.clientManager = clientManager;
    this.jsonManager = jsonManager;
    this.queue = queue;
  }

  @Override
  public void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
    final TransferObject transferObject = jsonManager.fromJson(msg, TransferObject.class);
    if (transferObject instanceof Message) {
      if (queue.offer(msg)) {
        LOGGER.debug("Message: {}", msg);
      } else {
        LOGGER.error("Lost Message: {}", msg);
      }
    } else if (transferObject instanceof ServerDetails) {
      clientManager.serverDetails((ServerDetails) transferObject);
    } else if (transferObject instanceof Authenticated) {
      clientManager.authenticated((Authenticated) transferObject);
    } else {
      LOGGER.warn("Unknown message: {},{}", msg, transferObject);
    }
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
    LOGGER.error(cause.getMessage(), cause);
    ctx.close();
  }

  @Override
  public void channelRegistered(final ChannelHandlerContext ctx) throws Exception {
    LOGGER.debug("channelRegistered:{}", ctx);
    super.channelRegistered(ctx);
  }

  @Override
  public void channelUnregistered(final ChannelHandlerContext ctx) throws Exception {
    LOGGER.debug("channelUnregistered: {}", ctx);
    super.channelUnregistered(ctx);
  }

  @Override
  public void channelActive(final ChannelHandlerContext ctx) throws Exception {
    LOGGER.debug("channelActive: {}", ctx);
    super.channelActive(ctx);
  }

  @Override
  public void channelInactive(final ChannelHandlerContext ctx) throws Exception {
    LOGGER.debug("channelInactive: {}", ctx);
    super.channelInactive(ctx);
  }

  @Override
  public void channelReadComplete(final ChannelHandlerContext ctx) throws Exception {
    LOGGER.debug("channelReadComplete: {}", ctx);
    super.channelReadComplete(ctx);
  }

  @Override
  public void userEventTriggered(final ChannelHandlerContext ctx, final Object evt) throws Exception {
    LOGGER.debug("userEventTriggered: {},{}", ctx, evt);
    super.userEventTriggered(ctx, evt);
  }

  @Override
  public void channelWritabilityChanged(final ChannelHandlerContext ctx) throws Exception {
    LOGGER.debug("channelWritabilityChanged: {}", ctx);
    super.channelWritabilityChanged(ctx);
  }
}
