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

import com.codeheadsystems.gamelib.net.client.component.ClientComponent;
import com.codeheadsystems.gamelib.net.client.component.DaggerClientComponent;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

public class Client {

  public static void main(String[] args) throws Exception {

    final ClientComponent clientComponent = DaggerClientComponent.builder()
        .build();

    try {
      // Start the connection attempt.
      Channel ch = clientComponent.channelFactory().instance();

      // Read commands from the stdin.
      ch.writeAndFlush("{\"value\":1}\r\n");
      final String message = clientComponent.queue().take();
      System.out.println("Message: " + message);
      clientComponent.queue().poll(1000, TimeUnit.MILLISECONDS);
    } finally {
      // The connection is closed automatically on shutdown.
      System.out.println("Shutting down");
      clientComponent.eventLoopGroup().shutdownGracefully();
    }
  }
}
