/*
 * Copyright (c) 2023. Ned Wolpert
 */

package com.codeheadsystems.gamelib.loader.manager;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Logger;
import com.codeheadsystems.gamelib.loader.Infrastructure;
import com.codeheadsystems.gamelib.loader.ScreenProvider;
import com.codeheadsystems.gamelib.loader.model.Assets;
import com.codeheadsystems.gamelib.loader.model.Loader;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * The type Loading manager.
 */
public class LoadingManager {
  private static final String ASSETS_FILE_NAME = "assets.json";
  private static final Logger LOGGER = new Logger(LoadingManager.class.getSimpleName(), Logger.DEBUG);
  private final AssetManager assetManager;
  private final FileHandleResolver fileHandleResolver;
  private final Json json;
  private final Infrastructure gameInfrastructure;
  private Assets assets;

  private LoadingStage loadingStage;

  /**
   * Instantiates a new Loading manager.
   *
   * @param infrastructure the infrastructure
   */
  public LoadingManager(final Infrastructure infrastructure) {
    this.gameInfrastructure = infrastructure;
    this.assetManager = infrastructure.getAssetManager();
    this.fileHandleResolver = infrastructure.getFileHandleResolver();
    this.json = infrastructure.getJson();
    setLoadingStage(LoadingStage.INIT);
  }

  /**
   * Returns screen when its build.
   *
   * @return boolean if we are done updating.
   */
  public Optional<Screen> generate() {
    LOGGER.info("Progress :" + getProgress());
    return switch (loadingStage) {
      case INIT -> init();
      case ASSET_LOADERS -> assetLoaders();
      case QUEUE_ASSETS -> queueAssets();
      case LOAD_ASSETS -> loadAssets();
      case GENERATE_SCREEN -> generateScreen();
      default -> Optional.empty();
    };
  }

  private Optional<Screen> init() {
    setAssets(json.fromJson(
        Assets.class,
        fileHandleResolver.resolve(ASSETS_FILE_NAME)));
    setLoadingStage(LoadingStage.ASSET_LOADERS);
    return Optional.empty();
  }

  private Optional<Screen> assetLoaders() {
    setLoadingStage(LoadingStage.QUEUE_ASSETS);
    if (!assets.getFonts().isEmpty()) {
      assetManager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(fileHandleResolver));
      assetManager.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(fileHandleResolver));
    }
    assets.loaders().forEach(this::buildLoader);
    return Optional.empty();
  }

  private Optional<Screen> queueAssets() {
    assetManagerConsumer(assets.getLoadingScreenConfiguration().getPreQueueAssetsHook())
        .ifPresent(consumer -> consumer.accept(assetManager));
    for (Map.Entry<String, FreetypeFontLoader.FreeTypeFontLoaderParameter> entry : assets.getFonts().entrySet()) {
      LOGGER.info("Queueing font: " + entry.getKey());
      assetManager.load(entry.getKey(), BitmapFont.class, entry.getValue());
    }
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
    assetManagerConsumer(assets.getLoadingScreenConfiguration().getPostQueueAssetsHook())
        .ifPresent(consumer -> consumer.accept(assetManager));
    setLoadingStage(LoadingStage.LOAD_ASSETS);
    return Optional.empty();
  }

  private Optional<Consumer<AssetManager>> assetManagerConsumer(final String name) {
    if (name == null) {
      return Optional.empty();
    }
    try {
      final Class<?> clazz = Class.forName(name);
      final Object instance = clazz.getConstructor().newInstance();
      if (instance instanceof Consumer consumer) {
        return Optional.of((Consumer<AssetManager>) instance);
      } else {
        throw new IllegalStateException("Post queue assets hook must be a consumer");
      }
    } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException |
             InvocationTargetException e) {
      throw new IllegalStateException(e);
    }
  }

  private void handlePostQueueAssetsHook(final String postQueueAssetsHook) {
    try {
      final Class<?> clazz = Class.forName(postQueueAssetsHook);
      final Object instance = clazz.getConstructor().newInstance();
      if (instance instanceof Consumer consumer) {
        consumer.accept(assetManager);
      } else {
        throw new IllegalStateException("Post queue assets hook must be a consumer");
      }
    } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException |
             InvocationTargetException e) {
      throw new IllegalStateException(e);
    }
  }

  private Optional<Screen> loadAssets() {
    if (assetManager.update()) {
      setLoadingStage(LoadingStage.GENERATE_SCREEN);
    }
    return Optional.empty();
  }

  private Optional<Screen> generateScreen() {
    try {
      final ScreenProvider screenProvider = Class.forName(assets.getLoadingScreenConfiguration().getScreenProvider())
          .asSubclass(ScreenProvider.class)
          .getConstructor()
          .newInstance();
      final Screen mainScreen = screenProvider.screen(gameInfrastructure);
      setLoadingStage(LoadingStage.DONE);
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
    return switch (loadingStage) {
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
  void setLoadingStage(final LoadingStage loadingStage) {
    LOGGER.info("stage: " + loadingStage.getTitle());
    this.loadingStage = loadingStage;
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
