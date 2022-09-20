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

package com.codeheadsystems.gamelib.net.server;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.codeheadsystems.gamelib.net.manager.JsonManager;
import com.codeheadsystems.gamelib.net.server.factory.AuthenticationManagerFactory;
import com.codeheadsystems.gamelib.net.server.manager.AuthenticationManager;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.group.ChannelGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class NetClientHandlerTest {

  private static final String MESSAGE = "message";

  @Mock private ChannelGroup channels;
  @Mock private JsonManager jsonManager;
  @Mock private AuthenticationManagerFactory authenticationManagerFactory;
  @Mock private AuthenticationManager authenticationManager;
  @Mock private Channel channel;
  @Mock private ChannelHandlerContext ctx;

  private NetClientHandler handler;

  @BeforeEach
  void setup() {
    when(authenticationManagerFactory.instance(any())).thenReturn(authenticationManager);
    handler = new NetClientHandler(channels, jsonManager, authenticationManagerFactory);
  }

  @Test
  public void initiallyOffline() {
    assertThat(handler)
        .hasFieldOrPropertyWithValue("status", NetClientHandler.Status.OFFLINE);
  }

  @Test
  public void exceptionHandling() {
    when(ctx.channel()).thenReturn(channel);

    handler.exceptionCaught(ctx, new NullPointerException());

    assertThat(handler)
        .hasFieldOrPropertyWithValue("status", NetClientHandler.Status.STOPPED);
    verify(ctx).close();
  }

  @Test
  void readMessage_outOfOrder() throws Exception {
    when(ctx.channel()).thenReturn(channel);

    handler.channelRead0(ctx, MESSAGE);

    assertThat(handler)
        .hasFieldOrPropertyWithValue("status", NetClientHandler.Status.OFFLINE);
  }


}