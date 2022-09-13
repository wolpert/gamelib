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

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Purpose:
 */
@ExtendWith(MockitoExtension.class)
class LoadingBarTest {

  private static final int WIDTH = 100;
  private static final int HEIGHT = 200;

  @Mock
  private Graphics graphics;
  @Mock
  private ShapeRenderer shapeRenderer;

  private LoadingBar loadingBar;

  @BeforeEach
  void setUp() {
    loadingBar = new LoadingBar(shapeRenderer);
  }

  @Test
  public void defaultTest() {
    when(graphics.getHeight()).thenReturn(HEIGHT);
    when(graphics.getWidth()).thenReturn(WIDTH);

    loadingBar.show(graphics);
    loadingBar.render(0.50f);

    verify(shapeRenderer).begin(ShapeRenderer.ShapeType.Filled);
    verify(shapeRenderer).setColor(Color.RED);
    verify(shapeRenderer).rect(0f, 20f, WIDTH, 20f);
    verify(shapeRenderer).setColor(Color.BLUE);
    verify(shapeRenderer).rect(0f, 20f, 50f, 20f);
    verify(shapeRenderer).end();

  }
}