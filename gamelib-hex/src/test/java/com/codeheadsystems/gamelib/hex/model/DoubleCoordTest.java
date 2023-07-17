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

package com.codeheadsystems.gamelib.hex.model;

import static org.assertj.core.api.Assertions.assertThat;

import com.codeheadsystems.gamelib.hex.GdxTest;
import com.codeheadsystems.gamelib.hex.manager.DoubledCoordManager;
import com.codeheadsystems.gamelib.hex.manager.HexManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DoubleCoordTest extends GdxTest {

  private DoubledCoordManager manager;

  @BeforeEach
  public void setup() {
    manager = new DoubledCoordManager(new HexManager());
  }

  @Test
  public void testDoubledRoundtrip() {
    final Hex a = Hex.of(3, 4, -7);
    final DoubledCoord b = DoubledCoord.of(1, -3);

    assertThat(a)
        .isEqualTo(manager.qdoubledToCube(manager.qdoubledFromCube(a)));
    assertThat(b)
        .isEqualTo(manager.qdoubledFromCube(manager.qdoubledToCube(b)));
    assertThat(a)
        .isEqualTo(manager.rdoubledToCube(manager.rdoubledFromCube(a)));
    assertThat(b)
        .isEqualTo(manager.rdoubledFromCube(manager.rdoubledToCube(b)));
  }

  @Test
  public void testDoubledFromCube() {
    assertThat(DoubledCoord.of(1, 5))
        .isEqualTo(manager.qdoubledFromCube(Hex.of(1, 2, -3)));
    assertThat(DoubledCoord.of(4, 2))
        .isEqualTo(manager.rdoubledFromCube(Hex.of(1, 2, -3)));
  }


  @Test
  public void testDoubledToCube() {
    assertThat(Hex.of(1, 2, -3))
        .isEqualTo(manager.qdoubledToCube(DoubledCoord.of(1, 5)));
    assertThat(Hex.of(1, 2, -3))
        .isEqualTo(manager.rdoubledToCube(DoubledCoord.of(4, 2)));
  }
}