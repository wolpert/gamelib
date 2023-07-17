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
 * The type Offset coord.
 */
public class OffsetCoord implements Pool.Poolable {

    private int col;
    private int row;

  /**
   * Of offset coord.
   *
   * @param col the col
   * @param row the row
   * @return the offset coord
   */
  public static OffsetCoord of(int col, int row) {
        return new OffsetCoord().setCol(col).setRow(row);
    }

  /**
   * Col int.
   *
   * @return the int
   */
  public int col() {
        return col;
    }

  /**
   * Row int.
   *
   * @return the int
   */
  public int row() {
        return row;
    }

  /**
   * Sets col.
   *
   * @param col the col
   * @return the col
   */
  public OffsetCoord setCol(int col) {
        this.col = col;
        return this;
    }

  /**
   * Sets row.
   *
   * @param row the row
   * @return the row
   */
  public OffsetCoord setRow(int row) {
        this.row = row;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OffsetCoord that = (OffsetCoord) o;
        return col == that.col && row == that.row;
    }

    @Override
    public int hashCode() {
        return Objects.hash(col, row);
    }

    @Override
    public void reset() {
        row = 0;
        col = 0;
    }

  /**
   * The enum Offset.
   */
  public enum Offset {
    /**
     * Even offset.
     */
    EVEN(1),
    /**
     * Odd offset.
     */
    ODD(-1);
    /**
     * The Value.
     */
    public int value;

        Offset(int i) {
            this.value = i;
        }
    }
}
