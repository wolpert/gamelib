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

package com.codeheadsystems.gamelib.core.manager;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Purpose: provide a way to load from json the GDX way.
 * Also makes it easier to test.
 * <p>
 * Note, for this to work, we are required that the resulting class be a JavaBean and not immutable.
 */
@Singleton
public class JsonManager {

    private final Json json;

  /**
   * Instantiates a new Json manager.
   *
   * @param json the json
   */
  @Inject
    public JsonManager(final Json json) {
        this.json = json;
    }

  /**
   * Wrappper for the fromJson method to ease testing.
   *
   * @param <T>   the type.
   * @param clazz we are looking for.
   * @param file  file handler.
   * @return an instance of clazz based on the type.
   */
  public <T> T fromJson(final Class<T> clazz,
                          final FileHandle file) {
        return json.fromJson(clazz, file);
    }

  /**
   * From json t.
   *
   * @param <T>        the type parameter
   * @param clazz      the clazz
   * @param jsonString the json string
   * @return the t
   */
  public <T> T fromJson(final Class<T> clazz,
                          final String jsonString) {
        return json.fromJson(clazz, jsonString);
    }
}
