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

import dagger.assisted.AssistedInject;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import java.util.concurrent.BlockingQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientHandler extends SimpleChannelInboundHandler<String> {

  private static final Logger LOGGER = LoggerFactory.getLogger(ClientHandler.class);

  private final BlockingQueue<String> queue;

  @AssistedInject
  public ClientHandler(final BlockingQueue<String> queue) {
    LOGGER.info("ClientHandler({})", queue);
    this.queue = queue;
  }

  @Override
  public void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
    if (queue.offer(msg)) {
      LOGGER.debug("Message: {}", msg);
    } else {
      LOGGER.error("Lost Message: {}", msg);
    }
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
    LOGGER.error(cause.getMessage(), cause);
    ctx.close();
  }

  @Override
  public void channelRegistered(final ChannelHandlerContext ctx) throws Exception {
    LOGGER.info("channelRegistered:{}", ctx);
    super.channelRegistered(ctx);
  }

  @Override
  public void channelUnregistered(final ChannelHandlerContext ctx) throws Exception {
    LOGGER.info("channelUnregistered: {}", ctx);
  }

  @Override
  public void channelActive(final ChannelHandlerContext ctx) throws Exception {
    LOGGER.info("channelActive: {}", ctx);
    super.channelActive(ctx);
  }

  @Override
  public void channelInactive(final ChannelHandlerContext ctx) throws Exception {
    LOGGER.info("channelInactive: {}", ctx);
    super.channelInactive(ctx);
  }

  @Override
  public void channelReadComplete(final ChannelHandlerContext ctx) throws Exception {
    LOGGER.info("channelReadComplete: {}", ctx);
    super.channelReadComplete(ctx);
  }

  @Override
  public void userEventTriggered(final ChannelHandlerContext ctx, final Object evt) throws Exception {
    LOGGER.info("userEventTriggered: {},{}", ctx, evt);
    super.userEventTriggered(ctx, evt);
  }

  @Override
  public void channelWritabilityChanged(final ChannelHandlerContext ctx) throws Exception {
    LOGGER.info("channelWritabilityChanged: {}", ctx);
    super.channelWritabilityChanged(ctx);
  }
}
