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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.codeheadsystems.gamelib.net.client.factory.ClientHandlerFactory;
import com.codeheadsystems.gamelib.net.client.model.NetClientConfiguration;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.json.JsonObjectDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class InitializerTest {

  public static final int PORT = 100;
  public static final String HOST = "HOST";
  @Mock private SslContext sslContext;
  @Mock private ClientHandlerFactory clientHandlerFactory;
  @Mock private NetClientConfiguration netClientConfiguration;

  @Mock private ChannelPipeline channelPipeline;
  @Mock private SslHandler sslHandler;
  @Mock private ByteBufAllocator byteBufAllocator;
  @Mock private ClientHandler clientHandler;
  @Mock private SocketChannel socketChannel;

  @InjectMocks
  private Initializer initializer;

  @Test
  public void testInitialize() throws Exception {
    when(socketChannel.pipeline()).thenReturn(channelPipeline);
    when(socketChannel.alloc()).thenReturn(byteBufAllocator);
    when(netClientConfiguration.host()).thenReturn(HOST);
    when(netClientConfiguration.port()).thenReturn(PORT);
    when(clientHandlerFactory.instance()).thenReturn(clientHandler);
    when(sslContext.newHandler(byteBufAllocator, HOST, PORT)).thenReturn(sslHandler);

    initializer.initChannel(socketChannel);

    verify(channelPipeline).addLast(sslHandler);
    verify(channelPipeline).addLast(any(DelimiterBasedFrameDecoder.class));
    verify(channelPipeline).addLast(any(JsonObjectDecoder.class));
    verify(channelPipeline).addLast(any(StringDecoder.class));
    verify(channelPipeline).addLast(any(StringEncoder.class));
    verify(channelPipeline).addLast(sslHandler);
  }
}