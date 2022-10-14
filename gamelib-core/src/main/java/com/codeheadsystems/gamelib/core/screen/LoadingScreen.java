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

package com.codeheadsystems.gamelib.core.screen;

import static com.codeheadsystems.gamelib.core.dagger.LoadingModule.LOADING_IMAGE;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.codeheadsystems.gamelib.core.manager.LoadingBar;
import com.codeheadsystems.gamelib.core.manager.LoadingManager;
import com.codeheadsystems.gamelib.core.manager.MainScreenManager;
import com.codeheadsystems.gamelib.core.manager.ResourcePathManager;
import com.codeheadsystems.gamelib.core.util.GameListener;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

/**
 * Minimal loading screen needed for the base game. Note, he does not use the asset manager... rather
 * via the screen and the loading manager, the asset manager gets set.
 */
@Singleton
public class LoadingScreen implements Screen, GameListener {

  private final SpriteBatch spriteBatch;
  private final LoadingManager loadingManager;
  private final LoadingBar loadingBar;
  private final MainScreenManager mainScreenManager;
  private final String loadingImage;

  private Game game;
  private Texture loadingTexture;
  private Sprite loadingSprite;

  private boolean init = false;

  @Inject
  public LoadingScreen(final ResourcePathManager resourcePathManager,
                       final SpriteBatch spriteBatch,
                       final LoadingManager loadingManager,
                       final LoadingBar loadingBar,
                       @Named(LOADING_IMAGE) final String loadingImage,
                       final MainScreenManager mainScreenManager) {
    this.spriteBatch = spriteBatch;
    this.loadingManager = loadingManager;
    this.loadingBar = loadingBar;
    this.loadingImage = resourcePathManager.path(loadingImage);
    this.mainScreenManager = mainScreenManager;
  }

  public Texture getLoadingTexture() {
    return loadingTexture;
  }

  public void setLoadingTexture(final Texture loadingTexture) {
    this.loadingTexture = loadingTexture;
  }

  public Sprite getLoadingSprite() {
    return loadingSprite;
  }

  public void setLoadingSprite(final Sprite loadingSprite) {
    this.loadingSprite = loadingSprite;
  }

  @Override
  public void setGame(final Game game) {
    this.game = game;
  }

  @Override
  public void show() {
    setLoadingTexture(new Texture(loadingImage));
    setLoadingSprite(new Sprite(getLoadingTexture()));
    resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    // TODO: Show loading screen and prep the progress bar.
    loadingBar.show(Gdx.graphics);
  }

  @Override
  public void render(float delta) {
    // Draw Loading screen
    Gdx.gl.glClearColor(0, 0, 0, 1);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    spriteBatch.begin();
    loadingSprite.draw(spriteBatch);
    spriteBatch.end();
    Gdx.app.log(getClass().getSimpleName(), loadingManager.getStageTitle() + ":" + loadingManager.getProgress());
    if (loadingManager.update()) {
      if (!init) {
        final Screen mainScreen = mainScreenManager.mainScreen();
        Gdx.app.log("LoadingScreen", "Showing: " + mainScreen);
        game.setScreen(mainScreen);
        init = true;
      }
    } else {
      loadingBar.render(loadingManager.getProgress());
    }
  }

  @Override
  public void resize(int width, int height) {
    if (loadingSprite != null) {
      loadingSprite.setCenter(width / 2f, height / 2f);
      // horizontal
      loadingSprite.setSize(width, height);
    }

  }

  @Override
  public void pause() {

  }

  @Override
  public void resume() {

  }

  @Override
  public void hide() {
    this.dispose();
  }

  @Override
  public void dispose() {
    // sprite batch should not be destroyed, but the texture can be.
    loadingTexture.dispose();
    loadingSprite = null;
    loadingTexture = null;
  }
}
