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

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Json;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

/**
 * This can be used by other dagger IOC.
 */
@Module
public class GdxRuntimeCacheModule {

  private final GdxRuntimeCache cache;

  public GdxRuntimeCacheModule(final GdxRuntimeCache cache) {
    this.cache = cache;
  }

  @Provides
  @Singleton
  public Json json() {
    return cache.getJson();
  }

  @Provides
  @Singleton
  public FileHandleResolver fileHandleResolver() {
    return cache.getFileHandleResolver();
  }

  @Provides
  @Singleton
  public AssetManager assetManager() {
    return cache.getAssetManager();
  }

  @Provides
  @Singleton
  public OrthographicCamera orthographicCamera() {
    return cache.getOrthographicCamera();
  }

  @Provides
  @Singleton
  public ShapeRenderer shapeRenderer() {
    return cache.getShapeRenderer();
  }

  @Provides
  @Singleton
  public SpriteBatch spriteBatch() {
    return cache.getSpriteBatch();
  }

}
