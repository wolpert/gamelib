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

import com.badlogic.gdx.math.Vector2;
import com.codeheadsystems.gamelib.hex.GdxTest;
import com.codeheadsystems.gamelib.hex.manager.FractionalHexManager;
import com.codeheadsystems.gamelib.hex.manager.HexManager;
import com.codeheadsystems.gamelib.hex.manager.LayoutManager;
import com.codeheadsystems.gamelib.hex.manager.Vector2Manager;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LayoutTest extends GdxTest {

  public static final Hex HEX = Hex.of(3, 4, -7);
  public static final Layout LAYOUT = Layout.of(Orientation.pointy, new Vector2().set(10.0f, 10.0f), new Vector2().set(0, 0));
  public static final Vector2 VECTOR_2A = new Vector2().set(10.0f, 10.0f);
  public static final Vector2 VECTOR_2B = new Vector2().set(20.0f, 20.0f);

  private LayoutManager manager;
  private FractionalHexManager fractionalHexManager;

  @BeforeEach
  public void setup() {
    HexManager hexManager = new HexManager();
    fractionalHexManager = new FractionalHexManager(hexManager);
    manager = new LayoutManager(new Vector2Manager(), fractionalHexManager);
  }

  @Test
  public void testLayout() {
    final Layout flat = Layout.of(Orientation.flat, new Vector2().set(10.0f, 15.0f), new Vector2().set(35.0f, 71.0f));
    assertThat(HEX)
        .isEqualTo(fractionalHexManager.hexRound(manager.pixelToHex(flat, manager.hexToPixel(flat, HEX)), true));

    final Layout pointy = Layout.of(Orientation.pointy, new Vector2().set(10.0f, 15.0f), new Vector2().set(35.0f, 71.0f));
    assertThat(HEX)
        .isEqualTo(fractionalHexManager.hexRound(manager.pixelToHex(pointy, manager.hexToPixel(pointy, HEX)), true));
  }

  @Test
  public void polygonCorners() {
    final List<Vector2> result = manager.polygonCorners(LAYOUT, HEX);
    assertThat(result)
        .hasSize(6);
  }

  @Test
  public void vertices() {
    final float[] result = manager.vertices(LAYOUT, HEX);
    assertThat(result)
        .hasSize(12);
  }

  @Test
  void testHashCode() {
    assertThat(LAYOUT.hashCode())
        .isEqualTo(142534242);
  }

  @Test
  void testReset() {
    final Layout flat = Layout.of(Orientation.flat, new Vector2().set(10.0f, 15.0f), new Vector2().set(35.0f, 71.0f));
    flat.reset();
    assertThat(flat.hashCode())
        .isEqualTo(29791);
  }

  @Test
  void equals() {
    final Layout flat = Layout.of(Orientation.flat, new Vector2().set(10.0f, 15.0f), new Vector2().set(35.0f, 71.0f));
    assertThat(LAYOUT.equals(null)).isFalse();
    assertThat(LAYOUT.equals(Layout.class)).isFalse();
    assertThat(LAYOUT.equals(LAYOUT)).isTrue();
    assertThat(flat.equals(LAYOUT)).isFalse();
    assertThat(new Layout().setOrientation(Orientation.flat).setOrigin(VECTOR_2A).setSize(VECTOR_2A)
        .equals(new Layout().setOrientation(Orientation.pointy).setOrigin(VECTOR_2A).setSize(VECTOR_2A)))
        .isFalse();
    assertThat(new Layout().setOrientation(Orientation.flat).setOrigin(VECTOR_2A).setSize(VECTOR_2A)
        .equals(new Layout().setOrientation(Orientation.flat).setOrigin(VECTOR_2B).setSize(VECTOR_2A)))
        .isFalse();
    assertThat(new Layout().setOrientation(Orientation.flat).setOrigin(VECTOR_2A).setSize(VECTOR_2A)
        .equals(new Layout().setOrientation(Orientation.flat).setOrigin(VECTOR_2A).setSize(VECTOR_2B)))
        .isFalse();
  }
}