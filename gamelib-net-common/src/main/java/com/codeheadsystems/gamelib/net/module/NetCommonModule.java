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

package com.codeheadsystems.gamelib.net.module;

import com.codeheadsystems.gamelib.net.factory.ObjectMapperFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

/**
 * The type Net common module.
 */
@Module
public class NetCommonModule {

  /**
   * Object mapper object mapper.
   *
   * @param factory the factory
   * @return the object mapper
   */
  @Provides
  @Singleton
  public ObjectMapper objectMapper(final ObjectMapperFactory factory) {
    return factory.objectMapper();
  }

}
