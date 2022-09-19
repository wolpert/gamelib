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

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class Handler extends SimpleChannelInboundHandler<String> {

  @Override
  public void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
    System.err.println(msg);
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
    cause.printStackTrace();
    ctx.close();
  }

  @Override
  public void channelRegistered(final ChannelHandlerContext ctx) throws Exception {
    System.err.println("channelRegistered");
    super.channelRegistered(ctx);
  }

  @Override
  public void channelUnregistered(final ChannelHandlerContext ctx) throws Exception {
    System.err.println("channelUnregistered");
  }

  @Override
  public void channelActive(final ChannelHandlerContext ctx) throws Exception {
    System.err.println("channelActive");
    super.channelActive(ctx);
  }

  @Override
  public void channelInactive(final ChannelHandlerContext ctx) throws Exception {
    System.err.println("channelInactive");
    super.channelInactive(ctx);
  }

  @Override
  public void channelReadComplete(final ChannelHandlerContext ctx) throws Exception {
    System.err.println("channelReadComplete");
    super.channelReadComplete(ctx);
  }

  @Override
  public void userEventTriggered(final ChannelHandlerContext ctx, final Object evt) throws Exception {
    System.err.println("userEventTriggered");
    super.userEventTriggered(ctx, evt);
  }

  @Override
  public void channelWritabilityChanged(final ChannelHandlerContext ctx) throws Exception {
    System.err.println("channelWritabilityChanged");
    super.channelWritabilityChanged(ctx);
  }
}
