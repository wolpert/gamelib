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

package com.codeheadsystems.gamelib.box2d.entitysystem;

import com.badlogic.ashley.core.EntitySystem;
import com.codeheadsystems.gamelib.box2d.manager.WorldManager;
import com.codeheadsystems.gamelib.entity.entitysystem.Priorities;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class WorldEntitySystem extends EntitySystem {

  private final WorldManager worldManager;

  @Inject
  public WorldEntitySystem(final WorldManager worldManager) {
    super(Priorities.PHYSICS.priority());
    this.worldManager = worldManager;
  }

  @Override
  public void update(final float deltaTime) {
    super.update(deltaTime);
    worldManager.step(deltaTime);
  }
}
