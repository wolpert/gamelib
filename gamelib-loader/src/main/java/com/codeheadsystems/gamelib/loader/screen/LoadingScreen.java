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
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.codeheadsystems.gamelib.loader.GdxGame;
import com.codeheadsystems.gamelib.loader.Infrastructure;
import com.codeheadsystems.gamelib.loader.ScreenProvider;
import com.codeheadsystems.gamelib.loader.model.Assets;
import com.codeheadsystems.gamelib.loader.model.Loader;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

/**
 * Minimal loading screen needed for the base game. Note, he does not use the asset manager... rather
 * via the screen and the loading manager, the asset manager gets set.
 */
public class LoadingScreen extends ScreenAdapter {

  private static final Logger LOGGER = new Logger(LoadingScreen.class.getSimpleName(), Logger.DEBUG);
  private static final String ASSETS_FILE_NAME = "assets.json";
  private final Infrastructure gameInfrastructure;
  private final AssetManager assetManager;
  private final FileHandleResolver fileHandleResolver;
  private final Json json;
  private final SpriteBatch spriteBatch;
  private LoadingStage currentStage;
  private Viewport viewport;
  private Assets assets;
  private Stage stage;
  private Skin skin;
  private ProgressBar progressBar;
  private Pixmap pixmap;

  /**
   * Instantiates a new Loading screen.
   *
   * @param infrastructure the infrastructure
   */
  public LoadingScreen(final Infrastructure infrastructure) {
    LOGGER.info("LoadingScreen()");
    this.gameInfrastructure = infrastructure;
    this.assetManager = infrastructure.getAssetManager();
    this.fileHandleResolver = infrastructure.getFileHandleResolver();
    this.json = infrastructure.getJson();
    this.spriteBatch = infrastructure.getSpriteBatch();
    setCurrentStage(LoadingStage.INIT);
  }

  @Override
  public void render(float delta) {
    generate().ifPresentOrElse(
        screen -> GdxGame.instance().setScreen(screen),
        () -> renderLoadingScreen(delta));
  }

  @Override
  public void show() {
    LOGGER.info("show()");
    super.show();
    viewport = new StretchViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    stage = new Stage(viewport, spriteBatch);
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
    viewport = null;
    progressBar = null;
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
    progressBar.setValue(getProgress());
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    ScreenUtils.clear(0.2f, 0.2f, 0.2f, 1);
    stage.act(delta);
    stage.draw();
  }

  /**
   * Returns screen when its build.
   *
   * @return boolean if we are done updating.
   */
  private Optional<Screen> generate() {
    LOGGER.info("Progress :" + getProgress());
    return switch (currentStage) {
      case INIT -> processInit();
      case ASSET_LOADERS -> processAssetLoaders();
      case QUEUE_ASSETS -> processQueueAssets();
      case LOAD_ASSETS -> processLoadAssets();
      case GENERATE_SCREEN -> processGenerateScreen();
      default -> Optional.empty();
    };
  }

  private Optional<Screen> processInit() {
    setAssets(json.fromJson(
        Assets.class,
        fileHandleResolver.resolve(ASSETS_FILE_NAME)));
    setCurrentStage(LoadingStage.ASSET_LOADERS);
    return Optional.empty();
  }

  private Optional<Screen> processAssetLoaders() {
    setCurrentStage(LoadingStage.QUEUE_ASSETS);
    assets.loaders().forEach(this::buildLoader);
    return Optional.empty();
  }

  private Optional<Screen> processQueueAssets() {
    for (Map.Entry<String, ArrayList<String>> entry : assets.getAssetsToLoad().entrySet()) {
      final String clazzName = entry.getKey();
      try {
        final Class<?> clazz = Class.forName(clazzName);
        for (String filename : entry.getValue()) {
          LOGGER.info("Queueing " + clazz.getSimpleName() + ":" + filename);
          assetManager.load(filename, clazz);
        }
      } catch (ClassNotFoundException e) {
        throw new IllegalStateException(e);
      }
    }
    setCurrentStage(LoadingStage.LOAD_ASSETS);
    return Optional.empty();
  }

  private Optional<Screen> processLoadAssets() {
    if (assetManager.update()) {
      setCurrentStage(LoadingStage.GENERATE_SCREEN);
    }
    return Optional.empty();
  }

  private Optional<Screen> processGenerateScreen() {
    try {
      final ScreenProvider screenProvider = Class.forName(assets.getScreenProvider())
          .asSubclass(ScreenProvider.class)
          .getConstructor()
          .newInstance();
      final Screen mainScreen = screenProvider.screen(gameInfrastructure);
      setCurrentStage(LoadingStage.DONE);
      return Optional.of(mainScreen);
    } catch (Exception e) {
      throw new IllegalStateException(e);
    }
  }

  private <T> void buildLoader(final Loader l) {
    try {
      LOGGER.info("Asset class to load: " + l.classToLoad());
      final Class<T> classToLoad = getParemeterizedClass(l.classToLoad());
      LOGGER.info("    Loading class for the asset: " + l.loaderClass());
      final Class<AsynchronousAssetLoader<T, AssetLoaderParameters<T>>> loaderClazz = getParemeterizedClass(l.loaderClass());
      final AsynchronousAssetLoader<T, AssetLoaderParameters<T>> instance = loaderClazz.getConstructor(FileHandleResolver.class).newInstance(fileHandleResolver);
      assetManager.setLoader(classToLoad, l.suffix(), instance);
    } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | InstantiationException |
             IllegalAccessException e) {
      LOGGER.error("Failed to add loader: " + l.classToLoad() + ":" + l.loaderClass(), e);
      throw new IllegalArgumentException(e);
    }
  }

  /**
   * Isolate the evil here. Needs to be a better way to get the class as a generalized class.
   *
   * @param classToLoad what we are loading.
   * @param <T>         whatever the type is supposed to be.
   * @return the class.
   * @throws ClassNotFoundException Which could also be due to the type not matching.
   */
  @SuppressWarnings("unchecked")
  private <T> Class<T> getParemeterizedClass(final String classToLoad) throws ClassNotFoundException {
    return (Class<T>) Class.forName(classToLoad);
  }

  /**
   * Returns the current progress as float.
   *
   * @return progress bar.
   */
  public float getProgress() {
    return switch (currentStage) {
      case INIT -> 0f;
      case ASSET_LOADERS -> 0.03f;
      case QUEUE_ASSETS -> 0.06f;
      case LOAD_ASSETS -> 0.19f + (0.7f * assetManager.getProgress());
      case GENERATE_SCREEN -> 0.99f;
      default -> 1f;
    };
  }

  /**
   * Sets assets.
   *
   * @param assets the assets
   */
  public void setAssets(final Assets assets) {
    this.assets = assets;
  }

  /**
   * Sets current stage.
   *
   * @param loadingStage the current stage
   */
  void setCurrentStage(final LoadingStage loadingStage) {
    LOGGER.info("stage: " + loadingStage.getTitle());
    this.currentStage = loadingStage;
  }

  /**
   * The enum LoadingStage.
   */
  enum LoadingStage {
    /**
     * The Init.
     */
    INIT("Initializing system"),
    /**
     * The Asset loaders.
     */
    ASSET_LOADERS("Setting up asset loaders"),
    /**
     * The Queue assets.
     */
    QUEUE_ASSETS("Queueing assets"),
    /**
     * The Load assets.
     */
    LOAD_ASSETS("Loading assets from system"),
    /**
     * We load the screen from the defined screen provider
     */
    GENERATE_SCREEN("Generating screen"),
    /**
     * Done stages.
     */
    DONE("Complete!");

    private final String title;

    LoadingStage(String title) {
      this.title = title;
    }

    /**
     * Gets title.
     *
     * @return the title
     */
    public String getTitle() {
      return title;
    }
  }


}
