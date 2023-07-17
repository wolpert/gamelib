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

package com.codeheadsystems.gamelib.net.server.manager;

import com.codeheadsystems.gamelib.net.server.factory.ServerConnectionFactory;
import com.codeheadsystems.gamelib.net.server.model.ServerConnection;
import io.netty.channel.ChannelFuture;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The type Server manager.
 */
@Singleton
public class ServerManager {
  private static final Logger LOGGER = LoggerFactory.getLogger(ServerManager.class);

  private final ServerConnectionFactory serverConnectionFactory;
  private ServerConnection serverConnection;
  private State state;

  /**
   * Instantiates a new Server manager.
   *
   * @param serverConnectionFactory the server connection factory
   */
  @Inject
  public ServerManager(final ServerConnectionFactory serverConnectionFactory) {
    this.serverConnectionFactory = serverConnectionFactory;
    LOGGER.info("ServerManager({})", serverConnectionFactory);
    setState(State.OFFLINE);
  }

  /**
   * Execute server boolean.
   *
   * @return the boolean
   */
  public boolean executeServer() {
    LOGGER.info("executeServer()");
    if (!state.equals(State.OFFLINE)) {
      return false;
    }
    setState(State.STARTING);
    serverConnection = serverConnectionFactory.instance();
    setState(State.RUNNING);
    serverConnection.channel().closeFuture().addListener(future -> {
      setState(State.STOPPING);
      serverConnection.bossGroup().shutdownGracefully();
      serverConnection.workerGroup().shutdownGracefully();
      setState(State.OFFLINE);
    });
    return true;
  }

  /**
   * Wait on close boolean.
   *
   * @return the boolean
   */
  public boolean waitOnClose() {
    if (state.equals(State.RUNNING)) {
      try {
        serverConnection.channel().closeFuture().sync();
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
      return true;
    } else {
      return false;
    }
  }

  /**
   * Gets state.
   *
   * @return the state
   */
  public State getState() {
    return state;
  }

  private void setState(final State state) {
    LOGGER.info("State change {} -> {}", this.state, state);
    this.state = state;
  }

  /**
   * Tell ourselves to close.
   *
   * @return the channel future
   */
  public ChannelFuture stop() {
    LOGGER.info("stop()");
    setState(State.STOPPING);
    return serverConnection.channel().close();
  }

  /**
   * Close future channel future.
   *
   * @return the channel future
   */
  public ChannelFuture closeFuture() {
    return serverConnection.channel().closeFuture();
  }

  /**
   * The enum State.
   */
  public enum State {
    /**
     * Offline state.
     */
    OFFLINE,
    /**
     * Starting state.
     */
    STARTING,
    /**
     * Running state.
     */
    RUNNING,
    /**
     * Stopping state.
     */
    STOPPING}

}
