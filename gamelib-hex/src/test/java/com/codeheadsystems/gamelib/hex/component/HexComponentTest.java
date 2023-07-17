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

package com.codeheadsystems.gamelib.hex.component;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.badlogic.gdx.math.Vector2;
import com.codeheadsystems.gamelib.hex.manager.LayoutManager;
import com.codeheadsystems.gamelib.hex.model.Hex;
import com.codeheadsystems.gamelib.hex.model.Layout;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class HexComponentTest {

  private static final float[] VERTICES = new float[]{0, 0, 1, 1, 1, 1, 2, 2, 2, 2, 0, 0};
  private static final Hex HEX = Hex.of(0, 0, 0);
  private static final Vector2 VECTOR_2 = new Vector2(10f, 20f);
  @Mock private Layout layout;
  @Mock private LayoutManager layoutManager;
  private HexComponent component;

  @BeforeEach
  void setup() {
    component = new HexComponent();
  }

  @Test
  void testInitialize() {
    when(layoutManager.vertices(layout, HEX)).thenReturn(VERTICES);
    when(layoutManager.hexToPixel(layout, HEX)).thenReturn(VECTOR_2);
    component.initialize(HEX, layout, layoutManager);
    assertThat(component.hex())
        .isEqualTo(HEX);
    assertThat(component.isHex(0, 0, 0))
        .isTrue();
    assertThat(component.isHex(1, 0, -1))
        .isFalse();
    assertThat(component.polygon())
        .isNotNull();
    assertThat(component.vertices())
        .isNotNull();
    assertThat(component.triangles())
        .isNotNull();
  }

  @Test
  void defaultObject() {
    assertThat(component)
        .isNotNull();
    assertThat(component.polygon())
        .isNull();
    assertThat(component.vertices())
        .isNull();
    assertThat(component.isHex(0, 0, 0))
        .isFalse();
  }

  @Test
  void reset() {
    when(layoutManager.vertices(layout, HEX)).thenReturn(VERTICES);
    when(layoutManager.hexToPixel(layout, HEX)).thenReturn(VECTOR_2);
    component.initialize(HEX, layout, layoutManager);
    component.reset();
    assertThat(component.polygon())
        .isNull();
    assertThat(component.vertices())
        .isNull();
  }

  @Test
  void vertices() {
    when(layoutManager.vertices(layout, HEX)).thenReturn(VERTICES);
    when(layoutManager.hexToPixel(layout, HEX)).thenReturn(VECTOR_2);
    component.initialize(HEX, layout, layoutManager);
    assertThat(component.vertices())
        .isNotNull()
        .isEqualTo(VERTICES);
  }

  @Test
  void polygon() {
    when(layoutManager.vertices(layout, HEX)).thenReturn(VERTICES);
    when(layoutManager.hexToPixel(layout, HEX)).thenReturn(VECTOR_2);
    component.initialize(HEX, layout, layoutManager);
    assertThat(component.polygon())
        .isNotNull()
        .extracting("vertices")
        .isEqualTo(VERTICES);
  }
}