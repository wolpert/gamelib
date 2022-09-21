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

public class Client {
  static final String HOST = System.getProperty("host", "127.0.0.1");
  static final int PORT = Integer.parseInt(System.getProperty("port", "8992"));

  public static void main(String[] args) throws Exception {

    final ClientComponent clientComponent = DaggerClientComponent.builder()

        .build();

    try {
      Bootstrap b = clientComponent.bootstrap();
      // Start the connection attempt.
      Channel ch = b.connect(HOST, PORT).sync().channel();

      // Read commands from the stdin.
      ChannelFuture lastWriteFuture = ch.writeAndFlush("{\"value\":1}\r\n");
      Thread.sleep(5000);
      BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
      for (; ; ) {
        if (!ch.isOpen()) {
          System.out.println("Channel is closed");
          break;
        }
        String line = in.readLine();
        if (line == null) {
          continue;
        }

        // Sends the received line to the server.
        lastWriteFuture = ch.writeAndFlush(line + "\r\n");

        // If user typed the 'bye' command, wait until the server closes
        // the connection.
        if ("bye".equalsIgnoreCase(line)) {
          ch.closeFuture().sync();
          break;
        }
      }

      // Wait until all messages are flushed before closing the channel.
      if (lastWriteFuture != null) {
        lastWriteFuture.sync();
      }
    } finally {
      // The connection is closed automatically on shutdown.
      clientComponent.eventLoopGroup().shutdownGracefully();
    }
  }
}
