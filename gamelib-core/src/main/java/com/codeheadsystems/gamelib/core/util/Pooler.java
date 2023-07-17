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

package com.codeheadsystems.gamelib.core.util;

import java.util.function.Supplier;

/**
 * The interface Pooler.
 *
 * @param <T> the type parameter
 */
public interface Pooler<T> {

  /**
   * Of pooler.
   *
   * @param <T>      the type parameter
   * @param supplier the supplier
   * @return the pooler
   */
  static <T> Pooler<T> of(final Supplier<T> supplier) {
    return new PoolerImpl<>(supplier);
  }

  /**
   * Disabled pooler.
   *
   * @param <T>      the type parameter
   * @param supplier the supplier
   * @return the pooler
   */
  static <T> Pooler<T> disabled(final Supplier<T> supplier) {
    return new DisabledPoolerImpl<>(supplier);
  }

  /**
   * Obtain t.
   *
   * @return the t
   */
  T obtain();

  /**
   * Free.
   *
   * @param tInstance the t instance
   */
  void free(T tInstance);

  /**
   * Pool size int.
   *
   * @return the int
   */
  int poolSize();

}
