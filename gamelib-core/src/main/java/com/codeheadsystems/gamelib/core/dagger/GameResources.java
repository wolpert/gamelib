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

package com.codeheadsystems.gamelib.core.dagger;

import static com.codeheadsystems.gamelib.core.dagger.LoadingModule.MAIN_SCREEN;
import static com.codeheadsystems.gamelib.core.dagger.LoadingModule.MAIN_SCREEN_PROVIDER;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.Disposable;
import com.codeheadsystems.gamelib.core.manager.ResizeManager;
import dagger.BindsOptionalOf;
import dagger.Module;
import dagger.multibindings.Multibinds;
import java.util.Set;
import java.util.function.Function;
import javax.inject.Named;

/**
 * These can be set by an application's module in order to override existing features.
 */
@Module
public interface GameResources {

  /**
   * The constant VIEWPORT_WIDTH.
   */
  String VIEWPORT_WIDTH = "viewportWidth";
  /**
   * The constant RESOURCE_PATH.
   */
  String RESOURCE_PATH = "resourcePath";

  /**
   * Optional top path for your configuration files.
   *
   * @return the string
   */
  @BindsOptionalOf
  @Named(RESOURCE_PATH)
  String resourcePath();

  /**
   * How wide you want your screen for the camera. Note, we calculate the height based on the
   * real height of the screen.
   *
   * @return the float
   */
  @BindsOptionalOf
  @Named(VIEWPORT_WIDTH)
  Float viewportWidth();

  /**
   * If you use the MAIN_SCREEN approach, we will just call that with the existing dagger injection.
   *
   * @return the screen
   */
  @BindsOptionalOf
  @Named(MAIN_SCREEN)
  Screen mainScreen();

  /**
   * Use this if you want to setup your own injected framework for the main screen.
   *
   * @return optional function for the main screen.
   */
  @BindsOptionalOf
  @Named(MAIN_SCREEN_PROVIDER)
  Function<GdxRuntimeCache, Screen> mainScreenProvider();

  /**
   * If you want to add your own disposables, you can do that here.
   *
   * @return set of disposables.
   */
  @Multibinds
  Set<Disposable> disposables();

  /**
   * The set of resize listeners.
   *
   * @return the set.
   */
  @Multibinds
  Set<ResizeManager.Listener> resizeListeners();
}
