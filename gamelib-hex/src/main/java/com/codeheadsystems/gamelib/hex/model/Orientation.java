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

import static com.codeheadsystems.gamelib.hex.utilities.MathConverter.toFloat;

import com.badlogic.gdx.utils.Pool;
import java.util.Objects;

/**
 * The type Orientation.
 */
public class Orientation implements Pool.Poolable {

  /**
   * The constant flat.
   */
  public static Orientation flat = of(3.0f / 2.0f, 0.0f, toFloat(Math.sqrt(3.0) / 2.0), toFloat(Math.sqrt(3.0)), 2.0f / 3.0f, 0.0f, -1.0f / 3.0f, toFloat(Math.sqrt(3.0) / 3.0f), 0.0f);
  /**
   * The constant pointy.
   */
  public static Orientation pointy = of(toFloat(Math.sqrt(3.0)), toFloat(Math.sqrt(3.0) / 2.0f), 0.0f, 3.0f / 2.0f, toFloat(Math.sqrt(3.0) / 3.0f), -1.0f / 3.0f, 0.0f, 2.0f / 3.0f, 0.5f);
  /**
   * The F 0.
   */
  float f0;
  /**
   * The F 1.
   */
  float f1;
  /**
   * The F 2.
   */
  float f2;
  /**
   * The F 3.
   */
  float f3;
  /**
   * The B 0.
   */
  float b0;
  /**
   * The B 1.
   */
  float b1;
  /**
   * The B 2.
   */
  float b2;
  /**
   * The B 3.
   */
  float b3;
  /**
   * The Start angle.
   */
  float startAngle;

  /**
   * Of orientation.
   *
   * @param f0          the f 0
   * @param f1          the f 1
   * @param f2          the f 2
   * @param f3          the f 3
   * @param b0          the b 0
   * @param b1          the b 1
   * @param b2          the b 2
   * @param b3          the b 3
   * @param start_angle the start angle
   * @return the orientation
   */
  public static Orientation of(float f0, float f1, float f2, float f3, float b0, float b1, float b2, float b3, float start_angle) {
    return new Orientation().setF0(f0).setF1(f1).setF2(f2).setF3(f3)
        .setB0(b0).setB1(b1).setB2(b2).setB3(b3).setStartAngle(start_angle);
  }

  /**
   * F 0 float.
   *
   * @return the float
   */
  public float f0() {
    return f0;
  }

  /**
   * F 1 float.
   *
   * @return the float
   */
  public float f1() {
    return f1;
  }

  /**
   * F 2 float.
   *
   * @return the float
   */
  public float f2() {
    return f2;
  }

  /**
   * F 3 float.
   *
   * @return the float
   */
  public float f3() {
    return f3;
  }

  /**
   * B 0 float.
   *
   * @return the float
   */
  public float b0() {
    return b0;
  }

  /**
   * B 1 float.
   *
   * @return the float
   */
  public float b1() {
    return b1;
  }

  /**
   * B 2 float.
   *
   * @return the float
   */
  public float b2() {
    return b2;
  }

  /**
   * B 3 float.
   *
   * @return the float
   */
  public float b3() {
    return b3;
  }

  /**
   * Start angle float.
   *
   * @return the float
   */
  public float startAngle() {
    return startAngle;
  }

  /**
   * Sets f 0.
   *
   * @param f0 the f 0
   * @return the f 0
   */
  public Orientation setF0(float f0) {
    this.f0 = f0;
    return this;
  }

  /**
   * Sets f 1.
   *
   * @param f1 the f 1
   * @return the f 1
   */
  public Orientation setF1(float f1) {
    this.f1 = f1;
    return this;
  }

  /**
   * Sets f 2.
   *
   * @param f2 the f 2
   * @return the f 2
   */
  public Orientation setF2(float f2) {
    this.f2 = f2;
    return this;
  }

  /**
   * Sets f 3.
   *
   * @param f3 the f 3
   * @return the f 3
   */
  public Orientation setF3(float f3) {
    this.f3 = f3;
    return this;
  }

  /**
   * Sets b 0.
   *
   * @param b0 the b 0
   * @return the b 0
   */
  public Orientation setB0(float b0) {
    this.b0 = b0;
    return this;
  }

  /**
   * Sets b 1.
   *
   * @param b1 the b 1
   * @return the b 1
   */
  public Orientation setB1(float b1) {
    this.b1 = b1;
    return this;
  }

  /**
   * Sets b 2.
   *
   * @param b2 the b 2
   * @return the b 2
   */
  public Orientation setB2(float b2) {
    this.b2 = b2;
    return this;
  }

  /**
   * Sets b 3.
   *
   * @param b3 the b 3
   * @return the b 3
   */
  public Orientation setB3(float b3) {
    this.b3 = b3;
    return this;
  }

  /**
   * Sets start angle.
   *
   * @param startAngle the start angle
   * @return the start angle
   */
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
