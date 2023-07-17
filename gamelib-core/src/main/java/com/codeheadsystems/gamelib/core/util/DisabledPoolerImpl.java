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

package com.codeheadsystems.gamelib.core.util;

import java.util.function.Supplier;

/**
 * A disabled pool. Used when you want caching off.
 *
 * @param <T> type of object to supply.
 */
public class DisabledPoolerImpl<T> implements Pooler<T> {

  private final Supplier<T> supplier;

  /**
   * Instantiates a new Disabled pooler.
   *
   * @param supplier the supplier
   */
  protected DisabledPoolerImpl(final Supplier<T> supplier) {
    this.supplier = supplier;
  }

  @Override
  public T obtain() {
    return supplier.get();
  }

  @Override
  public void free(T tInstance) {

  }

  @Override
  public int poolSize() {
    return 0;
  }
}
