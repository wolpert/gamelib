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

package com.codeheadsystems.gamelib.net.manager;

import com.codeheadsystems.gamelib.net.exception.JsonException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class JsonManager {

  private final ObjectMapper objectMapper;

  @Inject
  public JsonManager(final ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }


  public <T> String toJson(final T object) {
    return toJson(object, false);
  }


  public <T> String toJson(final T object,
                           final boolean withNewLine) {
    try {
      final String result = objectMapper.writeValueAsString(object);
      return (withNewLine ? result + "\n" : result);
    } catch (JsonProcessingException e) {
      throw new JsonException("Unable to convert to json: " + object, e) {
      };
    }
  }

  public <T> T fromJson(final String json, final Class<T> clazz) {
    try {
      return objectMapper.readValue(json, clazz);
    } catch (JsonProcessingException e) {
      throw new JsonException("Unable to convert from json: " + clazz, e);
    }
  }

}
