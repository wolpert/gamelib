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

package com.codeheadsystems.gamelib.core.manager;

import static com.codeheadsystems.gamelib.core.dagger.LoadingModule.MAIN_SCREEN;
import static com.codeheadsystems.gamelib.core.dagger.LoadingModule.MAIN_SCREEN_PROVIDER;
import static com.codeheadsystems.gamelib.core.util.LoggerHelper.logger;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.Logger;
import com.codeheadsystems.gamelib.core.dagger.GdxRuntimeCache;
import java.util.Optional;
import java.util.function.Function;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

/**
 * The type Main screen manager.
 */
@Singleton
public class MainScreenManager {

  private static final Logger LOGGER = logger(MainScreenManager.class);

  private final Optional<Screen> optionalScreen;
  private final Optional<Function<GdxRuntimeCache, Screen>> optionalScreenProvider;
  private final GdxRuntimeCache gdxRuntimeCache;

  /**
   * Instantiates a new Main screen manager.
   *
   * @param optionalScreen         the optional screen
   * @param optionalScreenProvider the optional screen provider
   * @param gdxRuntimeCache        the gdx runtime cache
   */
  @Inject
  public MainScreenManager(@Named(MAIN_SCREEN) final Optional<Screen> optionalScreen,
                           @Named(MAIN_SCREEN_PROVIDER) final Optional<Function<GdxRuntimeCache, Screen>> optionalScreenProvider,
                           final GdxRuntimeCache gdxRuntimeCache) {
    this.optionalScreen = optionalScreen;
    this.optionalScreenProvider = optionalScreenProvider;
    this.gdxRuntimeCache = gdxRuntimeCache;
  }

  /**
   * Main screen screen.
   *
   * @return the screen
   */
  public Screen mainScreen() {
    if (optionalScreen.isPresent() && optionalScreenProvider.isPresent()) {
      LOGGER.info("WARNING: you defined both a MAIN_SCREEN and a MAIN_SCREEN_PROVIDER. Will default to MAIN_SCREEN");
    }
    return optionalScreen
        .orElseGet(
            () -> optionalScreenProvider.orElseThrow(
                    () -> new IllegalStateException("You need to either define a MAIN_SCREEN or a MAIN_SCREEN_PROVIDER!"))
                .apply(gdxRuntimeCache));
  }

}
