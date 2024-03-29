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

import static com.codeheadsystems.gamelib.core.util.LoggerHelper.logger;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Logger;
import com.codeheadsystems.gamelib.entity.manager.RenderClearManager;
import java.util.Optional;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * This does double-duty, as its also functions in the entity system.
 */
@Singleton
public class CameraEntitySystem extends EntitySystem {
  private static final Logger LOGGER = logger(CameraEntitySystem.class);
  private final OrthographicCamera orthographicCamera;
  private final SpriteBatch spriteBatch;
  private final ShapeRenderer shapeRenderer;
  private final RenderClearManager renderClearManager;

  /**
   * Instantiates a new Camera entity system.
   *
   * @param orthographicCamera the orthographic camera
   * @param spriteBatch        the sprite batch
   * @param shapeRenderer      the shape renderer
   * @param renderClearManager the optional clean manager.
   */
  @Inject
  public CameraEntitySystem(final OrthographicCamera orthographicCamera,
                            final SpriteBatch spriteBatch,
                            final ShapeRenderer shapeRenderer,
                            final Optional<RenderClearManager> renderClearManager) {
    super(Priorities.CAMERA.priority());
    this.orthographicCamera = orthographicCamera;
    this.spriteBatch = spriteBatch;
    this.shapeRenderer = shapeRenderer;
    this.renderClearManager = renderClearManager.orElse(this::defaultCleanScreen);
  }

  @Override
  public void update(float deltaTime) {
    renderClearManager.doRenderClean();
    orthographicCamera.update();
    spriteBatch.setProjectionMatrix(orthographicCamera.combined);
    shapeRenderer.setProjectionMatrix(orthographicCamera.combined);
  }

  private void defaultCleanScreen() {
    Gdx.gl.glClearColor(0, 0, 0.2f, 1);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
  }

}
