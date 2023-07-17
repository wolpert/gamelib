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

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;
import java.util.Objects;

/**
 * The type Layout.
 */
public class Layout implements Pool.Poolable {

  private Orientation orientation;
  private Vector2 size;
  private Vector2 origin;

  /**
   * Of layout.
   *
   * @param orientation the orientation
   * @param size        the size
   * @param origin      the origin
   * @return the layout
   */
  public static Layout of(Orientation orientation, Vector2 size, Vector2 origin) {
    return new Layout().setOrientation(orientation).setSize(size).setOrigin(origin);
  }

  /**
   * Orientation orientation.
   *
   * @return the orientation
   */
  public Orientation orientation() {
    return orientation;
  }

  /**
   * Size vector 2.
   *
   * @return the vector 2
   */
  public Vector2 size() {
    return size;
  }

  /**
   * Origin vector 2.
   *
   * @return the vector 2
   */
  public Vector2 origin() {
    return origin;
  }

  /**
   * Sets orientation.
   *
   * @param orientation the orientation
   * @return the orientation
   */
  public Layout setOrientation(Orientation orientation) {
    this.orientation = orientation;
    return this;
  }

  /**
   * Sets size.
   *
   * @param size the size
   * @return the size
   */
  public Layout setSize(Vector2 size) {
    this.size = size;
    return this;
  }

  /**
   * Sets origin.
   *
   * @param origin the origin
   * @return the origin
   */
  public Layout setOrigin(Vector2 origin) {
    this.origin = origin;
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Layout layout = (Layout) o;
    return orientation.equals(layout.orientation) && size.equals(layout.size) && origin.equals(layout.origin);
  }

  @Override
  public int hashCode() {
    return Objects.hash(orientation, size, origin);
  }

  @Override
  public void reset() {
    origin = null;
    size = null;
    orientation = null;
  }
}
