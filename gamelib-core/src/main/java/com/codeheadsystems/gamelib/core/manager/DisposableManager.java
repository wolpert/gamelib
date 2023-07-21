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

package com.codeheadsystems.gamelib.core.manager;

import static com.codeheadsystems.gamelib.core.util.LoggerHelper.logger;

import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Logger;
import java.util.Set;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * The type Disposable manager.
 */
@Singleton
public class DisposableManager {

  private final Logger LOGGER = logger(DisposableManager.class);

  private final Set<Disposable> disposables;

  /**
   * Instantiates a new Disposable manager.
   *
   * @param disposables the disposables
   */
  @Inject
  public DisposableManager(final Set<Disposable> disposables) {
    LOGGER.info("Creating DisposableManager");
    this.disposables = disposables;
  }

  /**
   * Dispose.
   */
  public void dispose() {
    LOGGER.info("Disposing DisposableManager");
    disposables.forEach(Disposable::dispose);
  }

}
