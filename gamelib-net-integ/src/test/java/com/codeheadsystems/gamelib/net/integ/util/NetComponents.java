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

package com.codeheadsystems.gamelib.net.integ.util;

import com.codeheadsystems.gamelib.net.client.manager.ClientManager;
import com.codeheadsystems.gamelib.net.integ.component.DaggerTestClientComponent;
import com.codeheadsystems.gamelib.net.integ.component.DaggerTestServerComponent;
import com.codeheadsystems.gamelib.net.integ.component.TestClientComponent;
import com.codeheadsystems.gamelib.net.integ.component.TestServerComponent;
import com.codeheadsystems.gamelib.net.model.Identity;
import com.codeheadsystems.gamelib.net.model.ImmutableIdentity;
import com.codeheadsystems.gamelib.net.server.Authenticator;
import com.codeheadsystems.gamelib.net.server.GameListener;
import com.codeheadsystems.gamelib.net.server.manager.ServerManager;
import com.codeheadsystems.gamelib.net.server.module.NetServerModule;
import io.netty.channel.ChannelFuture;
import java.util.concurrent.BlockingQueue;

public class NetComponents {

  private static final Identity IDENTITY = ImmutableIdentity.builder().id("id").token("token").build();

  private final ServerManager serverManager;
  private final ClientManager clientManager;
  private final BlockingQueue<String> queue;

  public NetComponents(final Authenticator authenticator,
                       final GameListener gameListener) {
    final TestServerComponent serverComponent = DaggerTestServerComponent.builder()
        .netServerModule(new NetServerModule(authenticator, gameListener))
        .build();
    final TestClientComponent clientComponent = DaggerTestClientComponent.builder()
        .build();
    // start server
    serverManager = serverComponent.serverManager();
    clientManager = clientComponent.clientManager();
    queue = clientComponent.queue();
  }

  public ServerManager serverManager() {
    return serverManager;
  }

  public ClientManager clientManager() {
    return clientManager;
  }

  public BlockingQueue<String> queue() {
    return queue;
  }

  public ChannelFuture sendMessageFromClient(final String msg) {
    return clientManager.sendMessage(msg);
  }

  public NetComponents start() {
    serverManager.executeServer(); // enable the server
    clientManager.connect(IDENTITY); // enable the client.
    return this;
  }

  public NetComponents stop() {
    clientManager.disconnect();
    serverManager.stop();
    serverManager.waitOnClose();
    return this;
  }

}