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

package com.codeheadsystems.gamelib.loader.modules;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.utils.Json;
import com.codeheadsystems.gamelib.loader.Infrastructure;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

/**
 * The type Infrastructure module.
 */
@Module
public class InfrastructureModule {

  private final Infrastructure infrastructure;

  /**
   * Instantiates a new Infrastructure module.
   *
   * @param infrastructure the infrastructure
   */
  public InfrastructureModule(final Infrastructure infrastructure) {
    this.infrastructure = infrastructure;
  }

  /**
   * Asset manager asset manager.
   *
   * @return the asset manager
   */
  @Provides
  @Singleton
  public AssetManager assetManager() {
    return infrastructure.getAssetManager();
  }

  /**
   * Json json.
   *
   * @return the json
   */
  @Provides
  @Singleton
  public Json json() {
    return infrastructure.getJson();
  }

  /**
   * File handle resolver file handle resolver.
   *
   * @return the file handle resolver
   */
  @Provides
  @Singleton
  public FileHandleResolver fileHandleResolver() {
    return infrastructure.getFileHandleResolver();
  }
}
