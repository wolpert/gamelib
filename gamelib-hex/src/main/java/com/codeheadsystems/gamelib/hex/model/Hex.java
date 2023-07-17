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

/**
 * Purpose: define a hex class and its arguments.
 */
public class Hex implements Pool.Poolable {

    private int q;
    private int r;
    private int s;

  /**
   * Of hex.
   *
   * @param q the q
   * @param r the r
   * @param s the s
   * @return the hex
   */
  public static Hex of(int q, int r, int s) {
        return new Hex().setQ(q).setR(r).setS(s).checkConstructorArguments();
    }

  /**
   * Q int.
   *
   * @return the int
   */
  public int q() {
        return q;
    }

  /**
   * R int.
   *
   * @return the int
   */
  public int r() {
        return r;
    }

  /**
   * S int.
   *
   * @return the int
   */
  public int s() {
        return s;
    }

  /**
   * Set hex.
   *
   * @param q the q
   * @param r the r
   * @param s the s
   * @return the hex
   */
  public Hex set(final int q, final int r, final int s) {
        this.q = q;
        this.r = r;
        this.s = s;
        return this;
    }

  /**
   * Sets q.
   *
   * @param q the q
   * @return the q
   */
  public Hex setQ(int q) {
        this.q = q;
        return this;
    }

  /**
   * Sets r.
   *
   * @param r the r
   * @return the r
   */
  public Hex setR(int r) {
        this.r = r;
        return this;
    }

  /**
   * Sets s.
   *
   * @param s the s
   * @return the s
   */
  public Hex setS(int s) {
        this.s = s;
        return this;
    }

  /**
   * Check constructor arguments hex.
   *
   * @return the hex
   */
  public Hex checkConstructorArguments() {
        if (q() + r() + s() != 0) {
            throw new IllegalArgumentException("q + r + s must equal 0");
        }
        return this;
    }

  /**
   * Same hex boolean.
   *
   * @param otherQ the other q
   * @param otherR the other r
   * @param otherS the other s
   * @return the boolean
   */
  public boolean sameHex(final int otherQ, final int otherR, final int otherS) {
        return otherQ == q() && otherR == r() && otherS == s();
    }

  /**
   * Length int.
   *
   * @return the int
   */
  public int length() {
        return (Math.abs(q()) + Math.abs(r()) + Math.abs(s())) / 2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Hex hex = (Hex) o;
        return q == hex.q && r == hex.r && s == hex.s;
    }

    @Override
    public int hashCode() {
        return Objects.hash(q, r, s);
    }

    @Override
    public String toString() {
        return "Hex{" +
            "q=" + q +
            ", r=" + r +
            ", s=" + s +
            '}';
    }

    @Override
    public void reset() {
        q = 0;
        r = 0;
        s = 0;
    }
}
