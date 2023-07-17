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

import static com.codeheadsystems.gamelib.net.server.module.NetServerModule.TIMER_EXECUTOR_SERVICE;

import com.codeheadsystems.gamelib.net.server.model.NetServerConfiguration;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The type Timer manager.
 */
@Singleton
public class TimerManager {
  private static final Logger LOGGER = LoggerFactory.getLogger(TimerManager.class);
  private final ScheduledThreadPoolExecutor executorService;
  private final NetServerConfiguration configuration;

  /**
   * Instantiates a new Timer manager.
   *
   * @param executorService the executor service
   * @param configuration   the configuration
   */
  @Inject
  public TimerManager(@Named(TIMER_EXECUTOR_SERVICE) final ScheduledThreadPoolExecutor executorService,
                      final NetServerConfiguration configuration) {
    LOGGER.info("TimerManager({},{})", configuration, executorService);
    this.executorService = executorService;
    this.configuration = configuration;
  }

  /**
   * Enabled auth timeout handler future.
   *
   * @param manager the manager
   * @return the future
   */
  public Future enabledAuthTimeoutHandler(final AuthenticationManager manager) {
    return executorService.schedule(manager::timerExpired,
        configuration.authTimeoutMilliseconds(), TimeUnit.MILLISECONDS);
  }

}
