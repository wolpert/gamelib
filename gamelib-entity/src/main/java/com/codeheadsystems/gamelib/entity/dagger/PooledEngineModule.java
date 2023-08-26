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

package com.codeheadsystems.gamelib.entity.dagger;

import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.codeheadsystems.gamelib.entity.configuration.AshleyGameConfiguration;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

/**
 * Purpose: Provides entity systems.
 */
@Module
public class PooledEngineModule {

  /**
   * The constant CONFIGURATION_JSON.
   */
  public static final String CONFIGURATION_JSON = "ashleyGameConfiguration.json";

  /**
   * Pooled engine pooled engine.
   *
   * @param configuration the configuration
   * @return the pooled engine
   */
  @Provides
  @Singleton
  public PooledEngine pooledEngine(final AshleyGameConfiguration configuration) {
    final PooledEngine pooledEngine = new PooledEngine(
        configuration.entityPoolInitialSize,
        configuration.entityPoolMaxSize,
        configuration.componentPoolInitialSize,
        configuration.componentPoolMaxSize);
    return pooledEngine;
  }

  /**
   * Ashley game configuration ashley game configuration.
   *
   * @param fileHandleResolver the file handle resolver
   * @param json               the json
   * @return the ashley game configuration
   */
  @Provides
  @Singleton
  public AshleyGameConfiguration ashleyGameConfiguration(final FileHandleResolver fileHandleResolver,
                                                         final Json json) {
    final FileHandle fileHandle = fileHandleResolver.resolve(CONFIGURATION_JSON);
    return json.fromJson(AshleyGameConfiguration.class, fileHandle);
  }

}
