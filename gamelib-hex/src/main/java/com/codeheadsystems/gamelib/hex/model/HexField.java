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

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Pool;
import java.util.Map;
import java.util.Objects;

/**
 * This contains the superset of the hex fields. One instance of a screen contains this object.
 */
public class HexField implements Pool.Poolable {


    private HexFieldLayout hexFieldLayout;
    private Map<Hex, Entity> hexEntityHashMap;

    @Override
    public void reset() {
        hexFieldLayout = null;
        if (hexEntityHashMap != null) {
            hexEntityHashMap.clear();
            hexEntityHashMap = null;
        }
    }

  /**
   * Hex entity hash map map.
   *
   * @return the map
   */
  public Map<Hex, Entity> hexEntityHashMap() {
        return this.hexEntityHashMap;
    }

  /**
   * Sets hex entity hash map.
   *
   * @param hexEntityHashMap the hex entity hash map
   * @return the hex entity hash map
   */
  public HexField setHexEntityHashMap(final Map<Hex, Entity> hexEntityHashMap) {
        this.hexEntityHashMap = hexEntityHashMap;
        return this;
    }

  /**
   * Gets hex field layout.
   *
   * @return the hex field layout
   */
  public HexFieldLayout getHexFieldLayout() {
        return hexFieldLayout;
    }

  /**
   * Sets hex field layout.
   *
   * @param hexFieldLayout the hex field layout
   * @return the hex field layout
   */
  public HexField setHexFieldLayout(final HexFieldLayout hexFieldLayout) {
        this.hexFieldLayout = hexFieldLayout;
        return this;
    }

    @Override public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final HexField hexField = (HexField) o;
        return Objects.equals(hexFieldLayout, hexField.hexFieldLayout) && Objects.equals(hexEntityHashMap, hexField.hexEntityHashMap);
    }

    @Override public int hashCode() {
        return Objects.hash(hexFieldLayout, hexEntityHashMap);
    }
}
