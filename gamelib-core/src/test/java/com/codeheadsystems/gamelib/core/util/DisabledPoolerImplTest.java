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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.function.Supplier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DisabledPoolerImplTest {

  private static final Supplier<Object> SUPPLIER = Object::new;

  private DisabledPoolerImpl<Object> pooler;

  @BeforeEach
  void setup() {
    pooler = (DisabledPoolerImpl<Object>) Pooler.disabled(SUPPLIER);
  }

  @Test
  void testFullUsage(){
    assertThat(pooler.poolSize()).isEqualTo(0);
    final Object o1 = pooler.obtain();
    assertThat(o1).isNotNull();
    assertThat(pooler.poolSize()).isEqualTo(0);
    final Object o2 = pooler.obtain();
    assertThat(pooler.poolSize()).isEqualTo(0);
    assertThat(o2).isNotEqualTo(o1);
    pooler.free(o2);
    final Object o3 = pooler.obtain();
    assertThat(pooler.poolSize()).isEqualTo(0);
    assertThat(o3).isNotEqualTo(o1);
    assertThat(o2).isNotEqualTo(o1);
  }

}