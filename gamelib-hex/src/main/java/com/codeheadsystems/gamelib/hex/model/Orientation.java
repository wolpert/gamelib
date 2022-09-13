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

import static com.codeheadsystems.gamelib.hex.utilities.MathConverter.toFloat;

import com.badlogic.gdx.utils.Pool;
import java.util.Objects;

public class Orientation implements Pool.Poolable {

    public static Orientation flat = of(3.0f / 2.0f, 0.0f, toFloat(Math.sqrt(3.0) / 2.0), toFloat(Math.sqrt(3.0)), 2.0f / 3.0f, 0.0f, -1.0f / 3.0f, toFloat(Math.sqrt(3.0) / 3.0f), 0.0f);
    public static Orientation pointy = of(toFloat(Math.sqrt(3.0)), toFloat(Math.sqrt(3.0) / 2.0f), 0.0f, 3.0f / 2.0f, toFloat(Math.sqrt(3.0) / 3.0f), -1.0f / 3.0f, 0.0f, 2.0f / 3.0f, 0.5f);
    float f0;
    float f1;
    float f2;
    float f3;
    float b0;
    float b1;
    float b2;
    float b3;
    float startAngle;

    public static Orientation of(float f0, float f1, float f2, float f3, float b0, float b1, float b2, float b3, float start_angle) {
        return new Orientation().setF0(f0).setF1(f1).setF2(f2).setF3(f3)
            .setB0(b0).setB1(b1).setB2(b2).setB3(b3).setStartAngle(start_angle);
    }

    public float f0() {
        return f0;
    }

    public float f1() {
        return f1;
    }

    public float f2() {
        return f2;
    }

    public float f3() {
        return f3;
    }

    public float b0() {
        return b0;
    }

    public float b1() {
        return b1;
    }

    public float b2() {
        return b2;
    }

    public float b3() {
        return b3;
    }

    public float startAngle() {
        return startAngle;
    }

    public Orientation setF0(float f0) {
        this.f0 = f0;
        return this;
    }

    public Orientation setF1(float f1) {
        this.f1 = f1;
        return this;
    }

    public Orientation setF2(float f2) {
        this.f2 = f2;
        return this;
    }

    public Orientation setF3(float f3) {
        this.f3 = f3;
        return this;
    }

    public Orientation setB0(float b0) {
        this.b0 = b0;
        return this;
    }

    public Orientation setB1(float b1) {
        this.b1 = b1;
        return this;
    }

    public Orientation setB2(float b2) {
        this.b2 = b2;
        return this;
    }

    public Orientation setB3(float b3) {
        this.b3 = b3;
        return this;
    }

    public Orientation setStartAngle(float startAngle) {
        this.startAngle = startAngle;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Orientation that = (Orientation) o;
        return Float.compare(that.f0, f0) == 0 && Float.compare(that.f1, f1) == 0 && Float.compare(that.f2, f2) == 0 && Float.compare(that.f3, f3) == 0 && Float.compare(that.b0, b0) == 0 && Float.compare(that.b1, b1) == 0 && Float.compare(that.b2, b2) == 0 && Float.compare(that.b3, b3) == 0 && Float.compare(that.startAngle, startAngle) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(f0, f1, f2, f3, b0, b1, b2, b3, startAngle);
    }

    @Override
    public void reset() {
        f0 = 0;
        f1 = 0;
        f2 = 0;
        f3 = 0;
        b0 = 0;
        b1 = 0;
        b2 = 0;
        b3 = 0;
        startAngle = 0;
    }
}
