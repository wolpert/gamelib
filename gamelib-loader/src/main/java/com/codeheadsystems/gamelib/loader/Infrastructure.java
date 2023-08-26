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

package com.codeheadsystems.gamelib.loader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Logger;

/**
 * The type Game infrastructure.
 */
public class Infrastructure {

  private final AssetManager assetManager;
  private final Json json;
  private final FileHandleResolver fileHandleResolver;
  private final SpriteBatch spriteBatch;

  private Infrastructure(final AssetManager assetManager,
                         final Json json,
                         final FileHandleResolver fileHandleResolver,
                         final SpriteBatch spriteBatch) {
    this.assetManager = assetManager;
    this.json = json;
    this.fileHandleResolver = fileHandleResolver;
    this.spriteBatch = spriteBatch;
  }

  /**
   * Build game infrastructure.
   *
   * @return the game infrastructure
   */
  public static Infrastructure build() {
    return build(
        new AssetManager(),
        new Json(),
        new DefaultFileHandleResolver(),
        new SpriteBatch());
  }

  /**
   * Build infrastructure.
   *
   * @param assetManager       the asset manager
   * @param json               the json
   * @param fileHandleResolver the file handle resolver
   * @param spriteBatch        the sprite batch
   * @return the infrastructure
   */
  public static Infrastructure build(final AssetManager assetManager,
                                     final Json json,
                                     final FileHandleResolver fileHandleResolver,
                                     final SpriteBatch spriteBatch) {
    return new Infrastructure(
        assetManager,
        json,
        fileHandleResolver,
        spriteBatch);
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
   * Gets sprite batch.
   *
   * @return the sprite batch
   */
  public SpriteBatch getSpriteBatch() {
    return spriteBatch;
  }

  /**
   * The type Default file handle resolver.
   */
  public static class DefaultFileHandleResolver implements FileHandleResolver {

    private static final Logger LOGGER = new Logger(Infrastructure.class.getSimpleName(), Logger.DEBUG);

    @Override
    public FileHandle resolve(final String fileName) {
      LOGGER.info("Resolving: " + fileName);
      return Gdx.files.internal(fileName);
    }
  }
}
