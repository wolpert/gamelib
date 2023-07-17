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

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Json;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Provides a cache of all GDX objects. Usually used to inject GDX
 * object into other dagger instances.
 */
@Singleton
public class GdxRuntimeCache {

  private final Json json;
  private final FileHandleResolver fileHandleResolver;
  private final AssetManager assetManager;
  private final OrthographicCamera orthographicCamera;
  private final ShapeRenderer shapeRenderer;
  private final SpriteBatch spriteBatch;

  /**
   * Instantiates a new Gdx runtime cache.
   *
   * @param json               the json
   * @param fileHandleResolver the file handle resolver
   * @param assetManager       the asset manager
   * @param orthographicCamera the orthographic camera
   * @param shapeRenderer      the shape renderer
   * @param spriteBatch        the sprite batch
   */
  @Inject
  public GdxRuntimeCache(final Json json,
                         final FileHandleResolver fileHandleResolver,
                         final AssetManager assetManager,
                         final OrthographicCamera orthographicCamera,
                         final ShapeRenderer shapeRenderer,
                         final SpriteBatch spriteBatch) {
    this.json = json;
    this.fileHandleResolver = fileHandleResolver;
    this.assetManager = assetManager;
    this.orthographicCamera = orthographicCamera;
    this.shapeRenderer = shapeRenderer;
    this.spriteBatch = spriteBatch;
  }

  /**
   * Gets json.
   *
   * @return the json
   */
  public Json getJson() {
    return json;
  }

  /**
   * Gets file handle resolver.
   *
   * @return the file handle resolver
   */
  public FileHandleResolver getFileHandleResolver() {
    return fileHandleResolver;
  }

  /**
   * Gets asset manager.
   *
   * @return the asset manager
   */
  public AssetManager getAssetManager() {
    return assetManager;
  }

  /**
   * Gets orthographic camera.
   *
   * @return the orthographic camera
   */
  public OrthographicCamera getOrthographicCamera() {
    return orthographicCamera;
  }

  /**
   * Gets shape renderer.
   *
   * @return the shape renderer
   */
  public ShapeRenderer getShapeRenderer() {
    return shapeRenderer;
  }

  /**
   * Gets sprite batch.
   *
   * @return the sprite batch
   */
  public SpriteBatch getSpriteBatch() {
    return spriteBatch;
  }
}
