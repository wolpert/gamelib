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

package com.codeheadsystems.gamelib.entity.entitysystem;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.SortedIteratingSystem;
import java.util.Comparator;

/**
 * Purpose: Work just like the SortedIteratingSystem, except we have the before/after methods.
 */
abstract public class WrapperSortedIteratingSystem extends SortedIteratingSystem {
  /**
   * Instantiates a new Wrapper sorted iterating system.
   *
   * @param family     the family
   * @param comparator the comparator
   */
  public WrapperSortedIteratingSystem(Family family, Comparator<Entity> comparator) {
    super(family, comparator);
  }

  /**
   * Instantiates a new Wrapper sorted iterating system.
   *
   * @param family     the family
   * @param comparator the comparator
   * @param priority   the priority
   */
  public WrapperSortedIteratingSystem(Family family, Comparator<Entity> comparator, int priority) {
    super(family, comparator, priority);
  }

  @Override
  public void update(float deltaTime) {
    beforeIteration(deltaTime);
    super.update(deltaTime);
    afterIteration(deltaTime);
  }

  /**
   * Override this if you need something to execute before the start of the
   * batch.
   *
   * @param deltaTime time since last execution.
   */
  public void beforeIteration(final float deltaTime) {

  }

  /**
   * Override this if you want to handle the end of the entity.
   *
   * @param deltaTime time since last execution.
   */
  public void afterIteration(final float deltaTime) {

  }

}
