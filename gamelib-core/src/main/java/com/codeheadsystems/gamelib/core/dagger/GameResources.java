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

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import dagger.BindsOptionalOf;
import dagger.Module;
import javax.inject.Named;

/**
 * These can be set by an application's module in order to override existing features.
 */
@Module
public interface GameResources {

  String VIEWPORT_WIDTH = "viewportWidth";
  String RESOURCE_PATH = "resourcePath";

  /**
   * Optional top path for your configuration files.
   */
  @BindsOptionalOf
  @Named(RESOURCE_PATH)
  String resourcePath();

  /**
   * If you have a configuration, you can set it. Else, we use an empty one.
   */
  @BindsOptionalOf
  Lwjgl3ApplicationConfiguration lwjgl3ApplicationConfiguration();

  /**
   * How wide you want your screen for the camera. Note, we calculate the height based on the
   * real height of the screen.
   */
  @BindsOptionalOf
  @Named(VIEWPORT_WIDTH)
  Float viewportWidth();

}
