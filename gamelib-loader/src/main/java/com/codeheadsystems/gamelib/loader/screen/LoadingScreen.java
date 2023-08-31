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

package com.codeheadsystems.gamelib.loader.screen;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.codeheadsystems.gamelib.loader.GdxGame;
import com.codeheadsystems.gamelib.loader.Infrastructure;
import com.codeheadsystems.gamelib.loader.manager.LoadingManager;

/**
 * Minimal loading screen needed for the base game. Note, he does not use the asset manager... rather
 * via the screen and the loading manager, the asset manager gets set.
 */
public class LoadingScreen extends ScreenAdapter {

  private static final Logger LOGGER = new Logger(LoadingScreen.class.getSimpleName(), Logger.DEBUG);
  private LoadingManager loadingManager;
  private Stage stage;
  private Skin skin;
  private ProgressBar progressBar;
  private Pixmap pixmap;

  /**
   * Instantiates a new Loading screen.
   */
  public LoadingScreen() {
    LOGGER.info("LoadingScreenConfiguration()");
  }

  @Override
  public void render(float delta) {
    loadingManager.generate().ifPresentOrElse(
        screen -> GdxGame.instance().setScreen(screen),
        () -> renderLoadingScreen(delta));
  }

  @Override
  public void show() {
    LOGGER.info("show()");
    super.show();
    Infrastructure infrastructure = GdxGame.instance().getInfrastructure();
    if (this.loadingManager == null) {
      this.loadingManager = new LoadingManager(infrastructure);
    }
    Viewport viewport = new StretchViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    stage = new Stage(viewport, infrastructure.getSpriteBatch());
    stage.setDebugAll(false);
    Gdx.input.setInputProcessor(stage);
    skin = createSkin();
    Table table = new Table();
    table.setFillParent(true);
    progressBar = new ProgressBar(0f, 1f, 0.01f, false, skin);
    table.add(progressBar).size(Gdx.graphics.getWidth());
    stage.addActor(table);
  }

  @Override
  public void hide() {
    LOGGER.info("hide()");
    if (Gdx.input.getInputProcessor() == stage) {
      Gdx.input.setInputProcessor(null);
    }
    pixmap.dispose();
    pixmap = null;
    skin.dispose();
    skin = null;
    stage.dispose();
    stage = null;
    progressBar = null;
    this.loadingManager = null;
  }

  // TODO: Have this set by a loadable file
  private Skin createSkin() {
    final Skin newSkin = new Skin();
    pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
    pixmap.setColor(Color.WHITE);
    pixmap.fill();
    newSkin.add("white", new Texture(pixmap));
    ProgressBar.ProgressBarStyle progressBarStyle = new ProgressBar.ProgressBarStyle();
    progressBarStyle.background = newSkin.newDrawable("white", Color.BLACK);
    progressBarStyle.knob = newSkin.newDrawable("white", Color.ROYAL);
    //progressBarStyle.knob.setMinHeight(400);
    progressBarStyle.knob.setMinWidth(Gdx.graphics.getWidth() * 0.01f);
    progressBarStyle.knob.setMinHeight(Gdx.graphics.getHeight() * 0.05f);
    progressBarStyle.knobBefore = newSkin.newDrawable("white", Color.GREEN);
    progressBarStyle.knobAfter = newSkin.newDrawable("white", Color.RED);
    newSkin.add("default-horizontal", progressBarStyle);
    return newSkin;
  }

  private void renderLoadingScreen(float delta) {
    progressBar.setValue(loadingManager.getProgress());
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    ScreenUtils.clear(0.2f, 0.2f, 0.2f, 1);
    stage.act(delta);
    stage.draw();
  }

}
