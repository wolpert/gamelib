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

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PoolerImplTest {

  private Pooler<Object> pool;

  @BeforeEach
  void setup() {
    pool = Pooler.of(Object::new);
  }

  @Test
  void obtain() {
    assertThat(pool.obtain())
        .isNotNull();
    assertThat(pool.poolSize())
        .isEqualTo(0);
  }

  @Test
  void free() {
    final Object object = pool.obtain();
    pool.free(object);

    assertThat(pool.poolSize())
        .isEqualTo(1);
  }

}