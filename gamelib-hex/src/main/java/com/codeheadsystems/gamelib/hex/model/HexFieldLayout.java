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

public class HexFieldLayout implements Pool.Poolable {

    private int rows;
    private int cols;
    private Layout layout;

    public int rows() {
        return rows;
    }

    public int cols() {
        return cols;
    }

    public Layout layout() {
        return layout;
    }

    public HexFieldLayout setRows(int rows) {
        this.rows = rows;
        return this;
    }

    public HexFieldLayout setCols(int cols) {
        this.cols = cols;
        return this;
    }

    public HexFieldLayout setLayout(Layout layout) {
        this.layout = layout;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HexFieldLayout that = (HexFieldLayout) o;
        return rows == that.rows && cols == that.cols && layout.equals(that.layout);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rows, cols, layout);
    }

    @Override
    public void reset() {
        rows = 0;
        cols = 0;
        layout = null;
    }
}
