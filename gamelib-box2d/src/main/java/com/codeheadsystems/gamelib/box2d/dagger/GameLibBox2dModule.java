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

package com.codeheadsystems.gamelib.box2d.dagger;

import com.badlogic.ashley.core.EntitySystem;
import com.codeheadsystems.gamelib.box2d.entitysystem.WorldEntitySystem;
import com.codeheadsystems.gamelib.box2d.model.WorldConfiguration;
import dagger.Binds;
import dagger.BindsOptionalOf;
import dagger.Module;
import dagger.multibindings.IntoSet;

/**
 * The type Game lib box 2 d module.
 */
@Module(includes = {GameLibBox2dModule.Binder.class})
public class GameLibBox2dModule {

  /**
   * The interface Binder.
   */
  @Module
  interface Binder {
    /**
     * World configuration world configuration.
     *
     * @return the world configuration
     */
    @BindsOptionalOf
    WorldConfiguration worldConfiguration();

    /**
     * World entity system entity system.
     *
     * @param system the system
     * @return the entity system
     */
    @Binds
    @IntoSet
    EntitySystem worldEntitySystem(WorldEntitySystem system);
  }

}
