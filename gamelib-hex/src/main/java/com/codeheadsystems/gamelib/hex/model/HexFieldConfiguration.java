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

import com.badlogic.gdx.utils.Pool;
import java.util.Objects;

/**
 * The type Hex field configuration.
 */
public class HexFieldConfiguration implements Pool.Poolable {

    private int rows;
    private int cols;
    private Orientation orientation;
    private float sizeX;
    private float sizeY;
    private float originX;
    private float originY;

    @Override public void reset() {
        rows = 0;
        cols = 0;
        orientation = null;
        sizeX = 0;
        sizeY = 0;
        originX = 0;
        originY = 0;
    }

  /**
   * Gets rows.
   *
   * @return the rows
   */
  public int getRows() {
        return rows;
    }

  /**
   * Sets rows.
   *
   * @param rows the rows
   * @return the rows
   */
  public HexFieldConfiguration setRows(final int rows) {
        this.rows = rows;
        return this;
    }

  /**
   * Gets cols.
   *
   * @return the cols
   */
  public int getCols() {
        return cols;
    }

  /**
   * Sets cols.
   *
   * @param cols the cols
   * @return the cols
   */
  public HexFieldConfiguration setCols(final int cols) {
        this.cols = cols;
        return this;
    }

  /**
   * Gets orientation.
   *
   * @return the orientation
   */
  public Orientation getOrientation() {
        return orientation;
    }

  /**
   * Sets orientation.
   *
   * @param orientation the orientation
   * @return the orientation
   */
  public HexFieldConfiguration setOrientation(final Orientation orientation) {
        this.orientation = orientation;
        return this;
    }

  /**
   * Gets size x.
   *
   * @return the size x
   */
  public float getSizeX() {
        return sizeX;
    }

  /**
   * Sets size x.
   *
   * @param sizeX the size x
   * @return the size x
   */
  public HexFieldConfiguration setSizeX(final float sizeX) {
        this.sizeX = sizeX;
        return this;
    }

  /**
   * Gets size y.
   *
   * @return the size y
   */
  public float getSizeY() {
        return sizeY;
    }

  /**
   * Sets size y.
   *
   * @param sizeY the size y
   * @return the size y
   */
  public HexFieldConfiguration setSizeY(final float sizeY) {
        this.sizeY = sizeY;
        return this;
    }

  /**
   * Gets origin x.
   *
   * @return the origin x
   */
  public float getOriginX() {
        return originX;
    }

  /**
   * Sets origin x.
   *
   * @param originX the origin x
   * @return the origin x
   */
  public HexFieldConfiguration setOriginX(final float originX) {
        this.originX = originX;
        return this;
    }

  /**
   * Gets origin y.
   *
   * @return the origin y
   */
  public float getOriginY() {
        return originY;
    }

  /**
   * Sets origin y.
   *
   * @param originY the origin y
   * @return the origin y
   */
  public HexFieldConfiguration setOriginY(final float originY) {
        this.originY = originY;
        return this;
    }

    @Override public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final HexFieldConfiguration that = (HexFieldConfiguration) o;
        return rows == that.rows && cols == that.cols && Float.compare(that.sizeX, sizeX) == 0 && Float.compare(that.sizeY, sizeY) == 0 && Float.compare(that.originX, originX) == 0 && Float.compare(that.originY, originY) == 0 && Objects.equals(orientation, that.orientation);
    }

    @Override public int hashCode() {
        return Objects.hash(rows, cols, orientation, sizeX, sizeY, originX, originY);
    }
}
