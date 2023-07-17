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

package com.codeheadsystems.gamelib.entity.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

/**
 * The type Resizable component.
 */
public class ResizableComponent implements Pool.Poolable, Component {

  private float width;

  @Override
  public void reset() {
    width = 0;
  }

  /**
   * Sets width.
   *
   * @param w the w
   * @return the width
   */
  public ResizableComponent setWidth(float w) {
    this.width = w;
    return this;
  }

  /**
   * Width float.
   *
   * @return the float
   */
  public float width() {
    return width;
  }
}
