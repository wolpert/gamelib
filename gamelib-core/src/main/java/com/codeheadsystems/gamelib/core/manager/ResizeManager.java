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

import java.util.Set;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Purpose: Provide a common way to resize things.
 */
@Singleton
public class ResizeManager {

  private final Set<Listener> listeners;

  /**
   * Instantiates a new Resize manager.
   *
   * @param listeners the listeners
   */
  @Inject
  public ResizeManager(final Set<Listener> listeners) {
    this.listeners = listeners;
  }

  /**
   * Resize.
   *
   * @param width  the width
   * @param height the height
   */
  public void resize(int width, int height) {
    listeners.forEach(l -> l.resize(width, height));
  }

  /**
   * The interface Listener.
   */
  @FunctionalInterface
  public interface Listener {
    /**
     * Resize.
     *
     * @param width  the width
     * @param height the height
     */
    void resize(int width, int height);
  }

}
