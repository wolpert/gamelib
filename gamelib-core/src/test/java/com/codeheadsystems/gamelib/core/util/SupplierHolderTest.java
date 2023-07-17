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

import org.junit.jupiter.api.Test;

/**
 * Purpose: test class.
 */
class SupplierHolderTest {

  private static final String ONE = "one";
  private static final String TWO = "two";
  private static final String THREE = "three";

  @Test
  void get_currentNull() {
    final SupplierHolder<String> holder = new SupplierHolder<>(null, () -> ONE);

    assertThat(holder.get())
        .isNotNull()
        .isEqualTo(ONE);
  }

  @Test
  void get_currentNotNull() {
    final SupplierHolder<String> holder = new SupplierHolder<>(TWO, () -> ONE);

    assertThat(holder.get())
        .isNotNull()
        .isEqualTo(TWO);
  }

  @Test
  void set_currentNull() {
    final SupplierHolder<String> holder = new SupplierHolder<>(null, () -> ONE);

    holder.set(TWO);

    assertThat(holder.get())
            .isNotNull()
            .isEqualTo(TWO);
  }

  @Test
  void set_currentNotNull() {
    final SupplierHolder<String> holder = new SupplierHolder<>(TWO, () -> ONE);

    holder.set(THREE);

    assertThat(holder.get())
            .isNotNull()
            .isEqualTo(THREE);
  }

  @Test
  void set_currentNullSetToNull() {
    final SupplierHolder<String> holder = new SupplierHolder<>(null, () -> ONE);

    holder.set(null);

    assertThat(holder.get())
            .isNotNull()
            .isEqualTo(ONE);
  }

  @Test
  void set_currentNotNullSetToNull() {
    final SupplierHolder<String> holder = new SupplierHolder<>(TWO, () -> ONE);

    holder.set(null);

    assertThat(holder.get())
            .isNotNull()
            .isEqualTo(ONE);
  }
}