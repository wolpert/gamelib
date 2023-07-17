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
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Pool;

/**
 * The type Sprite component.
 */
public class SpriteComponent implements Pool.Poolable, Component {

  private Sprite sprite;

  @Override
  public void reset() {
    sprite = null;
  }

  /**
   * Gets sprite.
   *
   * @return the sprite
   */
  public Sprite getSprite() {
    return sprite;
  }

  /**
   * Sprite sprite component.
   *
   * @param sprite the sprite
   * @return the sprite component
   */
  public SpriteComponent sprite(Sprite sprite) {
    this.sprite = sprite;
    return this;
  }

}
