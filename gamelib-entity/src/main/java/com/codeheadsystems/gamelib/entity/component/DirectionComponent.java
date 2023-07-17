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
 * The type Direction component.
 */
public class DirectionComponent implements Pool.Poolable, Component {

  private Direction direction;

  @Override
  public void reset() {
  }

  /**
   * Gets direction.
   *
   * @return the direction
   */
  public Direction getDirection() {
    return direction;
  }

  /**
   * Direction direction component.
   *
   * @param direction the direction
   * @return the direction component
   */
  public DirectionComponent direction(Direction direction) {
    this.direction = direction;
    return this;
  }

  /**
   * The enum Direction.
   */
  public enum Direction {
    /**
     * East direction.
     */
    EAST,
    /**
     * West direction.
     */
    WEST,
    /**
     * South direction.
     */
    SOUTH,
    /**
     * North direction.
     */
    NORTH}
}
