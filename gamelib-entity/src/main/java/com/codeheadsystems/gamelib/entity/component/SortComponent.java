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

package com.codeheadsystems.gamelib.entity.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

/**
 * Purpose:
 */
public class SortComponent implements Pool.Poolable, Component {

  /**
   * The Z.
   */
  public int z = 50;

  @Override
  public void reset() {
    z = 50;
  }

  /**
   * Z sort component.
   *
   * @param z the z
   * @return the sort component
   */
  public SortComponent z(int z) {
    this.z = z;
    return this;
  }
}
