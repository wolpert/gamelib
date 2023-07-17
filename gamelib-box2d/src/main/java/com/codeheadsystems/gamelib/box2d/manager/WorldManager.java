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

package com.codeheadsystems.gamelib.box2d.manager;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.codeheadsystems.gamelib.box2d.model.WorldConfiguration;
import java.util.Optional;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * The type World manager.
 */
@Singleton
public class WorldManager {

  private final World world;
  private final float maxFrameTime;
  private final float stepTime;
  private final int velocityIterations;
  private final int positionIterations;
  private final Optional<Runnable> debug;

  private float timeSince = 0; // used for step counting.

  /**
   * Instantiates a new World manager.
   *
   * @param worldConfiguration the world configuration
   * @param camera             the camera
   */
  @Inject
  public WorldManager(final Optional<WorldConfiguration> worldConfiguration,
                      final OrthographicCamera camera) {
    final WorldConfiguration config = worldConfiguration.orElseGet(WorldConfiguration::new);
    world = new World(config.gravity, config.doSleep);
    this.stepTime = config.stepTime;
    this.maxFrameTime = config.maxFrameTime;
    this.velocityIterations = config.velocityIterations;
    this.positionIterations = config.positionIterations;
    if (config.debug) {
      final Box2DDebugRenderer renderer = new Box2DDebugRenderer();
      debug = Optional.of(() -> renderer.render(world, camera.combined));
    } else {
      debug = Optional.empty();
    }
  }

  /**
   * Last step in the render workflow according to LIBGDX docs.
   *
   * @param delta the delta
   */
  public void step(float delta) {
    debug.ifPresent(Runnable::run);
    float frameTime = Math.min(delta, maxFrameTime);
    timeSince += frameTime;
    while (timeSince >= stepTime) {
      world.step(stepTime, velocityIterations, positionIterations);
      timeSince -= stepTime;
    }
  }

  /**
   * World world.
   *
   * @return the world
   */
  public World world() {
    return world;
  }
}
