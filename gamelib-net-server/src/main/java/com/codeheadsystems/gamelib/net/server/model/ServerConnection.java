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

package com.codeheadsystems.gamelib.net.server.model;

import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import org.immutables.value.Value;

/**
 * The interface Server connection.
 */
@Value.Immutable
public interface ServerConnection {

  /**
   * Channel channel.
   *
   * @return the channel
   */
  Channel channel();

  /**
   * Boss group event loop group.
   *
   * @return the event loop group
   */
  EventLoopGroup bossGroup();

  /**
   * Worker group event loop group.
   *
   * @return the event loop group
   */
  EventLoopGroup workerGroup();

}
