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

import com.badlogic.gdx.utils.Pool;
import java.util.function.Supplier;

/**
 * The type Pooler.
 *
 * @param <T> the type parameter
 */
public class PoolerImpl<T> implements Pooler<T> {

    final private Pool<T> pool;

  /**
   * Instantiates a new Pooler.
   *
   * @param supplier the supplier
   */
  protected PoolerImpl(final Supplier<T> supplier) {
        this.pool = new Pool<>() {
            @Override
            protected T newObject() {
                return supplier.get();
            }
        };
    }

    @Override
    public T obtain() {
        return pool.obtain();
    }

    @Override
    public void free(T tInstance) {
        pool.free(tInstance);
    }

    @Override
    public int poolSize() {
        return pool.getFree();
    }
}
