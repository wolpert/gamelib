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

package com.codeheadsystems.terrapin.sample;

import static com.codeheadsystems.gamelib.core.dagger.LoadingModule.MAIN_SCREEN;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import dagger.Binds;
import dagger.Module;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

@Singleton
public class SampleScreen extends ScreenAdapter {

  private final SpriteBatch batch;
  private final AssetManager assetManager;

  private Texture img;

  @Inject
  public SampleScreen(final SpriteBatch batch,
                      final AssetManager assetManager) {
    this.batch = batch;
    this.assetManager = assetManager;
  }

  @Override
  public void show() {
    // This texture is only disposed when the asset manager is disposed.
    img = assetManager.get("badlogic.jpg", Texture.class);
  }

  @Override
  public void render(final float delta) {
    Gdx.gl.glClearColor(1, 0, 0, 1);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    batch.begin();
    batch.draw(img, 0, 0);
    batch.end();
  }

  /**
   * Placed the module here to stop code bloat for the default sample. Typically, you would
   * have it in a separate file.
   */
  @Module
  public interface SampleModule {

    @Binds
    @Named(MAIN_SCREEN)
    Screen mainScreen(SampleScreen impl);
  }
}
