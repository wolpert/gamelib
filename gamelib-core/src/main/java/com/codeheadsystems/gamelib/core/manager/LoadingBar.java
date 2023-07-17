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

package com.codeheadsystems.gamelib.core.manager;

import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * The type Loading bar.
 */
@Singleton
public class LoadingBar {

  private final ShapeRenderer shapeRenderer;
  private float startX;
  private float startY;
  private float width;
  private float height;

  /**
   * Instantiates a new Loading bar.
   *
   * @param shapeRenderer the shape renderer
   */
  @Inject
  public LoadingBar(final ShapeRenderer shapeRenderer) {
    this.shapeRenderer = shapeRenderer;
  }

  /**
   * Needs the graphics library to calculate current sizing.
   *
   * @param graphics from Gdx instance.
   */
  public void show(final Graphics graphics) {
    final int gameWidth = graphics.getWidth();
    final int gameHeight = graphics.getHeight();
    startX = 0f;
    startY = gameHeight * 0.1f; // start 10% off the ground
    width = gameWidth;
    height = gameHeight * 0.1f; // height is 10% of the screen;
  }

  /**
   * Call this to render a bar where the width represents the fraction loaded.
   *
   * @param fractionLoaded a number between 0 and 1.
   */
  public void render(final float fractionLoaded) {
    shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
    shapeRenderer.setColor(Color.RED);
    shapeRenderer.rect(startX, startY, width, height); // draw background
    shapeRenderer.setColor(Color.BLUE);
    shapeRenderer.rect(startX, startY, width * fractionLoaded, height);
    shapeRenderer.end();
  }

}
