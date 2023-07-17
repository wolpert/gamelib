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

package com.codeheadsystems.gamelib.core.dagger;

import static com.codeheadsystems.gamelib.core.util.LoggerHelper.logger;

import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Logger;
import com.codeheadsystems.gamelib.core.model.LoadingConfiguration;
import com.codeheadsystems.gamelib.core.screen.LoadingScreen;
import com.codeheadsystems.gamelib.core.util.GameListener;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoSet;
import javax.inject.Named;
import javax.inject.Singleton;

/**
 * This is the configuration for the loading module. Create an instance of this to build out your
 * game.
 */
@Module
public class LoadingModule {

  /**
   * The constant CONFIGURATION_JSON.
   */
  public static final String CONFIGURATION_JSON = "loadingConfiguration.json";
  /**
   * The constant LOADING_IMAGE.
   */
  public static final String LOADING_IMAGE = "loadingImage";
  /**
   * The constant MAIN_SCREEN.
   */
  public static final String MAIN_SCREEN = "mainScreen";
  /**
   * The constant MAIN_SCREEN_PROVIDER.
   */
  public static final String MAIN_SCREEN_PROVIDER = "mainScreenProvider";
  private static final Logger LOGGER = logger(LoadingModule.class);

  /**
   * Loading image string.
   *
   * @param loadingConfiguration the loading configuration
   * @return the string
   */
  @Named(LOADING_IMAGE)
  @Provides
  @Singleton
  public String loadingImage(final LoadingConfiguration loadingConfiguration) {
    return loadingConfiguration.getLoadingImage();
  }

  /**
   * Loading configuration loading configuration.
   *
   * @param fileHandleResolver the file handle resolver
   * @param json               the json
   * @return the loading configuration
   */
  @Provides
  @Singleton
  public LoadingConfiguration loadingConfiguration(final FileHandleResolver fileHandleResolver,
                                                   final Json json) {
    final FileHandle fileHandle = fileHandleResolver.resolve(CONFIGURATION_JSON);
    return json.fromJson(LoadingConfiguration.class, fileHandle);
  }

  /**
   * Loading screen game listener game listener.
   *
   * @param loadingScreen the loading screen
   * @return the game listener
   */
  @Provides
  @Singleton
  @IntoSet
  public GameListener loadingScreenGameListener(final LoadingScreen loadingScreen) {
    return loadingScreen;
  }
}
