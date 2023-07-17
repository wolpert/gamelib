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

package com.codeheadsystems.gamelib.core.loader;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

/**
 * User: wolpert
 * Date: 11/28/15
 * Time: 11:37 AM
 */
public class JsonValueLoader extends AsynchronousAssetLoader<JsonValue, JsonValueLoader.JsonValueLoaderParameter> {

  private JsonReader jsonReader = new JsonReader();

  /**
   * Instantiates a new Json value loader.
   *
   * @param resolver the resolver
   */
  public JsonValueLoader(final FileHandleResolver resolver) {
    super(resolver);
  }

  @Override
  public void loadAsync(final AssetManager manager, final String fileName, final FileHandle file, final JsonValueLoaderParameter parameter) {
  }

  @Override
  public JsonValue loadSync(final AssetManager manager, final String fileName, final FileHandle file, final JsonValueLoaderParameter parameter) {
    return jsonReader.parse(file);
  }

  @Override
  public Array<AssetDescriptor> getDependencies(final String fileName, final FileHandle file, final JsonValueLoaderParameter parameter) {
    return null;
  }

  /**
   * The type Json value loader parameter.
   */
  public static class JsonValueLoaderParameter extends AssetLoaderParameters<JsonValue> {
  }
}
