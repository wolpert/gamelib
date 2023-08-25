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

package com.codeheadsystems.gamelib.loader.manager;

import com.badlogic.gdx.utils.Array;
import java.util.function.Consumer;

/**
 * The type Listener manager.
 *
 * @param <T> the type parameter
 */
public class ListenerManager<T> {

  private final Array<T> listeners;

  /**
   * Instantiates a new Listener manager.
   */
  public ListenerManager() {
    listeners = new Array<>();
  }

  /**
   * Add listener.
   *
   * @param listener the listener
   */
  public void addListener(T listener) {
    listeners.add(listener);
  }

  /**
   * Remove listener.
   *
   * @param listener the listener
   */
  public void removeListener(T listener) {
    listeners.removeValue(listener, true);
  }

  /**
   * For each.
   *
   * @param consumer the consumer
   */
  public void forEach(Consumer<T> consumer) {
    for (int i = listeners.size - 1; i >= 0; i--) {
      consumer.accept(listeners.get(i)); // listeners can remove themselves from the list.
    }
  }

}
