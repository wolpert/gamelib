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

package com.codeheadsystems.gamelib.hex.entitysystem;

import static com.codeheadsystems.gamelib.core.util.LoggerHelper.logger;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Logger;
import com.codeheadsystems.gamelib.entity.entitysystem.Priorities;
import com.codeheadsystems.gamelib.hex.component.HexComponent;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * The type Hex shape renderer.
 */
@Singleton
public class HexShapeRenderer extends IteratingSystem {

  private static final Logger LOGGER = logger(HexShapeRenderer.class);
  private final ComponentMapper<HexComponent> hm = ComponentMapper.getFor(HexComponent.class);
  private final ShapeRenderer shapeRenderer;

  /**
   * Instantiates a new Hex shape renderer.
   *
   * @param shapeRenderer the shape renderer
   */
  @Inject
  public HexShapeRenderer(final ShapeRenderer shapeRenderer) {
    super(Family.all(HexComponent.class).get(), Priorities.BACKGROUND_LAYER1.priority());
    this.shapeRenderer = shapeRenderer;
    LOGGER.debug("HexShapeRenderer()");
  }

  @Override
  public void update(float deltaTime) {
    beforeIteration(deltaTime);
    super.update(deltaTime);
    afterIteration(deltaTime);
  }

  @Override
  protected void processEntity(final Entity entity,
                               final float deltaTime) {
    shapeRenderer.polygon(hm.get(entity).vertices());
  }

  /**
   * Override this if you need something to execute before the start of the
   * batch.
   *
   * @param deltaTime time since last execution.
   */
  public void beforeIteration(final float deltaTime) {
    shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
  }

  /**
   * Override this if you want to handle the end of the entity.
   *
   * @param deltaTime time since last execution.
   */
  public void afterIteration(final float deltaTime) {
    shapeRenderer.end();
  }
}
