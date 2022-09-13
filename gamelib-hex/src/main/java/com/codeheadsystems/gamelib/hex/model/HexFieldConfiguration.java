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

package com.codeheadsystems.gamelib.hex.model;

import com.badlogic.gdx.utils.Pool;
import java.util.Objects;

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

    public int getRows() {
        return rows;
    }

    public HexFieldConfiguration setRows(final int rows) {
        this.rows = rows;
        return this;
    }

    public int getCols() {
        return cols;
    }

    public HexFieldConfiguration setCols(final int cols) {
        this.cols = cols;
        return this;
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public HexFieldConfiguration setOrientation(final Orientation orientation) {
        this.orientation = orientation;
        return this;
    }

    public float getSizeX() {
        return sizeX;
    }

    public HexFieldConfiguration setSizeX(final float sizeX) {
        this.sizeX = sizeX;
        return this;
    }

    public float getSizeY() {
        return sizeY;
    }

    public HexFieldConfiguration setSizeY(final float sizeY) {
        this.sizeY = sizeY;
        return this;
    }

    public float getOriginX() {
        return originX;
    }

    public HexFieldConfiguration setOriginX(final float originX) {
        this.originX = originX;
        return this;
    }

    public float getOriginY() {
        return originY;
    }

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
