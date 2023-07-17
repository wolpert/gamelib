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

import com.codeheadsystems.gamelib.net.server.factory.NetClientHandlerFactory;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.json.JsonObjectDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.ssl.SslContext;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The type Channel pipeline initializer.
 */
@Singleton
public class ChannelPipelineInitializer extends ChannelInitializer<SocketChannel> {
  private static final Logger LOGGER = LoggerFactory.getLogger(ChannelPipelineInitializer.class);

  private final SslContext sslCtx;
  private final ChannelGroup channels;
  private final NetClientHandlerFactory factory;

  /**
   * Instantiates a new Channel pipeline initializer.
   *
   * @param sslCtx   the ssl ctx
   * @param channels the channels
   * @param factory  the factory
   */
  @Inject
  public ChannelPipelineInitializer(final SslContext sslCtx,
                                    final ChannelGroup channels,
                                    final NetClientHandlerFactory factory) {
    LOGGER.info("ChannelPipelineInitializer({},{},{})", sslCtx, channels, factory);
    this.sslCtx = sslCtx;
    this.channels = channels;
    this.factory = factory;
  }


  @Override
  public void initChannel(final SocketChannel ch) {
    LOGGER.info("initChannel({})", ch);
    final ChannelPipeline pipeline = ch.pipeline();

    // Add SSL handler first to encrypt and decrypt everything.
    // In this example, we use a bogus certificate in the server side
    // and accept any invalid certificates in the client side.
    // You will need something more complicated to identify both
    // and server in the real world.
    pipeline.addLast(sslCtx.newHandler(ch.alloc()));

    // On top of the SSL handler, add the text line codec.
    pipeline.addLast(new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
    pipeline.addLast(new JsonObjectDecoder());
    pipeline.addLast(new StringDecoder());
    pipeline.addLast(new StringEncoder());

    // and then business logic.
    pipeline.addLast(factory.instance());
  }

}
