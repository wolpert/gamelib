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

package com.codeheadsystems.gamelib.net.client;

import com.codeheadsystems.gamelib.net.client.factory.ClientHandlerFactory;
import com.codeheadsystems.gamelib.net.client.model.NetClientConfiguration;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.json.JsonObjectDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.ssl.SslContext;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * The type Initializer.
 */
@Singleton
public class Initializer extends ChannelInitializer<SocketChannel> {

  private final SslContext sslCtx;
  private final ClientHandlerFactory clientHandlerFactory;
  private final NetClientConfiguration netClientConfiguration;

  /**
   * Instantiates a new Initializer.
   *
   * @param sslCtx                 the ssl ctx
   * @param clientHandlerFactory   the client handler factory
   * @param netClientConfiguration the net client configuration
   */
  @Inject
  public Initializer(final SslContext sslCtx,
                     final ClientHandlerFactory clientHandlerFactory,
                     final NetClientConfiguration netClientConfiguration) {
    this.sslCtx = sslCtx;
    this.clientHandlerFactory = clientHandlerFactory;
    this.netClientConfiguration = netClientConfiguration;
  }

  @Override
  public void initChannel(final SocketChannel ch) throws Exception {
    final ChannelPipeline pipeline = ch.pipeline();

    // Add SSL handler first to encrypt and decrypt everything.
    // In this example, we use a bogus certificate in the server side
    // and accept any invalid certificates in the client side.
    // You will need something more complicated to identify both
    // and server in the real world.
    pipeline.addLast(sslCtx.newHandler(ch.alloc(), netClientConfiguration.host(), netClientConfiguration.port()));

    // On top of the SSL handler, add the text line codec.
    pipeline.addLast(new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
    pipeline.addLast(new JsonObjectDecoder());
    pipeline.addLast(new StringDecoder());
    pipeline.addLast(new StringEncoder());

    // and then business logic.
    pipeline.addLast(clientHandlerFactory.instance());
  }

}
