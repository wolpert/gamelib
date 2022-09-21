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

package com.codeheadsystems.gamelib.net.client.factory;

import com.codeheadsystems.gamelib.net.client.model.NetClientConfiguration;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ChannelFactory {

  private final NetClientConfiguration netClientConfiguration;
  private final Bootstrap bootstrap;

  @Inject
  public ChannelFactory(final NetClientConfiguration netClientConfiguration,
                        final Bootstrap bootstrap) {
    this.netClientConfiguration = netClientConfiguration;
    this.bootstrap = bootstrap;
  }

  public Channel instance() {
    try {
      return bootstrap.connect(netClientConfiguration.host(), netClientConfiguration.port()).sync().channel();
    } catch (InterruptedException e) {
      throw new IllegalStateException(e);
    }
  }

}
