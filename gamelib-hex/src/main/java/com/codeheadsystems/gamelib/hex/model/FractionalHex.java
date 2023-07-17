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
 * The type Fractional hex.
 */
public class FractionalHex implements Pool.Poolable {

  private double q;
  private double r;
  private double s;

  /**
   * Instantiates a new Fractional hex.
   */
  public FractionalHex() {

  }

  /**
   * Of fractional hex.
   *
   * @param q the q
   * @param r the r
   * @param s the s
   * @return the fractional hex
   */
  public static FractionalHex of(double q, double r, double s) {
    return new FractionalHex().setQ(q).setR(r).setS(s).checkConstructorArguments();
  }

  /**
   * Check constructor arguments fractional hex.
   *
   * @return the fractional hex
   */
  public FractionalHex checkConstructorArguments() {
    if (Math.round(q() + r() + s()) != 0) {
      throw new IllegalArgumentException("q + r + s must equal 0");
    }
    return this;
  }

  /**
   * Q double.
   *
   * @return the double
   */
  public double q() {
    return q;
  }

  /**
   * R double.
   *
   * @return the double
   */
  public double r() {
    return r;
  }

  /**
   * S double.
   *
   * @return the double
   */
  public double s() {
    return s;
  }

  /**
   * Sets q.
   *
   * @param q the q
   * @return the q
   */
  public FractionalHex setQ(double q) {
    this.q = q;
    return this;
  }

  /**
   * Sets r.
   *
   * @param r the r
   * @return the r
   */
  public FractionalHex setR(double r) {
    this.r = r;
    return this;
  }

  /**
   * Sets s.
   *
   * @param s the s
   * @return the s
   */
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
    q = 0;
    r = 0;
    s = 0;
  }
}
