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

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.codeheadsystems.gamelib.core.GameLauncher;
import dagger.Module;
import dagger.Provides;
import java.util.Optional;
import javax.inject.Singleton;

/**
 * Use this module to load all the gamelib-core features for a desktop application.
 */
@Module(includes = {GameResources.class, GdxModule.class, LoadingModule.class})
public class GameLibModule {

  @Provides
  @Singleton
  public Lwjgl3Application lwjgl3Application(final Optional<Lwjgl3ApplicationConfiguration> configuration,
                                             final GameLauncher gameLauncher) {
    return new Lwjgl3Application(gameLauncher, configuration.orElseGet(Lwjgl3ApplicationConfiguration::new));
  }

}
