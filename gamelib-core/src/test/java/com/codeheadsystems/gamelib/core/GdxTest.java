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

package com.codeheadsystems.gamelib.core;

import com.badlogic.gdx.*;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class GdxTest {

  @Mock protected Application app;
  @Mock protected Files files;
  @Mock protected Graphics graphics;
  @Mock protected GL20 gl20;

  @BeforeAll
  static void setupGdxHeadless() {
    HeadlessApplicationConfiguration conf = new HeadlessApplicationConfiguration();
    new HeadlessApplication(new ApplicationAdapter() {
      @Override public void create() {
        super.create();
      }

      @Override public void resize(int width, int height) {
        super.resize(width, height);
      }

      @Override public void render() {
        super.render();
      }

      @Override public void pause() {
        super.pause();
      }

      @Override public void resume() {
        super.resume();
      }

      @Override public void dispose() {
        super.dispose();
      }
    }, conf);
  }

  @BeforeEach
  void setUpGdx() {
    Gdx.app = app;
    Gdx.files = files;
    Gdx.graphics = graphics;
    Gdx.gl = gl20;
    Gdx.gl20 = gl20;
  }
}
