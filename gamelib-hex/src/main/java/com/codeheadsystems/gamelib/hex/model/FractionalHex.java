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

public class FractionalHex implements Pool.Poolable {

    private double q;
    private double r;
    private double s;

    public static FractionalHex of(double q, double r, double s) {
        return new FractionalHex().setQ(q).setR(r).setS(s).checkConstructorArguments();
    }

    public FractionalHex() {

    }

    public FractionalHex checkConstructorArguments() {
        if (Math.round(q() + r() + s()) != 0) {
            throw new IllegalArgumentException("q + r + s must equal 0");
        }
        return this;
    }

    public double q() {
        return q;
    }

    public double r() {
        return r;
    }

    public double s() {
        return s;
    }

    public FractionalHex setQ(double q) {
        this.q = q;
        return this;
    }

    public FractionalHex setR(double r) {
        this.r = r;
        return this;
    }

    public FractionalHex setS(double s) {
        this.s = s;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FractionalHex that = (FractionalHex) o;
        return Double.compare(that.q, q) == 0 && Double.compare(that.r, r) == 0 && Double.compare(that.s, s) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(q, r, s);
    }

    @Override
    public void reset() {
        q=0;
        r=0;
        s=0;
    }
}
