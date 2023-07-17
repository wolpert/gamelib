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

package com.codeheadsystems.gamelib.net.server.manager;

import com.codeheadsystems.gamelib.net.exception.JsonException;
import com.codeheadsystems.gamelib.net.manager.JsonManager;
import com.codeheadsystems.gamelib.net.model.Identity;
import com.codeheadsystems.gamelib.net.server.Authenticator;
import com.codeheadsystems.gamelib.net.server.NetClientHandler;
import dagger.assisted.Assisted;
import dagger.assisted.AssistedInject;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class handles the authentication for an individual connection.
 */
public class AuthenticationManager {

  /**
   * The constant AUTH_FAIL.
   */
  public static final String AUTH_FAIL = "Auth Fail";
  /**
   * The constant AUTH_TIMER_EXPIRED.
   */
  public static final String AUTH_TIMER_EXPIRED = "Auth timer expired";
  private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationManager.class);
  private final Authenticator authenticator;
  private final TimerManager timerManager;
  private final JsonManager jsonManager;
  private final NetClientHandler handler;

  private final AtomicBoolean timerExpired = new AtomicBoolean(false);

  private Future authTimer;

  /**
   * Instantiates a new Authentication manager.
   *
   * @param authenticator the authenticator
   * @param timerManager  the timer manager
   * @param jsonManager   the json manager
   * @param handler       the handler
   */
  @AssistedInject
  public AuthenticationManager(final Authenticator authenticator,
                               final TimerManager timerManager,
                               final JsonManager jsonManager,
                               @Assisted final NetClientHandler handler) {
    LOGGER.info("AuthenticationManager({},{})", authenticator, handler);
    this.authenticator = authenticator;
    this.timerManager = timerManager;
    this.handler = handler;
    this.jsonManager = jsonManager;
  }

  /**
   * Timer expired.
   */
  public void timerExpired() {
    if (timerExpired.compareAndSet(false, true)) { // if it was false and now true, shutdown.
      handler.shutdown(AUTH_TIMER_EXPIRED);
      LOGGER.info("Timer expired on {}", handler);
    } else {
      LOGGER.info("Timer expired and we already managed it on {}", handler);
    }
  }

  /**
   * Authenticate.
   *
   * @param message the message
   */
  public void authenticate(final String message) {
    if (timerExpired.compareAndSet(true, true)) {
      // Timer has expired and we did not beat it out. So we are expecting everything to shutdown.
      LOGGER.info("Authenticate came too late: {}", handler);
      return;
    }
    authTimer.cancel(false);
    // anything goes wrong we shut everything down.
    try {
      final Identity identity = jsonManager.fromJson(message, Identity.class);
      if (authenticator.isAuthenticated(identity)) {
        handler.authenticated();
      } else {
        handler.shutdown(AUTH_FAIL);
      }
    } catch (JsonException j) {
      LOGGER.error("Auth Fail: {}", j.getMessage());
      handler.shutdown(AUTH_FAIL); // don't get a different message to the client.
    } catch (Throwable t) {
      LOGGER.error("Auth Fail: {}", t.getMessage(), t);
      handler.shutdown(AUTH_FAIL); // don't get a different message to the client.
    }
  }

  /**
   * Indicates the handler has been initialized.
   */
  public void initialized() {
    authTimer = timerManager.enabledAuthTimeoutHandler(this);
  }
}
