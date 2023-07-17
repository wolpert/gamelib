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

package com.codeheadsystems.gamelib.entity.manager;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import java.util.HashSet;
import java.util.Set;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * The type Engine manager.
 */
@Singleton
public class EngineManager {

    private final Set<EntitySystem> entitySystems = new HashSet<>();
    private final PooledEngine pooledEngine;

  /**
   * Instantiates a new Engine manager.
   *
   * @param entities      the entities
   * @param entitySystems the entity systems
   * @param pooledEngine  the pooled engine
   */
  @Inject
    public EngineManager(final Set<Entity> entities,
                         final Set<EntitySystem> entitySystems,
                         final PooledEngine pooledEngine) {
        this.entitySystems.addAll(entitySystems);
        this.pooledEngine = pooledEngine;
        for (Entity entity : entities) {
            Gdx.app.log("PoolEngineManager", "AddEntity: " + entity + ":" + entity.getComponents());
            pooledEngine.addEntity(entity);
        }
        for (EntitySystem entitySystem : entitySystems) {
            Gdx.app.log("PoolEngineManager", "AddSystem: " + entitySystem);
            pooledEngine.addSystem(entitySystem);
        }
    }

  /**
   * Create entity entity.
   *
   * @return the entity
   */
  public Entity createEntity() {
        return pooledEngine.createEntity();
    }

  /**
   * Create component t.
   *
   * @param <T>           the type parameter
   * @param componentType the component type
   * @return the t
   */
  public <T extends Component> T createComponent(Class<T> componentType) {
        return pooledEngine.createComponent(componentType);
    }

  /**
   * Engine pooled engine.
   *
   * @return the pooled engine
   */
  public PooledEngine engine() {
        return pooledEngine;
    }

  /**
   * Add system.
   *
   * @param system the system
   */
  public void addSystem(EntitySystem system) {
        pooledEngine.addSystem(system);
        entitySystems.add(system);
    }

  /**
   * Remove system.
   *
   * @param system the system
   */
  public void removeSystem(EntitySystem system) {
        pooledEngine.addSystem(system);
        entitySystems.remove(system);
    }

  /**
   * Add entity.
   *
   * @param entity the entity
   */
  public void addEntity(Entity entity) {
        pooledEngine.addEntity(entity);
    }

  /**
   * Remove entity.
   *
   * @param entity the entity
   */
  public void removeEntity(Entity entity) {
        entity.removeAll();
        pooledEngine.removeEntity(entity);
    }

  /**
   * Update.
   *
   * @param delta the delta
   */
  public void update(float delta) {
        pooledEngine.update(delta);
    }

  /**
   * Destroy.
   */
  public void destroy() {
        pooledEngine.removeAllEntities();
        for (EntitySystem entitySystem : entitySystems) {
            Gdx.app.log("PoolEngineManager", "removeSystem: " + entitySystem);
            pooledEngine.removeSystem(entitySystem);
        }
        pooledEngine.removeAllSystems();
        entitySystems.clear();
    }

  /**
   * Gets entity systems.
   *
   * @return the entity systems
   */
  public Set<EntitySystem> getEntitySystems() {
        return entitySystems;
    }

  /**
   * Clear.
   */
  public void clear() {
      pooledEngine.removeAllEntities();
    }
}
