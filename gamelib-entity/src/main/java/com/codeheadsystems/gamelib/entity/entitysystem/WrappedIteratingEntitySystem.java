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

package com.codeheadsystems.gamelib.entity.entitysystem;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.utils.Array;

/**
 * Purpose: Given the family, manages the list of entities in the engine,
 * always keeping up to date. Note, the SortedIteratingSystem has flaws with
 * adding entities, so we basically re-wrote it here, using the SortedIteratingSystem
 * as a guide.
 */
public abstract class WrappedIteratingEntitySystem extends EntitySystem {

  private final Family family;
  private Engine engine;

  /**
   * Instantiates a new Wrapped iterating entity system.
   *
   * @param family   the family
   * @param priority the priority
   */
  public WrappedIteratingEntitySystem(final Family family,
                                      final int priority) {
    super(priority);
    this.family = family;
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
   * Override this if you want to handle the individual entity.
   *
   * @param entity    to process.
   * @param deltaTime time since last execution.
   */
  public void processEntity(final Entity entity, final float deltaTime) {

  }

  /**
   * Override this if you want to handle the end of the entity.
   *
   * @param deltaTime time since last execution.
   */
  public void afterIteration(final float deltaTime) {

  }

  @Override
  public void update(float deltaTime) {
    beforeIteration(deltaTime);
    if (engine != null) {
      for (Entity entity : engine.getEntitiesFor(family)) {
        processEntity(entity, deltaTime);
      }
    }
    afterIteration(deltaTime);
  }

  @Override
  public void addedToEngine(Engine thisEngine) {
    this.engine = thisEngine;
  }

  @Override
  public void removedFromEngine(Engine engine) {
    this.engine = null;
  }
}
