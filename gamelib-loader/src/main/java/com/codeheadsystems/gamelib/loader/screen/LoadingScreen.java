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
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Logger;
import com.codeheadsystems.gamelib.loader.GdxGame;
import com.codeheadsystems.gamelib.loader.ScreenProvider;
import com.codeheadsystems.gamelib.loader.model.Assets;
import com.codeheadsystems.gamelib.loader.model.GameInfrastructure;
import com.codeheadsystems.gamelib.loader.model.Loader;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Map;

/**
 * Minimal loading screen needed for the base game. Note, he does not use the asset manager... rather
 * via the screen and the loading manager, the asset manager gets set.
 */
public class LoadingScreen extends ScreenAdapter {

  private static final Logger LOGGER = new Logger(LoadingScreen.class.getSimpleName(), Logger.DEBUG);
  private final GameInfrastructure gameInfrastructure;

  private final AssetManager assetManager;
  private final FileHandleResolver fileHandleResolver;
  private final Json json;
  private Stages currentStage;
  private Assets assets;

  public LoadingScreen(final GameInfrastructure infrastructure) {
    LOGGER.info("LoadingScreen()");
    this.gameInfrastructure = infrastructure;
    this.assetManager = infrastructure.getAssetManager();
    this.fileHandleResolver = infrastructure.getFileHandleResolver();
    this.json = infrastructure.getJson();
    setCurrentStage(Stages.INIT);
  }

  @Override
  public void show() {
  }

  @Override
  public void render(float delta) {
    if (!update()) {
      // render the loading screen
      //loadingBar.render(loadingManager.getProgress());
    }
  }

  /**
   * Returns false if no more updates are needed.
   *
   * @return boolean if we are done updating.
   */
  public boolean update() {
    LOGGER.info("Update() : " + currentStage);
    switch (currentStage) {
      case INIT -> {
        setAssets(json.fromJson(
            Assets.class,
            fileHandleResolver.resolve("assets.json")));
        setCurrentStage(Stages.ASSET_LOADERS);
        return false;
      }
      case ASSET_LOADERS -> {
        setCurrentStage(Stages.QUEUE_ASSETS);
        assetManager.setLoader(TiledMap.class, ".tmx", new TmxMapLoader(fileHandleResolver));
        // Loop through the loaders.
        assets.loaders().forEach(this::buildLoader);
        return false;
      }
      case QUEUE_ASSETS -> {
        for (Map.Entry<String, ArrayList<String>> entry : assets.getAssetsToLoad().entrySet()) {
          final String clazzName = entry.getKey();
          LOGGER.info("Processing: " + clazzName);
          try {
            final Class<?> clazz = Class.forName(clazzName);
            for (String filename : entry.getValue()) {
              LOGGER.info("  adding to queue: " + clazz.getSimpleName() + ":" + filename);
              assetManager.load(filename, clazz);
            }
          } catch (ClassNotFoundException e) {
            throw new IllegalStateException(e);
          }
        }
        setCurrentStage(Stages.LOAD_ASSETS);
        return false;
      }
      case LOAD_ASSETS -> {
        if (assetManager.update()) {
          setCurrentStage(Stages.GENERATE_SCREEN);
        }
        return false;
      }
      case GENERATE_SCREEN -> {
        try {
          final ScreenProvider screenProvider = Class.forName(assets.getScreenProvider())
              .asSubclass(ScreenProvider.class)
              .getConstructor()
              .newInstance();
          final Screen mainScreen = screenProvider.screen(gameInfrastructure);
          GdxGame.instance().setScreen(mainScreen); // This should end this screen being used.
        } catch (Exception e) {
          throw new IllegalStateException(e);
        }
        setCurrentStage(Stages.DONE);
        return false;
      }
      default -> {
        return true;
      }
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
      case ASSET_LOADERS -> 0.1f;
      case QUEUE_ASSETS -> 0.2f;
      case LOAD_ASSETS -> 0.3f + (0.7f * assetManager.getProgress());
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
   * @param currentStage the current stage
   */
  void setCurrentStage(final Stages currentStage) {
    LOGGER.info("Setting stage: " + currentStage + " : " + currentStage.getTitle());
    this.currentStage = currentStage;
  }

  /**
   * The enum Stages.
   */
  enum Stages {
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

    Stages(String title) {
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
